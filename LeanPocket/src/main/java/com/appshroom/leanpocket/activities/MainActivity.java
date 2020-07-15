package com.appshroom.leanpocket.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OnAccountsUpdateListener;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.adapters.AccountSpinnerAdapter;
import com.appshroom.leanpocket.adapters.BoardSection;
import com.appshroom.leanpocket.adapters.CardListAdapter;
import com.appshroom.leanpocket.adapters.DrawerListAdapter;
import com.appshroom.leanpocket.adapters.DrawerListItem;
import com.appshroom.leanpocket.adapters.LanesWithCardAmountsSpinnerAdapter;
import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.v2.ListBoardsBoard;
import com.appshroom.leanpocket.fragments.ConfirmDeleteCardDialog;
import com.appshroom.leanpocket.fragments.LeanKitWorkerFragment;
import com.appshroom.leanpocket.fragments.MoveCardDialog;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.SecurePreferences;
import com.emilsjolander.components.stickylistheaders.StickyListHeadersListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import org.apache.http.HttpStatus;
import org.codechimp.apprater.AppRater;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.LifecycleCallback;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.RetrofitError;

public class MainActivity extends Activity

        implements ConfirmDeleteCardDialog.OnDeleteCardDialogChoiceListener,
        MoveCardDialog.OnMoveCardDialogChoiceListener,
        LeanKitWorkerFragment.LeanKitWorkerListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        OnAccountsUpdateListener {

    private DrawerLayout mDrawerLayout;
    private ProgressBar mGetBoardsProgressBar;
    private ProgressBar mProgressLoading;
    private LinearLayout mDrawerView;
    private StickyListHeadersListView mDrawerList;
    private DrawerListAdapter mDrawerListStickyAdapter;
    private SwingBottomInAnimationAdapter mSwingInAnimationAdapter;
    private Spinner mAccountsSpinner;
    private Spinner mLanesHeaderSpinner;
    private LanesWithCardAmountsSpinnerAdapter mLanesHeaderSpinnerAdapter;
    private AccountSpinnerAdapter mAccountSpinnerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private GridView mCardGrid;
    private CardListAdapter mCardListAdapter;

    private SharedPreferences mSharedPreferences;
    private AccountManager mAccountManager;
    private Account mActiveAccount;
    private List<Account> mAvailableAccounts;

    private Board mActiveBoard;
    private String mUserName="";
    private int mActiveLaneSpinnerSelection;
    private int mCardGridScrollY;
    private BoardSection mInFlightSection;
    private BoardSection mBacklogSection;
    private BoardSection mArchiveSection;

    //using this only for network connection losses during board clicks.
    private BoardSection.BoardSectionType mLastActiveSection;

    private String mLastUsedBoardId;
    private String mLastUsedBoardAccount;
    private List<ListBoardsBoard> mAvailableGetBoardsBoards;
    private String mActiveAccountAuthToken;
    private String mLastUsedAccount;
    private List<Card> mSelectedCards;
    private boolean mSingleColumnMode;
    private Map<String, String> mDefaultLaneMap;
    private Gson gson;

    private boolean mAnimateCards;

    private Crouton mRetryCrouton;
    private boolean mRetryCroutonDisplayed;
    private boolean mProgressLoadingDisplayed;
    private View mErrorCroutonLayout;
    private TextView mErrorCroutonText;
    private boolean mInitializedView;
    private boolean mInitializedAccountSpinner;
    private boolean mIsFiltered = false;
    private TextView mFilterTitle;
    private TextView mEmptyView;

    private LeanKitWorkerFragment mLeanKitWorker;
    private ProgressDialog pd;
    private ActionMode mCAB;
    private Menu mMenu;

    private int mLongAnimationDuration;

    private enum CROUTON_TYPE {CONFIRM, ALERT, INFO, LOADING}

    private boolean mIsFirstGetBoardsCall = true;
    private boolean mUserHasLearnedDrawer = false;
    private boolean mAutoLoadLastBoard;
    private boolean mDrawerWasOpenedBeforeConfigChanged = false;
    private boolean mShowArchivedBoards = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableLogo();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        initRememberedSettings();
        initNavigationDrawer();
        initMainView();

        populateAccountsSpinner();

        //Begin post instantiation work

        FragmentManager fm = getFragmentManager();
        mLeanKitWorker = (LeanKitWorkerFragment) fm.findFragmentByTag("lkWorker");

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.

        if (mLeanKitWorker == null) {
            mLeanKitWorker = new LeanKitWorkerFragment();
            fm.beginTransaction().add(mLeanKitWorker, "lkWorker").commit();

            mDrawerLayout.openDrawer(GravityCompat.START);

            useRememberedAccount();


        } else {

            repopulateFromConfigChange();
        }

        AppRater.app_launched(this);
    }

    private void populateAccountsSpinner() {

        mAccountManager = AccountManager.get(this);
        mAccountManager.addOnAccountsUpdatedListener(this, null, false);

        mAvailableAccounts = Arrays.asList(mAccountManager.getAccountsByType(Consts.LEANKIT_ACCOUNT_TYPE));

        if (mAvailableAccounts.size() == 0) {

            mAccountManager.addAccount(Consts.LEANKIT_ACCOUNT_TYPE, Consts.LEANKIT_AUTH_TOKEN_TYPE, null,
                    null, null, new OnAddAccountTokenAcquired(), null);

        } else {

            mAccountSpinnerAdapter.clear();
            mAccountSpinnerAdapter.addAll(mAvailableAccounts);
            mAccountSpinnerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {

        mAvailableAccounts = Arrays.asList(mAccountManager.getAccountsByType(Consts.LEANKIT_ACCOUNT_TYPE));

        mAccountSpinnerAdapter.clear();
        mAccountSpinnerAdapter.addAll(mAvailableAccounts);

        mAccountSpinnerAdapter.notifyDataSetChanged();
    }

    private void initRememberedSettings() {

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        mSharedPreferences = new SecurePreferences(this);
        mLastUsedBoardId = mSharedPreferences.getString(Consts.SHARED_PREFS_LAST_USED_BOARD_ID, "");
        mLastUsedBoardAccount = mSharedPreferences.getString(Consts.SHARED_PREFS_LAST_USED_BOARD_ACCOUNT, "");
        mUserHasLearnedDrawer = mSharedPreferences.getBoolean(Consts.SHARED_PREFS_USER_HAS_LEARNED_DRAWER, false);
        mLastUsedAccount = mSharedPreferences.getString(Consts.SHARED_PREFS_LAST_USED_ACCOUNT, "");
        mSingleColumnMode = mSharedPreferences.getBoolean(Consts.SHARED_PREFS_SINGLE_COL_MODE, false);

        String serializedDefaultLaneMap = mSharedPreferences.getString(Consts.SHARED_PREFS_DEFAULT_LANE_MAP, "");
        gson = new Gson();

        if (serializedDefaultLaneMap != ""){
            Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
            mDefaultLaneMap = gson.fromJson(serializedDefaultLaneMap, stringStringMap);
        } else {
            mDefaultLaneMap = new HashMap<String, String>();
        }

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        mAnimateCards = settings.getBoolean(Consts.SHARED_PREFS_ANIMATE_CARDS, true);
        mAutoLoadLastBoard = settings.getBoolean(Consts.SHARED_PREFS_AUTO_LOAD, true);
        mShowArchivedBoards = settings.getBoolean(Consts.SHARED_PREFS_SHOW_ARCHIVED_BOARDS, false);

    }

    private void initMainView() {

        mLanesHeaderSpinner = (Spinner) findViewById(R.id.spinner_lanes_header);
        mLanesHeaderSpinnerAdapter = new LanesWithCardAmountsSpinnerAdapter(getContext(), R.layout.lane_list_item, new ArrayList<Lane>());
        mLanesHeaderSpinner.setAdapter(mLanesHeaderSpinnerAdapter);
        mLanesHeaderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mInitializedView == false) {
                    mInitializedView = true;
                } else {

                    dismissContextualActionBar();

                    mActiveLaneSpinnerSelection = position;

                    Lane selectedLane = (Lane) parent.getSelectedItem();

                    invalidateOptionsMenu();

                    mCardListAdapter.clear();
                    mCardListAdapter.addAll(selectedLane.getCards());

                    resetCardGridAdapter();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFilterTitle = (TextView) findViewById(R.id.filter_textview);
        mEmptyView = (TextView) findViewById(R.id.emptyText);


        mCardGrid = (GridView) findViewById(R.id.gridView_cards);

        if (mSingleColumnMode) {

            mCardGrid.setNumColumns(1);

        } else {

            mCardGrid.setNumColumns(getResources().getInteger(R.integer.num_columns_multi));
        }


        mSelectedCards = new ArrayList<Card>();

        mCardGrid.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                Card card = (Card) mCardGrid.getItemAtPosition(position);

                if (checked) {

                    mSelectedCards.add(card);

                } else {

                    mSelectedCards.remove(card);

                }

                int numSelected = mSelectedCards.size();

                if (numSelected == 0) {

                    mode.finish();
                    return;
                }

                mode.getMenu().findItem(R.id.contextual_menu_move).setVisible(!(numSelected > 1));
                mode.getMenu().findItem(R.id.contextual_menu_edit).setVisible(!(mSelectedCards.size() > 1));

                setCABTitle(numSelected);

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                mCAB = mode;

                if (mSelectedCards == null) {

                    return false;

                }

                setCABTitle(mSelectedCards.size());

                MenuInflater inflater = mode.getMenuInflater();

                inflater.inflate(R.menu.contextual, menu);

                menu.findItem(R.id.contextual_menu_move).setVisible(!(mSelectedCards.size() > 1));
                menu.findItem(R.id.contextual_menu_edit).setVisible(!(mSelectedCards.size() > 1));

                return true;


            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.contextual_menu_delete:

                        showDeleteCardsDialog(new ArrayList<Card>(mSelectedCards));
                        // mode.finish();
                        return true;

                    case R.id.contextual_menu_move:

                        showMoveCardDialog();
                        mode.finish();
                        return true;

                    case R.id.contextual_menu_edit:

                        startEditActivity();
                        mode.finish();
                        return true;
                }


                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

                mCAB = null;

                mSelectedCards.clear();

            }
        });

        mCardListAdapter = new CardListAdapter(this, R.layout.grid_card_item, new ArrayList<Card>());
        mSwingInAnimationAdapter = new SwingBottomInAnimationAdapter(mCardListAdapter, 50, 400);
        mSwingInAnimationAdapter.setAbsListView(mCardGrid);

        if (mAnimateCards) {

            mCardGrid.setAdapter(mSwingInAnimationAdapter);

        } else {

            mCardGrid.setAdapter(mCardListAdapter);
        }


        mCardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Card selectedCard = (Card) parent.getItemAtPosition(position);

                startCardDetailActivity(selectedCard);
            }
        });

        mCardGrid.setEmptyView(findViewById(android.R.id.empty));
        mCardGridScrollY = 0;

        initProgressLoading();
        initErrorCroutonLayout();

    }

    private void SetDefaultLaneMenuItemTitle(Lane selectedLane) {

        if (mMenu == null){

        }

        MenuItem setStartingLaneMenuItem = mMenu.findItem(R.id.action_set_starting_lane);

        if (mActiveBoard != null) {

            String key = mActiveBoard.getId();

            if (mDefaultLaneMap.containsKey(key) && mDefaultLaneMap.get(key).equals(selectedLane.getId())) {
                setStartingLaneMenuItem.setTitle("Remove default starting lane");
            } else {
                setStartingLaneMenuItem.setTitle("Use current lane as starting lane");
            }
        }
    }

    private void setCABTitle(int numSelected) {
        mCAB.setTitle(Integer.toString(numSelected) + " " + getString(R.string.selected));
    }

    private void initNavigationDrawer() {

        initRetryCrouton();

        mGetBoardsProgressBar = (ProgressBar) findViewById(R.id.progress_bar_drawer);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerView = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (StickyListHeadersListView) findViewById(R.id.drawer_list_sticky);

        mAccountsSpinner = (Spinner) findViewById(R.id.drawer_spinner_accounts);
        mAccountSpinnerAdapter = new AccountSpinnerAdapter(this, R.layout.account_spinner_item, new ArrayList<Account>());
        mAccountsSpinner.setAdapter(mAccountSpinnerAdapter);

        mAccountsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mInitializedAccountSpinner == false) {
                    mInitializedAccountSpinner = true;

                } else {

                    mActiveAccount = (Account) parent.getItemAtPosition(position);

                    refreshAvailableBoards();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mTitle = getTitle();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

                if (mActiveBoard != null) {

                    enableTitle();

                } else {

                    enableLogo();

                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

                enableLogo();

                if (mUserHasLearnedDrawer == false) {

                    mSharedPreferences.edit().putBoolean(Consts.SHARED_PREFS_USER_HAS_LEARNED_DRAWER, true).apply();
                    mUserHasLearnedDrawer = true;

                }

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerListStickyAdapter = new DrawerListAdapter(this, new ArrayList<DrawerListItem>());
        mDrawerList.setAdapter(mDrawerListStickyAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DrawerListItem selectedItem = (DrawerListItem) parent.getItemAtPosition(position);

                switch (selectedItem.getType()) {

                    case BOARD:
                        handleBoardClick((ListBoardsBoard) selectedItem);
                        break;

                    case SECTION:
                        handleBoardSectionClick((BoardSection) selectedItem);
                        break;

                }

                mDrawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        initBoardSections();

    }

    private void enableTitle() {
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setTitle(mTitle);
    }

    private void initRetryCrouton() {

        View retryCroutonView = getLayoutInflater().inflate(R.layout.crouton_retry, null);
        mRetryCrouton = Crouton.make(this, retryCroutonView, R.id.retry_crouton_viewgroup).setConfiguration(CONFIGURATION_INFINITE);

        Button retryButton = (Button) retryCroutonView.findViewById(R.id.btn_crouton_retry);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refreshAvailableBoards();
                Crouton.cancelAllCroutons();
                mRetryCroutonDisplayed = false;

            }
        });
    }

    private void initErrorCroutonLayout() {

        mErrorCroutonLayout = getLayoutInflater().inflate(R.layout.crouton_layout, null);

        mErrorCroutonText = (TextView) mErrorCroutonLayout.findViewById(R.id.tv_crouton_text);
    }

    private void initProgressLoading() {

        mProgressLoading = (ProgressBar) findViewById(R.id.progress_main_loading);

        mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
    }

    private void initBoardSections() {

        mInFlightSection = new BoardSection(getString(R.string.in_flight_lanes), BoardSection.BoardSectionType.INFLIGHT);
        mBacklogSection = new BoardSection(getString(R.string.backlog_lanes), BoardSection.BoardSectionType.BACKLOG);
        mArchiveSection = new BoardSection(getString(R.string.archive_lanes), BoardSection.BoardSectionType.ARCHIVE);

    }

    private void repopulateFromConfigChange() {

        mAvailableGetBoardsBoards = mLeanKitWorker.getLastRetrievedBoards();

        mSelectedCards = mLeanKitWorker.getSelectedCards();

        mActiveAccount = mLeanKitWorker.getLastActiveAccount();
        mActiveAccountAuthToken = mLeanKitWorker.getLastActiveAccountAuthToken();
        mActiveBoard = mLeanKitWorker.getLastActiveBoard();
        mActiveLaneSpinnerSelection = mLeanKitWorker.getLastActiveLaneSpinnerSelection();
        mCardGridScrollY = mLeanKitWorker.getCardGridScrollY();
        mDrawerWasOpenedBeforeConfigChanged = mLeanKitWorker.isDrawerOpened();
        mFilterTitle.setVisibility(View.GONE);

        if (mAvailableGetBoardsBoards == null) {

            selectAccount(mAccountsSpinner.getSelectedItemPosition());
            dismissContextualActionBar();


        } else {

            mInFlightSection = mLeanKitWorker.getInFlightSection();
            mBacklogSection = mLeanKitWorker.getBacklogSection();
            mArchiveSection = mLeanKitWorker.getArchiveSection();
            mLastActiveSection = mLeanKitWorker.getLastActiveSection();
            mIsFiltered = mLeanKitWorker.isFiltered();

            mDrawerListStickyAdapter.clear();
            mDrawerListStickyAdapter.addAll(mAvailableGetBoardsBoards);
            mDrawerListStickyAdapter.add(mInFlightSection);
            mDrawerListStickyAdapter.add(mBacklogSection);
            mDrawerListStickyAdapter.add(mArchiveSection);

            mDrawerListStickyAdapter.notifyDataSetChanged();

            if (mActiveBoard != null) {

                populateUI(mActiveBoard);

            }
        }


        if (mLeanKitWorker.isDrawerProgressBarDisplayed()) {
            mGetBoardsProgressBar.setVisibility(View.VISIBLE);
        }

        if (mLeanKitWorker.isLoadingCroutonDisplayed()) {
            showLoadingProgress();
        } else {
            hideLoadingProgress();
        }

        if (mLeanKitWorker.isRetryCroutonDisplayed()) {
            mRetryCroutonDisplayed = true;
            mRetryCrouton.show();
        }

        FragmentManager fm = getFragmentManager();
        ConfirmDeleteCardDialog mDeleteDialog = (ConfirmDeleteCardDialog) fm.findFragmentByTag("ConfirmDeleteCardDialog");

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.

        if (mDeleteDialog == null) {


        } else {


            fm.beginTransaction().remove(mDeleteDialog).commit();
            showDeleteCardsDialog(mSelectedCards);

        }
    }

    private void refreshAvailableBoards() {

        mDrawerListStickyAdapter.clear();
        mDrawerListStickyAdapter.notifyDataSetChanged();

        clearUI();

        mAccountManager.getAuthToken(mActiveAccount, Consts.LEANKIT_AUTH_TOKEN_TYPE, null, null,
                new OnAccountSelectedTokenAcquired(), null);

    }

    private void refreshBoard() {

        if (mActiveBoard == null) {

            showCrouton(getResources().getString(R.string.must_have_board), CROUTON_TYPE.ALERT);
        } else {

            showLoadingProgress();

            mLeanKitWorker.getBoard(mActiveBoard.getId());

        }
    }

    private void populateUI(Board board) {

        mActiveBoard = board;

        mCardListAdapter.setColorMap(mActiveBoard.getCardColorMap());
        mCardListAdapter.setAccentColorMap(mActiveBoard.getCardAccentColorMap());
        mCardListAdapter.setGravatarUrlMap(mActiveBoard.getUserGravatarUrlMap());
        mCardListAdapter.setBoardSettings(mActiveBoard.getSettings());

        //If activity AND retained fragment were lost...
        if (mSelectedCards == null) {
            mSelectedCards = new ArrayList<Card>();
        }

        mTitle = mActiveBoard.getTitle();

        if (mDrawerWasOpenedBeforeConfigChanged) {

            enableLogo();
            mDrawerWasOpenedBeforeConfigChanged = false;

        } else {

            enableTitle();
        }

        activateBoardInNavDrawer();

        if (mIsFiltered){
            filterCardsAssignedToUser();
        } else {
            openBoardSection();
        }

        invalidateOptionsMenu();
    }

    private String getDefaultLaneId() {

        if (mDefaultLaneMap != null){
            if (mDefaultLaneMap.containsKey(mActiveBoard.getId())){
                return mDefaultLaneMap.get(mActiveBoard.getId());
            }
        }

        return "";
    }

    private void activateBoardInNavDrawer() {

        for (ListBoardsBoard board : mAvailableGetBoardsBoards) {

            if (mActiveBoard.getId().equals(board.getId())) {

                board.setActiveBoard(true);

            } else {

                board.setActiveBoard(false);
            }

        }

        mDrawerListStickyAdapter.notifyDataSetChanged();

    }

    private void openBoardSection() {

        BoardSection activeSection = null;

        List<BoardSection> sections = Arrays.asList(mInFlightSection, mBacklogSection, mArchiveSection);

        for (BoardSection section : sections) {

            section.setReadyForUse(true);

            if (section.isActive()) {
                activeSection = section;
            }
        }

        if (activeSection == null) {

            activeSection = mInFlightSection;

        }

        fillLaneSpinnerFromSection(activeSection);

    }

    private void populateLanesSpinner(List<Lane> lanes) {

        mIsFiltered = false;
        mFilterTitle.setVisibility(View.GONE);
        mEmptyView.setText(getString(R.string.empty_card_list));

        mLanesHeaderSpinner.setVisibility(View.VISIBLE);
      //  mLanesHeaderSpinner.setAlpha(1f);
        mLanesHeaderSpinnerAdapter.clear();
        mLanesHeaderSpinnerAdapter.addAll(lanes);
        mLanesHeaderSpinnerAdapter.notifyDataSetChanged();

        String defaultLaneId = getDefaultLaneId();

        if (defaultLaneId != "") {
            for (int i = 0; i < lanes.size(); i++) {

                if (lanes.get(i).getId().equals(defaultLaneId)) {
                    mActiveLaneSpinnerSelection = i;
                    break;
                }
            }
        }

        mLanesHeaderSpinner.setSelection(mActiveLaneSpinnerSelection, false);

        Lane selectedLane = (Lane) mLanesHeaderSpinner.getSelectedItem();

        setVisibleCardsToAdapter( selectedLane.getCards() );

    }

    private void setVisibleCardsToAdapter(List<Card> cards) {

        mCardListAdapter.clear();
        mCardListAdapter.addAll(cards);

        if (mCardGridScrollY != 0 || mAnimateCards == false) {

            mCardGrid.setAdapter(mCardListAdapter);
            mCardListAdapter.notifyDataSetChanged();
            mCardGrid.setSelection(mCardGridScrollY);


        } else {

            mCardGrid.setAdapter(mSwingInAnimationAdapter);
            resetCardGridAdapter();
        }
    }

    private void handleBoardClick(ListBoardsBoard getBoardsBoard) {

        String boardId = getBoardsBoard.getId();

        dismissContextualActionBar();
        toggleBoardSections(false);

        mActiveLaneSpinnerSelection = 0;

        showLoadingProgress();

        mLeanKitWorker.getBoard(boardId);
    }

    private void dismissContextualActionBar() {

        if (mCAB != null) {
            mCAB.finish();
        }
    }

    private void toggleBoardSections(boolean on) {

        if (on) {

            switch (mLastActiveSection) {

                case INFLIGHT:
                    mInFlightSection.setActive(true);
                    break;

                case BACKLOG:
                    mBacklogSection.setActive(true);
                    break;

                case ARCHIVE:
                    mArchiveSection.setActive(true);
                    break;

            }

        } else {

            if (mInFlightSection.isActive()) {

                mLastActiveSection = BoardSection.BoardSectionType.INFLIGHT;
                mInFlightSection.setActive(false);
            } else if (mBacklogSection.isActive()) {

                mLastActiveSection = BoardSection.BoardSectionType.BACKLOG;
                mBacklogSection.setActive(false);
            } else if (mArchiveSection.isActive()) {

                mLastActiveSection = BoardSection.BoardSectionType.ARCHIVE;
                mArchiveSection.setActive(false);
            }

        }

        mInFlightSection.setReadyForUse(on);

        mBacklogSection.setReadyForUse(on);

        mArchiveSection.setReadyForUse(on);
    }

    private void resetBoardSections() {

        mInFlightSection.setActive(false);
        mInFlightSection.setReadyForUse(false);

        mBacklogSection.setActive(false);
        mBacklogSection.setReadyForUse(false);

        mArchiveSection.setActive(false);
        mArchiveSection.setReadyForUse(false);
    }

    private void handleBoardSectionClick(BoardSection selectedSection) {

        if (selectedSection.isActive()) {

            return;
        }

        dismissContextualActionBar();

        mActiveLaneSpinnerSelection = 0;
        mCardGridScrollY = 0;

        fillLaneSpinnerFromSection(selectedSection);


    }

    private void fillLaneSpinnerFromSection(BoardSection selectedSection) {

        if (selectedSection.getSectionType() == BoardSection.BoardSectionType.ARCHIVE && mActiveBoard.archiveNotLoaded()) {

            showLoadingProgress();
            mLeanKitWorker.getArchive(mActiveBoard.getId());

        } else {

            //reset each time to emulate singleChoice mode.
            mInFlightSection.setActive(false);
            mBacklogSection.setActive(false);
            mArchiveSection.setActive(false);

            selectedSection.setActive(true);

            mDrawerListStickyAdapter.notifyDataSetChanged();

            switch (selectedSection.getSectionType()) {

                case INFLIGHT:
                    populateLanesSpinner(mActiveBoard.getOrderedInFlightChildLanes());
                    break;

                case BACKLOG:
                    populateLanesSpinner(mActiveBoard.getOrderedBacklogChildLanes());
                    break;

                case ARCHIVE:
                    populateLanesSpinner(mActiveBoard.getOrderedArchiveChildLanes());
                    break;

            }

        }
    }

    private void useRememberedAccount() {

        for (int i = 0; i < mAvailableAccounts.size(); i++) {

            if (mAvailableAccounts.get(i).name.equals(mLastUsedAccount)) {

                selectAccount(i);
                return;

            }
        }

        selectAccount(0);
    }

    private void selectAccount(int position) {

        mInitializedAccountSpinner = true;
        mAccountsSpinner.setSelection(position, true);
    }

    private boolean useLastUsedBoard() {

        for (int i = 0; i < mAvailableGetBoardsBoards.size(); i++) {

            if (mLastUsedBoardId.equals(mAvailableGetBoardsBoards.get(i).getId())
                    && mLastUsedBoardAccount.equals(mActiveAccount.name)) {

                mDrawerList.performItemClick(mDrawerList, i, mDrawerList.getItemIdAtPosition(i));

                return true;
            }

        }

        return false;

    }

    private void resetCardGridAdapter() {


        if (mAnimateCards) {

            mSwingInAnimationAdapter.reset();
            mSwingInAnimationAdapter.notifyDataSetChanged();
        } else {

            mCardListAdapter.notifyDataSetChanged();
        }

    }

    private void clearUI() {

        mCardListAdapter.clear();
        resetCardGridAdapter();

        mCardGridScrollY = 0;

        mLanesHeaderSpinnerAdapter.clear();
        mLanesHeaderSpinnerAdapter.notifyDataSetChanged();
        mLanesHeaderSpinner.setVisibility(View.INVISIBLE);

        mDrawerList.setItemChecked(mDrawerList.getCheckedItemPosition(), false);

        mActiveBoard = null;
        mAvailableGetBoardsBoards = null;
        mTitle = getTitle();
        invalidateOptionsMenu();
        enableLogo();
        mFilterTitle.setVisibility(View.GONE);
        TextView emptyView = (TextView) findViewById(R.id.emptyText);
        emptyView.setText(getString(R.string.no_board_selected));

        resetBoardSections();

        mDrawerListStickyAdapter.notifyDataSetChanged();

    }

    private void enableLogo() {
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
    }

    private void showCrouton(String message, CROUTON_TYPE type) {

        int color;

        switch (type) {

            case CONFIRM:
                color = Style.holoGreenLight;
                break;
            case ALERT:
                color = Style.holoRedLight;
                break;
            case INFO:
                color = Style.holoBlueLight;
                break;

            default:
                color = Style.holoBlueLight;

        }

        mErrorCroutonLayout.setBackgroundColor(color);
        mErrorCroutonText.setText(message);


        Crouton c = Crouton.make(this, mErrorCroutonLayout);

        if (type == CROUTON_TYPE.ALERT) {
            c.setLifecycleCallback(MyErrorCallback);
        } else {
            c.setLifecycleCallback(null);
        }

        c.show();

    }

    private LifecycleCallback MyErrorCallback = new LifecycleCallback() {
        @Override
        public void onDisplayed() {

        }

        @Override
        public void onRemoved() {
            hideLoadingProgress();
        }
    };

    private void showLoadingProgress() {

        if (mProgressLoading.getVisibility() != View.VISIBLE) {

            mProgressLoading.setVisibility(View.VISIBLE);
            mLanesHeaderSpinner.setVisibility(View.INVISIBLE);


            mProgressLoadingDisplayed = true;

            mAccountsSpinner.setEnabled(false);

        }
    }

    private void hideLoadingProgress() {


        mLanesHeaderSpinner.setVisibility(View.VISIBLE);
        mProgressLoading.setVisibility(View.GONE);


        mProgressLoadingDisplayed = false;

        mAccountsSpinner.setEnabled(true);


    }

    private void getBoardsForAccount() {

        mGetBoardsProgressBar.setVisibility(View.VISIBLE);

        prepareRetrofit();

        //call worker thread getBoards.
        mLeanKitWorker.getBoards();

    }

    private void beginNewCard() {

        if (mActiveBoard == null) {

            showCrouton(getResources().getString(R.string.must_have_board), CROUTON_TYPE.ALERT);

        } else {

            Intent newCardIntent = new Intent(getContext(), NewCardActivity.class);

            newCardIntent.putExtra(Consts.BOARD_ID_EXTRA, mActiveBoard.getId());
            newCardIntent.putParcelableArrayListExtra(Consts.ALL_CHILD_LANES_EXTRA, mActiveBoard.getAllChildLaneNames());
            newCardIntent.putExtra(Consts.BOARD_SETTINGS_EXTRA, mActiveBoard.getSettings());

            startActivityForResult(newCardIntent, Consts.REQUEST_CODE_NEW_CARD);

        }
    }

    private void deleteCards() {


        List<String> ids = new ArrayList<String>();

        for (Card card : mSelectedCards) {

            ids.add(card.getId());
        }

        if (ids.size() == 1) {

            deleteCard(ids.get(0));
            return;
        }

        showProgressDialog(false);

        mLeanKitWorker.deleteCards(ids, mActiveBoard.getId());

    }

    private void deleteCard(String id) {

        showProgressDialog(true);

        mLeanKitWorker.deleteCard(id, mActiveBoard.getId());

    }

    private void showProgressDialog(boolean isSingleCard) {

        pd = new ProgressDialog(this);

        String message;

        if (isSingleCard) {

            message = getString(R.string.deleting_card);

        } else {

            message = getString(R.string.deleting_cards);

        }


        pd.setMessage(message);
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        pd.show();

    }

    private void dismissProgressDialog() {

        if (pd != null) {

            pd.dismiss();
        }
    }

    private void startCardDetailActivity(Card selectedCard) {

        Intent cardDetailIntent = new Intent(this, CardDetailActivity.class);

        cardDetailIntent.putExtra(Consts.CARD_DETAIL_CARD_EXTRA, selectedCard);
        cardDetailIntent.putExtra(Consts.BOARD_ID_EXTRA, mActiveBoard.getId());
        cardDetailIntent.putParcelableArrayListExtra(Consts.ALL_CHILD_LANES_EXTRA, mActiveBoard.getAllChildLaneNames());
        cardDetailIntent.putExtra(Consts.BOARD_SETTINGS_EXTRA, mActiveBoard.getSettings());

        startActivityForResult(cardDetailIntent, Consts.REQUEST_CODE_CARD_DETAIL);
    }

    public void startEditActivity() {

        Intent editCardIntent = new Intent(this, NewCardActivity.class);

        editCardIntent.putExtra(Consts.BOARD_ID_EXTRA, mActiveBoard.getId());
        editCardIntent.putExtra(Consts.EXISTING_CARD_EXTRA, mSelectedCards.get(0));
        editCardIntent.putParcelableArrayListExtra(Consts.ALL_CHILD_LANES_EXTRA, mActiveBoard.getAllChildLaneNames());
        editCardIntent.putExtra(Consts.BOARD_SETTINGS_EXTRA, mActiveBoard.getSettings());

        startActivityForResult(editCardIntent, Consts.REQUEST_CODE_EDIT_EXISTING);
    }

    private void showDeleteCardsDialog(List<Card> cardsToBeDeleted) {

        if (cardsToBeDeleted != null) {

            ConfirmDeleteCardDialog confirmDeleteDialog = new ConfirmDeleteCardDialog(cardsToBeDeleted, mActiveBoard.getId(), false);
            confirmDeleteDialog.show(getFragmentManager(), "ConfirmDeleteCardDialog");
        }
    }

    private void showMoveCardDialog() {

        MoveCardDialog moveCardDialog = MoveCardDialog.newInstance(mActiveBoard.getId(),
                mSelectedCards.get(0),
                mActiveBoard.getAllChildLaneNames());

        moveCardDialog.show(getFragmentManager(), "MoveCardDialog");
    }

    private class OnAccountSelectedTokenAcquired implements AccountManagerCallback<Bundle> {

        @Override
        public void run(AccountManagerFuture<Bundle> result) {

            Intent launch = null;

            try {

                launch = (Intent) result.getResult().get(AccountManager.KEY_INTENT);
                if (launch != null) {

                    //This logic happens when a user's password was changed elsewhere.
                    launch.putExtra(Consts.LEANKIT_ACCOUNT_TO_VERIFY,
                            mAccountManager.getUserData(mActiveAccount, Consts.LEANKIT_USERDATA_EMAIL));

                    startActivityForResult(launch, Consts.REQUEST_CODE_ACCOUNT_SELECTED);

                } else {

                    mActiveAccountAuthToken = result.getResult().getString(AccountManager.KEY_AUTHTOKEN);


                    getBoardsForAccount();

                }

            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            }

        }
    }

    private class OnAddAccountTokenAcquired implements AccountManagerCallback<Bundle> {


        @Override
        public void run(AccountManagerFuture<Bundle> result) {

            Intent launch = null;

            try {

                launch = (Intent) result.getResult().get(AccountManager.KEY_INTENT);
                if (launch != null) {

                    startActivityForResult(launch, Consts.REQUEST_CODE_ADD_ACCOUNT);

                }

            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



            switch (requestCode) {

                case Consts.REQUEST_CODE_ADD_ACCOUNT:

                    handleAddAccountResult(resultCode);

                    break;

                case Consts.REQUEST_CODE_ACCOUNT_SELECTED:

                    handleAccountSelectedResult(resultCode);

                    break;

                case Consts.REQUEST_CODE_NEW_CARD:

                    handleNewCardResult(resultCode);

                    break;

                case Consts.REQUEST_CODE_CARD_DETAIL:

                    handleCardDetailResult(resultCode, data);

                    break;

                case Consts.REQUEST_CODE_EDIT_EXISTING:

                    if (resultCode == Consts.RESULT_CODE_CARD_UPDATE_SUCCESS) {

                        showCrouton(getString(R.string.update_successful), CROUTON_TYPE.CONFIRM);
                        refreshBoard();
                    }

                    break;




        }

    }

    private void handleCardDetailResult(int resultCode, Intent data) {

        switch (resultCode) {

            case Consts.RESULT_CODE_CARD_DELETE_ATTEMPTED:

                handleDeleteAttempt(data);
                break;

            case Consts.RESULT_CODE_CARD_UPDATE_SUCCESS:

                refreshBoard();
                showCrouton(getString(R.string.update_successful), CROUTON_TYPE.CONFIRM);
                break;

        }

    }

    private void handleDeleteAttempt(Intent data) {

        String cardId = data.getStringExtra(Consts.BUNDLE_CARD_ID);

        deleteCard(cardId);

    }

    private void handleNewCardResult(int resultCode) {
        if (resultCode == RESULT_OK) {

            showCrouton(getString(R.string.new_card_created_success), CROUTON_TYPE.CONFIRM);
            refreshBoard();


        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    private void handleAccountSelectedResult(int resultCode) {
        if (resultCode == RESULT_OK) {

            mAccountManager.getAuthToken(mActiveAccount, Consts.LEANKIT_AUTH_TOKEN_TYPE, null,
                    null, new OnAccountSelectedTokenAcquired(), null);

        } else if (resultCode == RESULT_CANCELED) {


        }
    }

    private void handleAddAccountResult(int resultCode) {


        if (resultCode == RESULT_OK) {

            if (mAvailableAccounts != null && mAvailableAccounts.size() > 0) {

                selectAccount(0);
            }


        } else if (resultCode == RESULT_CANCELED) {

            //Can't use the app without an account...
            if (mAvailableAccounts.size() == 0) {

                finish();
            }


        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerView);

        boolean isMultiColumnView = mCardGrid.getNumColumns() == getResources().getInteger(R.integer.num_columns_multi);

        menu.findItem(R.id.action_multi_column_view).setVisible(!isMultiColumnView && !drawerOpen && mActiveBoard!=null);
        menu.findItem(R.id.action_single_column_view).setVisible(isMultiColumnView && !drawerOpen && mActiveBoard!=null);

        menu.findItem(R.id.action_filter_default).setVisible(mIsFiltered && !drawerOpen && mActiveBoard!=null);
        menu.findItem(R.id.action_filter_assigned_to_me).setVisible( !mIsFiltered && !drawerOpen && mActiveBoard!=null && !(mBacklogSection.isActive() || mArchiveSection.isActive())  );

        MenuItem setStartingLaneMenuItem = menu.findItem(R.id.action_set_starting_lane);

        if (!mIsFiltered && !drawerOpen && mActiveBoard!=null && !(mBacklogSection.isActive() || mArchiveSection.isActive())){

            setStartingLaneMenuItem.setVisible(true);

            Lane selectedLane = (Lane) mLanesHeaderSpinner.getSelectedItem();
            SetDefaultLaneMenuItemTitle(selectedLane);

        } else {
           setStartingLaneMenuItem.setVisible(false);
        }

        menu.findItem(R.id.action_new_card).setVisible(!drawerOpen && mActiveBoard!=null);
        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen && mActiveBoard!=null);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        switch (item.getItemId()) {

            case R.id.action_new_card:
                beginNewCard();
                return true;

            case R.id.action_set_starting_lane:
                setOrClearActiveLaneAsStartingLane();
                return true;

            case R.id.action_single_column_view:
                setNumColumns(1);
                return true;

            case R.id.action_multi_column_view:
                setNumColumns(getResources().getInteger(R.integer.num_columns_multi));
                return true;

            case R.id.action_filter_assigned_to_me:
                filterCardsAssignedToUser();
                return true;

            case R.id.action_filter_default:
                openBoardSection();
                return true;

            case R.id.action_refresh:
                refreshBoard();
                return true;

            case R.id.action_settings:
                startSettingsActivity();
                return true;

            case R.id.action_send_feedback:
                sendEmailIntentForFeedback();
                return true;

            case R.id.action_help:
                openHelpWebPageIntent();
                return true;


        }

        return false;
    }

    private void filterCardsAssignedToUser(){

        mIsFiltered = true;

        setVisibleCardsToAdapter( mActiveBoard.getCardsAssignedToAppUser() );

        mFilterTitle.setVisibility(View.VISIBLE);
        mEmptyView.setText(getString(R.string.no_assigned_cards));

    }

    private void setOrClearActiveLaneAsStartingLane(){

        Lane activeLane = (Lane) mLanesHeaderSpinner.getItemAtPosition(mActiveLaneSpinnerSelection);

        if (activeLane != null){

            String key = mActiveBoard.getId();
            String value = activeLane.getId();

            if (mDefaultLaneMap.containsKey(key) && mDefaultLaneMap.get(key).equals(value)){
                mDefaultLaneMap.put(key, "");
            } else {
                mDefaultLaneMap.put(mActiveBoard.getId(), activeLane.getId());
            }

            String serializedDefaultLaneMap = gson.toJson(mDefaultLaneMap);
            mSharedPreferences.edit().putString(Consts.SHARED_PREFS_DEFAULT_LANE_MAP, serializedDefaultLaneMap).apply();

            invalidateOptionsMenu();
        }
    }


    private void openHelpWebPageIntent() {

        final Intent helpIntent = new Intent(Intent.ACTION_VIEW);

        helpIntent.setData(Uri.parse(Consts.HELP_PAGE_URL));

        startActivity(helpIntent);
    }

    private void sendEmailIntentForFeedback() {

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.mail_feedback_email)});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));

        startActivity(Intent.createChooser(emailIntent, getString(R.string.title_send_feedback)));

    }

    private void startSettingsActivity() {

        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

    }

    private void setNumColumns(int n) {

        mCardGrid.setNumColumns(n);

        mSharedPreferences.edit().putBoolean(Consts.SHARED_PREFS_SINGLE_COL_MODE, n == 1).apply();

    }

    @Override
    protected void onPause() {
        super.onPause();

        mLeanKitWorker.setLastRetrievedBoards(mAvailableGetBoardsBoards);
        mLeanKitWorker.setLastActiveAccount(mActiveAccount);
        mLeanKitWorker.setLastActiveAccountAuthToken(mActiveAccountAuthToken);
        mLeanKitWorker.setLastActiveBoard(mActiveBoard);
        mLeanKitWorker.setLastActiveLane(mActiveLaneSpinnerSelection);
        mLeanKitWorker.setInFlightSection(mInFlightSection);
        mLeanKitWorker.setBacklogSection(mBacklogSection);
        mLeanKitWorker.setArchiveSection(mArchiveSection);
        mLeanKitWorker.setRetryCroutonDisplayed(mRetryCroutonDisplayed);
        mLeanKitWorker.setCardGridScrollY(mCardGrid.getFirstVisiblePosition());
        mLeanKitWorker.setDrawerProgressBarDisplayed(mGetBoardsProgressBar.isShown());
        mLeanKitWorker.setLoadingProgressBarDisplayed(mProgressLoadingDisplayed);
        mLeanKitWorker.setSelectedCards(mSelectedCards);
        mLeanKitWorker.setDrawerOpened(mDrawerLayout.isDrawerOpen(GravityCompat.START));
        mLeanKitWorker.setIsFiltered(mIsFiltered);
    }

    @Override
    protected void onStop() {
        super.onStop();

        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mAccountManager.removeOnAccountsUpdatedListener(this);
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        Crouton.cancelAllCroutons();

    }

    private Context getContext() {
        return this;
    }

    @Override
    public void onBoardsRetrieved(List<ListBoardsBoard> boards) {

        mAvailableGetBoardsBoards = new ArrayList<ListBoardsBoard>();

        if (!mShowArchivedBoards){

            for (ListBoardsBoard board : boards){

                if ( !board.isArchived() ){
                    mAvailableGetBoardsBoards.add(board);
                }
            }

        } else {

            mAvailableGetBoardsBoards = boards;
        }

        mDrawerListStickyAdapter.clear();
        mDrawerListStickyAdapter.addAll(mAvailableGetBoardsBoards);
        mDrawerListStickyAdapter.add(mInFlightSection);
        mDrawerListStickyAdapter.add(mBacklogSection);
        mDrawerListStickyAdapter.add(mArchiveSection);

        mDrawerListStickyAdapter.notifyDataSetChanged();

        mGetBoardsProgressBar.setVisibility(View.GONE);

        if (mIsFirstGetBoardsCall) {

            if (mAutoLoadLastBoard == false || useLastUsedBoard() == false) {

                //User needs to select a board.
                mDrawerLayout.openDrawer(GravityCompat.START);
                mIsFirstGetBoardsCall = false;

            }

        }

        mSharedPreferences.edit().putString(Consts.SHARED_PREFS_LAST_USED_ACCOUNT, mActiveAccount.name).apply();


    }

    @Override
    public void onGetBoardsRetrofitError(RetrofitError error) {

        mGetBoardsProgressBar.setVisibility(View.GONE);

        if (error.getResponse() == null) {

            mDrawerLayout.openDrawer(GravityCompat.START);
            mRetryCroutonDisplayed = true;
            mRetryCrouton.show();

        } else {

            handleRetrofitError(error);
        }

    }

    @Override
    public void onGetBoardsLeanKitException(int replyCode, String replyText) {

        mGetBoardsProgressBar.setVisibility(View.GONE);

        showCrouton(replyText, CROUTON_TYPE.ALERT);

    }

    @Override
    public void onBoardReadyForUse(Board board) {

        populateUI(board);
        hideLoadingProgress();
        mSharedPreferences.edit().putString(Consts.SHARED_PREFS_LAST_USED_BOARD_ID, board.getId()).apply();
        mSharedPreferences.edit().putString(Consts.SHARED_PREFS_LAST_USED_BOARD_ACCOUNT, mActiveAccount.name).apply();

    }

    @Override
    public void onGetBoardRetrofitError(RetrofitError error) {

        hideLoadingProgress();

        if (mActiveBoard != null && mLastActiveSection != null) {

            toggleBoardSections(true);
            mActiveLaneSpinnerSelection = mLanesHeaderSpinner.getSelectedItemPosition();
        }

        handleRetrofitError(error);

    }

    @Override
    public void onGetBoardLeanKitException(int replyCode, String replyText) {

        hideLoadingProgress();

        showCrouton(replyText, CROUTON_TYPE.ALERT);

    }

    @Override
    public void onArchiveReadyForUse(List<Lane> archive) {

        hideLoadingProgress();

        mActiveBoard.setOrderedArchiveChildLanes(archive);
        mActiveBoard.setHasLoadedArchive(true);

        //reset each time to emulate singleChoice mode.
        mInFlightSection.setActive(false);
        mBacklogSection.setActive(false);
        mArchiveSection.setActive(true);

        mDrawerListStickyAdapter.notifyDataSetChanged();

        populateLanesSpinner(archive);

    }

    @Override
    public void onGetArchiveRetrofitError(RetrofitError error) {

        hideLoadingProgress();
        handleRetrofitError(error, getString(R.string.no_network_signal_archive));

    }

    @Override
    public void onGetArchiveLeanKitException(int replyCode, String replyText) {
        hideLoadingProgress();
        showCrouton(replyText, CROUTON_TYPE.ALERT);

    }

    @Override
    public void onDeleteSuccess(String replyText) {

        dismissProgressDialog();
        showCrouton(replyText, CROUTON_TYPE.CONFIRM);
        refreshBoard();
    }

    @Override
    public void onDeleteLeanKitException(int replyCode, String replyText) {

        dismissProgressDialog();
        showCrouton(replyText, CROUTON_TYPE.ALERT);

    }

    @Override
    public void onDeleteRetrofitError(RetrofitError error) {

        dismissProgressDialog();
        handleRetrofitError(error, getString(R.string.no_network_signal_delete_card));

    }

    @Override
    public void onDeleteDialogConfirm() {

        deleteCards();
        dismissContextualActionBar(); //rememeber the mSelected card stuff
    }

    @Override
    public void onDeleteDialogCancel() {

    }

    @Override
    public void onMoveCardDialogMoveSuccess(String message) {

        showCrouton(message, CROUTON_TYPE.CONFIRM);

        refreshBoard();
//        mCardListAdapter.remove(cardToMove);
//        destLane.getCards().add(cardToMove);
//        cardToMove.setLaneId( destLane.getId() );
//
//        resetCardGridAdapter();
    }

    @Override
    public void onMoveCardDialogLeanKitException(int replyCode, String replyText) {
        showCrouton(replyText, CROUTON_TYPE.ALERT);
    }

    @Override
    public void onMoveCardDialogRetrofitError(RetrofitError retrofitError) {

        handleRetrofitError(retrofitError, getString(R.string.no_network_signal_move_card));
    }

    @Override
    public void onMoveCardDialogCancel() {

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Consts.SHARED_PREFS_ANIMATE_CARDS)) {

            mAnimateCards = sharedPreferences.getBoolean(Consts.SHARED_PREFS_ANIMATE_CARDS, true);

            if (mAnimateCards) {

                mCardGrid.setAdapter(mSwingInAnimationAdapter);
            } else {

                mCardGrid.setAdapter(mCardListAdapter);

            }
        } else if (key.equals(Consts.SHARED_PREFS_SHOW_ARCHIVED_BOARDS)){

            mShowArchivedBoards = sharedPreferences.getBoolean(Consts.SHARED_PREFS_SHOW_ARCHIVED_BOARDS, false);
        }

    }

    private void handleRetrofitError(RetrofitError error) {

        handleRetrofitError(error, getResources().getString(R.string.no_network_signal));
    }

    private void handleRetrofitError(RetrofitError error, String msg) {

        if (error.getResponse() == null) {

            showCrouton(msg, CROUTON_TYPE.ALERT);

        } else {

            switch (error.getResponse().getStatus()) {

                case HttpStatus.SC_UNAUTHORIZED:

                    mAccountManager.invalidateAuthToken(Consts.LEANKIT_ACCOUNT_TYPE, mActiveAccountAuthToken);
                    mSharedPreferences.edit().remove(mAccountManager.getPassword(mActiveAccount)).commit();
                    mAccountManager.getAuthToken(mActiveAccount, Consts.LEANKIT_AUTH_TOKEN_TYPE,
                            null, null, new OnAccountSelectedTokenAcquired(), null);
                    break;
            }
        }
    }


    private void prepareRetrofit() {

        String hostName = mAccountManager.getUserData(mActiveAccount, Consts.LEANKIT_USERDATA_ORG_HOST);
        mUserName = mAccountManager.getUserData(mActiveAccount, Consts.LEANKIT_USERDATA_EMAIL);
        String pwd = mActiveAccountAuthToken;

        mSharedPreferences.edit().putString(Consts.SHARED_PREFS_HOST_NAME, hostName).apply();
        mSharedPreferences.edit().putString(Consts.SHARED_PREFS_USER_NAME, mUserName).apply();
        mSharedPreferences.edit().putString(Consts.SHARED_PREFS_PWD, pwd).apply();

        ((MyApplication) getApplication()).initRetroLeanKitApi(hostName, mUserName, pwd);

    }

    private static final de.keyboardsurfer.android.widget.crouton.Configuration CONFIGURATION_INFINITE = new de.keyboardsurfer.android.widget.crouton.Configuration.Builder()
            .setDuration(de.keyboardsurfer.android.widget.crouton.Configuration.DURATION_INFINITE)
            .build();




}
