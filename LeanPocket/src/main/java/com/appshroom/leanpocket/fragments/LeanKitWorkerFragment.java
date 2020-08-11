package com.appshroom.leanpocket.fragments;

import android.accounts.Account;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.activities.MyApplication;
import com.appshroom.leanpocket.adapters.BoardSection;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApiV2;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApiV2Callback;
import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.BoardSettings;
import com.appshroom.leanpocket.dto.BoardUser;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.CardType;
import com.appshroom.leanpocket.dto.ClassOfService;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.v2.DeleteCardsRequest;
import com.appshroom.leanpocket.dto.v2.ListBoardsBoard;
import com.appshroom.leanpocket.dto.v2.ListBoardsResponse;
import com.appshroom.leanpocket.dto.v2.ListCardsResponse;
import com.appshroom.leanpocket.helpers.BoardHelpers;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.GravatarHelpers;
import com.appshroom.leanpocket.helpers.SecurePreferences;
import com.google.gson.Gson;

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

        public void onBoardsRetrieved(List<ListBoardsBoard> boards);

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
    RetroLeanKitApiV2 mRetroLeanKitApiV2;

    //retained activity objects

    List<ListBoardsBoard> mLastRetrievedBoards;
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


    public List<ListBoardsBoard> getLastRetrievedBoards() {
        return mLastRetrievedBoards;
    }

    public void setLastRetrievedBoards(List<ListBoardsBoard> mLastRetrievedBoards) {
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

    public void deleteCards(List<String> ids) {

        DeleteCardsRequest request = new DeleteCardsRequest();
        request.cardIds = ids;

        mRetroLeanKitApiV2.deleteCards(request, new RetroLeanKitApiV2Callback<Void>() {

            @Override
            public void onSuccess(int replyCode, String replyText, List<Void> replyData) {
                mLeanKitWorkerListener.onDeleteSuccess(getString(R.string.delete_success));
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<Void> replyData) {
                mLeanKitWorkerListener.onDeleteLeanKitException(replyCode, replyText);
            }

            @Override
            public void failure(RetrofitError error) {
                mLeanKitWorkerListener.onDeleteRetrofitError(error);
            }
        });
    }

    public void deleteCard(String id) {

        mRetroLeanKitApiV2.deleteCard(id, new RetroLeanKitApiV2Callback<Void>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<Void> replyData) {
                mLeanKitWorkerListener.onDeleteSuccess(getString(R.string.delete_single_success));
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<Void> replyData) {
                mLeanKitWorkerListener.onDeleteLeanKitException(replyCode, replyText);
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
        mRetroLeanKitApiV2 = mApp.getRetroLeanKitApiV2Instance();

        mRetroLeanKitApiV2.listBoards(new RetroLeanKitApiV2Callback<ListBoardsResponse>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<ListBoardsResponse> replyData) {
                mLastRetrievedBoards = replyData.get(0).getBoards();
                mLeanKitWorkerListener.onBoardsRetrieved(mLastRetrievedBoards);
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<ListBoardsResponse> replyData) {
                mLeanKitWorkerListener.onGetBoardsLeanKitException(replyCode, replyText);
            }

            @Override
            public void failure(RetrofitError error) {
                mLeanKitWorkerListener.onGetBoardsRetrofitError(error);
            }
        });
    }

    public void getBoard(final String boardId) {

        mRetroLeanKitApiV2.getBoardDetails(boardId, new RetroLeanKitApiV2Callback<Board>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<Board> replyData) {
                Context c = getActivity();

                if (c != null) {
                    String userName = new SecurePreferences(getActivity()).getString(Consts.SHARED_PREFS_USER_NAME, "");

                    new StructureBoardTask().execute(new BoardData(replyData.get(0), userName));
                }
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<Board> replyData) {
                mLeanKitWorkerListener.onGetBoardLeanKitException(replyCode, replyText);
            }

            @Override
            public void failure(RetrofitError error) {
                mLeanKitWorkerListener.onGetBoardRetrofitError(error);
            }
        });
    }

    public void getCardsForBacklogAndActiveLanes(final Board board) {

        mRetroLeanKitApiV2.listCards(board.getId(),"backlog,active", 999999999, new RetroLeanKitApiV2Callback<ListCardsResponse>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<ListCardsResponse> replyData) {

                List<Card> cards = replyData.get(0).getCards();

                //TODO: migrating to v2 leankit API has made maintaining all these different lists of lanes pointless.
                //so we ought to get rid of them.

                BoardHelpers.setCardsOnLanes(cards, board.getOrderedBacklogChildLanes());
                BoardHelpers.setCardsOnLanes(cards, board.getOrderedInFlightChildLanes());

                mLeanKitWorkerListener.onBoardReadyForUse(board);
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<ListCardsResponse> replyData) {
                mLeanKitWorkerListener.onGetBoardLeanKitException(replyCode, replyText);
            }

            @Override
            public void failure(RetrofitError error) {
                mLeanKitWorkerListener.onGetBoardRetrofitError(error);
            }
        });
    }

    public class ArchiveData {
        public Board board;
        public List<Card> cards;
    }

    public void getArchive(final Board board) {

        mRetroLeanKitApiV2.listCards(board.getId(), "archive", 9999999, new RetroLeanKitApiV2Callback<ListCardsResponse>() {
            @Override
            public void onSuccess(int replyCode, String replyText, List<ListCardsResponse> replyData) {

                ArchiveData ad = new ArchiveData();
                ad.board = board;
                ad.cards = replyData.get(0).getCards();

                new PrepareArchiveTask().execute(ad);
            }

            @Override
            public void onLeanKitException(int replyCode, String replyText, List<ListCardsResponse> replyData) {
                mLeanKitWorkerListener.onGetArchiveLeanKitException(replyCode, replyText);
            }

            @Override
            public void failure(RetrofitError error) {
                mLeanKitWorkerListener.onGetArchiveRetrofitError(error);
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

            BoardHelpers.treeifyBoard(board);

            BoardHelpers.applyContextualLaneNames("", board.getTreeifiedLanes());
            BoardHelpers.applyContextualLaneNames("", Arrays.asList(board.getTreeifiedBacklog()));
            BoardHelpers.applyContextualLaneNames("", Arrays.asList(board.getTreeifiedArchive()));

            List<Lane> orderedInFlightChildLanes = BoardHelpers.getCardHoldableLanesInOrder(board.getTreeifiedLanes());
            List<Lane> orderedBacklogChildLanes = BoardHelpers.getCardHoldableLanesInOrder(Arrays.asList(board.getTreeifiedBacklog()));
            List<Lane> orderedArchiveChildLanes = BoardHelpers.getCardHoldableLanesInOrder(Arrays.asList(board.getTreeifiedArchive()));

            ArrayList<Lane> allCardHoldableLanes = new ArrayList<Lane>();
            allCardHoldableLanes.addAll(orderedInFlightChildLanes);
            allCardHoldableLanes.addAll(orderedBacklogChildLanes);
            allCardHoldableLanes.addAll(orderedArchiveChildLanes);

            board.setAllOrderedChildLanes(allCardHoldableLanes);
            board.setAllChildLaneNames(allCardHoldableLanes);
            board.setOrderedInFlightChildLanes(orderedInFlightChildLanes);
            board.setOrderedBacklogChildLanes(orderedBacklogChildLanes);
            board.setOrderedArchiveChildLanes(orderedArchiveChildLanes);

            //TODO: cards now come in a different API call so continue structuring in that response
            //and call boardReadyForUse after that...
            //same with board users...
            //I could use RxJava and call them at the same time but may be overkill.
            //I could still call them at the same time and just have them synchronize in a basic way
            //with a flag.

//            board.setCardsAssignedToAppUser(
//                    BoardHelpers.getCardsAssignedToUser( userName
//                          , BoardHelpers.getAllCards( board.getOrderedInFlightChildLanes()))
//            );

            //TODO: make sure I test class of service on a board that has them.
            ClassOfService defaultCOS = new ClassOfService("None");
            board.getClassesOfService().add(0, defaultCOS);

            HashMap<String, Integer> colorMap = generateCardColorsFromHex(board.getCardTypes(), board.getClassesOfService());

            HashMap<Integer, Integer> accentColorMap = generateCardAccentColors(colorMap);

            //TODO: users is a separate API call, emailaddress is given instead of gravatar.
            HashMap<String, String> idToGravatarUrlMap = generateGravatarURLs(board.getUsers());

            board.setCardColorMap(colorMap);
            board.setCardAccentColorMap(accentColorMap);
            board.setUserGravatarUrlMap(idToGravatarUrlMap);

            board.setDateFormat(getDateFormat(board.getUsers()));

            board.setSettings( getBoardSettings(board) );

            return board;
        }


        @Override
        protected void onPostExecute(Board board) {

            getCardsForBacklogAndActiveLanes(board);
            //mLeanKitWorkerListener.onBoardReadyForUse(board);
        }
    }

    private BoardSettings getBoardSettings(Board board){

        BoardSettings settings = new BoardSettings();

        settings.setCardTypes( board.getCardTypes() );
        settings.setClassOfServices( board.getClassesOfService() );
        settings.setBoardUsers( board.getUsers() );

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

    private class PrepareArchiveTask extends AsyncTask<ArchiveData, Void, List<Lane>> {

        @Override
        protected List<Lane> doInBackground(ArchiveData... params) {

            ArchiveData archiveData = params[0];

            BoardHelpers.setCardsOnLanes(archiveData.cards, archiveData.board.getOrderedArchiveChildLanes());

            return archiveData.board.getOrderedArchiveChildLanes();

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
