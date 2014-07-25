package com.appshroom.leanpocket.fragments;

import android.accounts.Account;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.activities.MyApplication;
import com.appshroom.leanpocket.adapters.BoardSection;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitCallback;
import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.BoardSettings;
import com.appshroom.leanpocket.dto.BoardUser;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.CardType;
import com.appshroom.leanpocket.dto.ClassOfService;
import com.appshroom.leanpocket.dto.DeleteCardsReplyData;
import com.appshroom.leanpocket.dto.GetBoardsBoard;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LeanKitTreeifiedLane;
import com.appshroom.leanpocket.helpers.BoardHelpers;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.GravatarHelpers;
import com.appshroom.leanpocket.helpers.SecurePreferences;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.acra.ACRA;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by jpetrakovich on 8/25/13.
 */
public class LeanKitWorkerFragment extends Fragment {


    public interface LeanKitWorkerListener {

        public void onBoardsRetrieved(List<GetBoardsBoard> boards);

        public void onGetBoardsRetrofitError(RetrofitError error);

        public void onGetBoardsLeanKitException(int replyCode, String replyText);

        public void onBoardReadyForUse(Board board);

        public void onGetBoardRetrofitError(RetrofitError error);

        public void onGetBoardLeanKitException(int replyCode, String replyText);

        public void onArchiveReadyForUse(List<Lane> archive);

        public void onGetArchiveRetrofitError(RetrofitError error);

        public void onGetArchiveLeanKitException(int replyCode, String replyText);

        public void onDeleteSuccess(String replyText);

        public void onDeleteLeanKitException(int replyCode, String replyText);

        public void onDeleteRetrofitError(RetrofitError error);

    }

    LeanKitWorkerListener mLeanKitWorkerListener;
    RetroLeanKitApi mRetroLeanKitApi;

    //retained activity objects

    List<GetBoardsBoard> mLastRetrievedBoards;
    List<Lane> mLastRetrievedArchive;
    Board mLastActiveBoard;
    int mLastActiveLaneSpinnerSelection;
    Account mLastActiveAccount;
    String mLastActiveAccountAuthToken;
    BoardSection mInFlightSection;
    BoardSection mBacklogSection;
    BoardSection mArchiveSection;
    BoardSection.BoardSectionType mLastActiveSection;
    List<Card> mSelectedCards;
    boolean mRetryCroutonDisplayed;
    int mCardGridScrollY;
    boolean mDrawerProgressBarDisplayed;
    boolean mLoadingCroutonDisplayed;
    boolean mIsFiltered;
    private boolean isDrawerOpened;
    String mLoadingCroutonText;
    Gson gson = new Gson();

    public LeanKitWorkerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }

    public boolean isFiltered() {
        return mIsFiltered;
    }

    public void setIsFiltered(boolean mIsFiltered) {
        this.mIsFiltered = mIsFiltered;
    }

    public void setDrawerOpened(boolean drawerOpened) {
        this.isDrawerOpened = drawerOpened;
    }

    public boolean isDrawerOpened() {
        return isDrawerOpened;
    }

    public List<Card> getSelectedCards() {
        return mSelectedCards;
    }

    public void setSelectedCards(List<Card> mSelectedCards) {
        this.mSelectedCards = mSelectedCards;
    }

    public String getLoadingCroutonText() {
        return mLoadingCroutonText;
    }

    public void setLoadingCroutonText(String mLoadingCroutonText) {
        this.mLoadingCroutonText = mLoadingCroutonText;
    }

    public boolean isLoadingCroutonDisplayed() {
        return mLoadingCroutonDisplayed;
    }

    public void setLoadingProgressBarDisplayed(boolean mLoadingCroutonDisplayed) {
        this.mLoadingCroutonDisplayed = mLoadingCroutonDisplayed;
    }

    public boolean isDrawerProgressBarDisplayed() {
        return mDrawerProgressBarDisplayed;
    }

    public void setDrawerProgressBarDisplayed(boolean mDrawerProgressBarDisplayed) {
        this.mDrawerProgressBarDisplayed = mDrawerProgressBarDisplayed;
    }

    public int getCardGridScrollY() {
        return mCardGridScrollY;
    }

    public void setCardGridScrollY(int mCardGridScrollY) {
        this.mCardGridScrollY = mCardGridScrollY;
    }

    public BoardSection.BoardSectionType getLastActiveSection() {
        return mLastActiveSection;
    }

    public void setLastActiveSection(BoardSection.BoardSectionType mLastActiveSection) {
        this.mLastActiveSection = mLastActiveSection;
    }

    public boolean isRetryCroutonDisplayed() {
        return mRetryCroutonDisplayed;
    }

    public void setRetryCroutonDisplayed(boolean mRetryCroutonDisplayed) {
        this.mRetryCroutonDisplayed = mRetryCroutonDisplayed;
    }

    public BoardSection getInFlightSection() {
        return mInFlightSection;
    }

    public void setInFlightSection(BoardSection mInFlightSection) {
        this.mInFlightSection = mInFlightSection;

        if (mInFlightSection != null && mInFlightSection.isActive()) {

            setLastActiveSection(BoardSection.BoardSectionType.INFLIGHT);
        }
    }

    public BoardSection getBacklogSection() {
        return mBacklogSection;
    }

    public void setBacklogSection(BoardSection mBacklogSection) {
        this.mBacklogSection = mBacklogSection;

        if (mBacklogSection != null && mBacklogSection.isActive()) {

            setLastActiveSection(BoardSection.BoardSectionType.BACKLOG);
        }
    }

    public BoardSection getArchiveSection() {
        return mArchiveSection;
    }

    public void setArchiveSection(BoardSection mArchiveSection) {
        this.mArchiveSection = mArchiveSection;

        if (mArchiveSection != null && mArchiveSection.isActive()) {

            setLastActiveSection(BoardSection.BoardSectionType.ARCHIVE);
        }
    }

    public int getLastActiveLaneSpinnerSelection() {
        return mLastActiveLaneSpinnerSelection;
    }

    public void setLastActiveLane(int lastActiveLane) {
        mLastActiveLaneSpinnerSelection = lastActiveLane;
    }


    public List<GetBoardsBoard> getLastRetrievedBoards() {
        return mLastRetrievedBoards;
    }

    public void setLastRetrievedBoards(List<GetBoardsBoard> mLastRetrievedBoards) {
        this.mLastRetrievedBoards = mLastRetrievedBoards;
    }

    public Board getLastActiveBoard() {
        return mLastActiveBoard;
    }

    public void setLastActiveBoard(Board mLastActiveBoard) {
        this.mLastActiveBoard = mLastActiveBoard;
    }

    public Account getLastActiveAccount() {
        return mLastActiveAccount;
    }

    public void setLastActiveAccount(Account mLastActiveAccount) {
        this.mLastActiveAccount = mLastActiveAccount;
    }

    public List<Lane> getLastRetrievedArchive() {
        return mLastRetrievedArchive;
    }

    public void setLastRetrievedArchive(List<Lane> mLastRetrievedArchive) {
        this.mLastRetrievedArchive = mLastRetrievedArchive;
    }

    public String getLastActiveAccountAuthToken() {
        return mLastActiveAccountAuthToken;
    }

    public void setLastActiveAccountAuthToken(String mLastActiveAccountAuthToken) {
        this.mLastActiveAccountAuthToken = mLastActiveAccountAuthToken;
    }

    public void deleteCards(List<String> ids, String boardId) {


        mRetroLeanKitApi.deleteCards(ids, boardId, new RetroLeanKitCallback<DeleteCardsReplyData>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<DeleteCardsReplyData> replyData) {

                mLeanKitWorkerListener.onDeleteSuccess(getString(R.string.delete_success));


            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<DeleteCardsReplyData> replyData) {

                mLeanKitWorkerListener.onDeleteLeanKitException(replyCode, replyText);

            }

            @Override
            public void onWIPOverrideCommentRequired() {

            }

            @Override
            public void failure(RetrofitError retrofitError) {

                mLeanKitWorkerListener.onDeleteRetrofitError(retrofitError);
            }
        });

    }

    public void deleteCard(String id, String boardId) {

        mRetroLeanKitApi.deleteCard(boardId, id, new RetroLeanKitCallback<JsonObject>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<JsonObject> replyData) {

                mLeanKitWorkerListener.onDeleteSuccess(getString(R.string.delete_single_success));


            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<JsonObject> replyData) {

                mLeanKitWorkerListener.onDeleteLeanKitException(replyCode, replyText);

            }

            @Override
            public void onWIPOverrideCommentRequired() {

            }

            @Override
            public void failure(RetrofitError retrofitError) {

                mLeanKitWorkerListener.onDeleteRetrofitError(retrofitError);

            }
        });

    }

    public void getBoards() {

        MyApplication mApp = ((MyApplication) getActivity().getApplication());

        mRetroLeanKitApi = mApp.getRetroLeanKitApiInstance();

        mRetroLeanKitApi.getBoards(new RetroLeanKitCallback<List<GetBoardsBoard>>() {

            @Override
            public void onSuccess(int replyCode, String replyText, List<List<GetBoardsBoard>> replyData) {

                mLastRetrievedBoards = replyData.get(0);
                mLeanKitWorkerListener.onBoardsRetrieved(mLastRetrievedBoards);
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<List<GetBoardsBoard>> replyData) {
                mLeanKitWorkerListener.onGetBoardsLeanKitException(replyCode, replyText);
            }

            @Override
            public void onWIPOverrideCommentRequired() {

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mLeanKitWorkerListener.onGetBoardsRetrofitError(retrofitError);
            }
        });

    }

    public void getBoard(final String boardId) {

        mRetroLeanKitApi.getBoard(boardId, new RetroLeanKitCallback<Board>() {

            @Override
            public void onSuccess(int replyCode, String replyText, List<Board> replyData) {

                String userName = new SecurePreferences(getActivity()).getString(Consts.SHARED_PREFS_USER_NAME, "");

                BoardData bd = new BoardData(replyData.get(0), userName);

                new StructureBoardTask().execute(bd);
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<Board> replyData) {
                mLeanKitWorkerListener.onGetBoardLeanKitException(replyCode, replyText);
            }

            @Override
            public void onWIPOverrideCommentRequired() {

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mLeanKitWorkerListener.onGetBoardRetrofitError(retrofitError);
            }
        });

    }

    public void getArchive(String boardId) {

        mRetroLeanKitApi.archive(boardId, new RetroLeanKitCallback<List<LeanKitTreeifiedLane>>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<List<LeanKitTreeifiedLane>> replyData) {

                new PrepareArchiveTask().execute(replyData.get(0).get(0));
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<List<LeanKitTreeifiedLane>> replyData) {
                mLeanKitWorkerListener.onGetArchiveLeanKitException(replyCode, replyText);
            }

            @Override
            public void onWIPOverrideCommentRequired() {

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                mLeanKitWorkerListener.onGetArchiveRetrofitError(retrofitError);
            }
        });
    }

    private class BoardData{

        public String userName;
        public Board board;

        public BoardData(Board board, String userName){
            this.userName = userName;
            this.board = board;
        }
    }

    private class StructureBoardTask extends AsyncTask<BoardData, Void, Board> {

        @Override
        protected Board doInBackground(BoardData... params) {

            Board board = params[0].board;
            String userName = params[0].userName;

            ACRA.getErrorReporter().putCustomData("board", gson.toJson(board));


            //Temp

            //   String bd = readTxt();

            //  board = gson.fromJson(bd, Board.class);

            //End Temp

            BoardHelpers.treeifyBoard(board);

            BoardHelpers.applyContextualLaneNames("", board.getTreeifiedLanes());
            BoardHelpers.applyContextualLaneNames("", Arrays.asList(board.getTreeifiedBacklog()));
            BoardHelpers.applyContextualLaneNames("", Arrays.asList(board.getTreeifiedArchive()));

            List<Lane> orderedInFlightChildLanes = BoardHelpers.getCardHoldableLanesInOrder(board.getTreeifiedLanes());
            List<Lane> orderedBacklogChildLanes = BoardHelpers.getCardHoldableLanesInOrder(Arrays.asList(board.getTreeifiedBacklog()));
            List<Lane> orderedArchiveChildLanes = BoardHelpers.getCardHoldableLanesInOrder(Arrays.asList(board.getTreeifiedArchive()));

            setLaneBoardSectionTypes(orderedInFlightChildLanes, BoardSection.BoardSectionType.INFLIGHT);
            setLaneBoardSectionTypes(orderedBacklogChildLanes, BoardSection.BoardSectionType.BACKLOG);
            setLaneBoardSectionTypes(orderedArchiveChildLanes, BoardSection.BoardSectionType.ARCHIVE);

            ArrayList<Lane> allCardHoldableLanes = new ArrayList<Lane>();
            allCardHoldableLanes.addAll(orderedInFlightChildLanes);
            allCardHoldableLanes.addAll(orderedBacklogChildLanes);
            allCardHoldableLanes.addAll(orderedArchiveChildLanes);

            board.setAllOrderedChildLanes(allCardHoldableLanes);

            board.setOrderedInFlightChildLanes(orderedInFlightChildLanes);
            board.setOrderedBacklogChildLanes(orderedBacklogChildLanes);
            board.setOrderedArchiveChildLanes(orderedArchiveChildLanes);

            board.setCardsAssignedToAppUser(
                    BoardHelpers.getCardsAssignedToUser( userName
                          , BoardHelpers.getAllCards( board.getOrderedInFlightChildLanes()))
            );

            ClassOfService defaultCOS = new ClassOfService("None");
            board.getClassesOfService().add(0, defaultCOS);

            HashMap<String, Integer> colorMap = generateCardColorsFromHex(board.getCardTypes(), board.getClassesOfService());

            HashMap<Integer, Integer> accentColorMap = generateCardAccentColors(colorMap);

            HashMap<String, String> idToGravatarUrlMap = generateGravatarURLs(board.getBoardUsers());

            board.setCardColorMap(colorMap);
            board.setCardAccentColorMap(accentColorMap);
            board.setUserGravatarUrlMap(idToGravatarUrlMap);

            board.setDateFormat(getDateFormat(board.getBoardUsers()));

            board.setSettings( getBoardSettings(board) );

            return board;
        }


        @Override
        protected void onPostExecute(Board board) {

            mLeanKitWorkerListener.onBoardReadyForUse(board);
        }
    }

    private BoardSettings getBoardSettings(Board board){

        BoardSettings settings = new BoardSettings();

        settings.setCardTypes( board.getCardTypes() );
        settings.setClassOfServices( board.getClassesOfService() );
        settings.setBoardUsers( board.getBoardUsers() );

        settings.setDateFormat( board.getDateFormat() );
        settings.setCardIdPrefix( board.getPrefix() );

        settings.setUsesClassOfService( board.isClassOfServiceEnabled() );
        settings.setUsesClassOfServiceColor( board.getCardColorField().equals(Consts.COLOR_FIELD_CLASS_OF_SERVICE) );
        settings.setUsesExternalCardId( board.isCardIdEnabled() );
        settings.setCardHeaderEnabled( board.isHeaderEnabled() );
        settings.setCardIdPrefixEnabled( board.isPrefixEnabled() );
        settings.setAutoIncrementCardIdEnabled( board.isAutoIncrementCardIdEnabled() );

        return settings;

    }

    private void setLaneBoardSectionTypes(List<Lane> lanes, BoardSection.BoardSectionType type) {

        for (Lane lane : lanes) {

            lane.setBoardSectionType(type);
        }

    }

    private String getDateFormat(List<BoardUser> users) {

        for (BoardUser user : users) {

            if (user.isAccountOwner()) {

                return user.getDateFormat();
            }
        }

        return "MM/dd/yyyy";
    }

    private HashMap<String, Integer> generateCardColorsFromHex(List<CardType> cardTypes, List<ClassOfService> classesOfService) {

        HashMap<String, Integer> colorSet = new HashMap<String, Integer>();

        for (CardType type : cardTypes) {

            String colorHex = type.getColorHex();
            Integer color = Color.parseColor(colorHex);

            colorSet.put(colorHex, color);
        }

        for (ClassOfService cos : classesOfService) {

            String colorHex = cos.getColorHex();
            Integer color = Color.parseColor(colorHex);

            colorSet.put(colorHex, color);
        }


        return colorSet;
    }


    private HashMap<Integer, Integer> generateCardAccentColors(HashMap<String, Integer> colorSet) {

        HashMap<Integer, Integer> accentColorSet = new HashMap<Integer, Integer>();

        for (Integer color : colorSet.values()) {

            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] = hsv[2] * 0.5f;

            Integer accentColor = Color.HSVToColor(hsv);

            accentColorSet.put(color, accentColor);
        }

        return accentColorSet;
    }

    private HashMap<String, String> generateGravatarURLs(List<BoardUser> boardUsers) {

        HashMap<String, String> idToGravatarUrlMap = new HashMap<String, String>();

        int size = ((Activity) mLeanKitWorkerListener).getResources().getInteger(R.integer.gravatar_grid_size);

        for (BoardUser user : boardUsers) {

            String gravUrl = GravatarHelpers.buildGravatarUrl(user.getGravatarLink(), size);

            idToGravatarUrlMap.put(user.getId(), gravUrl);
        }

        return idToGravatarUrlMap;
    }


    private void removeGhostCards(List<Lane> archiveLanes) {

        for (Lane lane : archiveLanes) {

            List<Card> cards = lane.getCards();

            for (Card card : cards) {

                if (card.getSystemType().equals(Consts.CARD_SYSTEM_TYPE.GHOST_CARD)) {

                    cards.remove(card);

                }
            }
        }
    }

    private class PrepareArchiveTask extends AsyncTask<LeanKitTreeifiedLane, Void, List<Lane>> {


        @Override
        protected List<Lane> doInBackground(LeanKitTreeifiedLane... params) {

            LeanKitTreeifiedLane archive = params[0];

            ACRA.getErrorReporter().putCustomData("archive", gson.toJson(archive));

            BoardHelpers.applyContextualLaneNamesToArchive("", Arrays.asList(archive));

            List<Lane> orderedArchiveChildLanes = BoardHelpers.getCardHoldableLanesInOrderFromArchive(archive);
            setLaneBoardSectionTypes(orderedArchiveChildLanes, BoardSection.BoardSectionType.ARCHIVE);
            removeGhostCards(orderedArchiveChildLanes);

            return orderedArchiveChildLanes;

        }

        @Override
        protected void onPostExecute(List<Lane> orderedArchiveLanes) {

            mLeanKitWorkerListener.onArchiveReadyForUse(orderedArchiveLanes);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mLeanKitWorkerListener = (LeanKitWorkerListener) activity;
    }

    private String readTxt() {

        InputStream inputStream = getResources().openRawResource(R.raw.test);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }

}
