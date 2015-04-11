package com.appshroom.leanpocket.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.dto.BoardSettings;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.fragments.CommentsFragment;
import com.appshroom.leanpocket.fragments.ConfirmDeleteCardDialog;
import com.appshroom.leanpocket.fragments.DetailsFragment;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.SecurePreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CardDetailActivity extends FragmentActivity
        implements ActionBar.TabListener, ConfirmDeleteCardDialog.OnDeleteCardDialogChoiceListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    Card mCard;
    String mBoardId;
    BoardSettings mBoardSettings;

    List<Lane> mLanes;

    ConfirmDeleteCardDialog mDeleteDialog;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        mSharedPreferences = new SecurePreferences(this);


        Intent intent = getIntent();

        mCard = intent.getParcelableExtra(Consts.CARD_DETAIL_CARD_EXTRA);
        mBoardId = intent.getStringExtra(Consts.BOARD_ID_EXTRA);
        mBoardSettings = intent.getParcelableExtra(Consts.BOARD_SETTINGS_EXTRA);
        mLanes = intent.getParcelableArrayListExtra(Consts.ALL_CHILD_LANES_EXTRA);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // Show the Up button in the action bar.
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }


        android.app.FragmentManager fm = getFragmentManager();
        mDeleteDialog = (ConfirmDeleteCardDialog) fm.findFragmentByTag("ConfirmDeleteCardDialog");

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.

        if (mDeleteDialog == null) {


        } else {


            fm.beginTransaction().remove(mDeleteDialog).commit();
            showDeleteCardsDialog(Arrays.asList(mCard));

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_detail, menu);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_send_feedback:
                sendEmailIntentForFeedback();
                return true;

            case R.id.action_settings:
                startSettingsActivity();
                return true;

            case R.id.action_help:

                openHelpWebPageIntent();
                return true;

        }
        return super.onOptionsItemSelected(item);
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
    }

    public void startEditActivity() {

        Intent editCardIntent = new Intent(this, NewCardActivity.class);

        editCardIntent.putExtra(Consts.BOARD_ID_EXTRA, mBoardId);
        editCardIntent.putExtra(Consts.EXISTING_CARD_EXTRA, mCard);
        editCardIntent.putExtra(Consts.BOARD_SETTINGS_EXTRA, mBoardSettings);
        editCardIntent.putParcelableArrayListExtra(Consts.ALL_CHILD_LANES_EXTRA, new ArrayList<Lane>(mLanes));

        startActivityForResult(editCardIntent, Consts.REQUEST_CODE_EDIT_EXISTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


            switch (requestCode) {

                case Consts.REQUEST_CODE_EDIT_EXISTING:

                    if (resultCode == Consts.RESULT_CODE_CARD_UPDATE_SUCCESS) {

                        setResult(Consts.RESULT_CODE_CARD_UPDATE_SUCCESS);
                        finish();
                    }

                    break;


            }
    }


    public void showDeleteCardsDialog(List<Card> cardsToBeDeleted) {

        ConfirmDeleteCardDialog confirmDeleteDialog = new ConfirmDeleteCardDialog(cardsToBeDeleted, mBoardId, true);

        confirmDeleteDialog.show(getFragmentManager(), "ConfirmDeleteCardDialog");
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }


    @Override
    public void onDeleteDialogConfirm() {

        Intent deleteResultIntent = new Intent();
        deleteResultIntent.putExtra(Consts.BUNDLE_CARD_ID, mCard.getId());
        setResult(Consts.RESULT_CODE_CARD_DELETE_ATTEMPTED, deleteResultIntent);
        finish();


    }

    @Override
    public void onDeleteDialogCancel() {


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.

            switch (position) {

                case 0:
                    return DetailsFragment.newInstance(mCard, mBoardSettings);

                case 1:
                    return CommentsFragment.newInstance(mCard, mBoardId);

                //  case 2:
                //    Fragment historyFragment = CardHistoryFragment.newInstance(mCard, mBoardId);
                //   return historyFragment;

            }

            return null;

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_details).toUpperCase(l);
                case 1:
                    return getString(R.string.title_comments).toUpperCase(l);
                // case 2:
                //   return getString(R.string.title_card_history).toUpperCase(l);
            }
            return null;
        }
    }


}
