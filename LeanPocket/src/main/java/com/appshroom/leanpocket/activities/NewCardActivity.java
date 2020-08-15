package com.appshroom.leanpocket.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApiV2;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApiV2Callback;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitCallback;
import com.appshroom.leanpocket.dto.AddCardReplyData;
import com.appshroom.leanpocket.dto.BoardSettings;
import com.appshroom.leanpocket.dto.BoardUser;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.CardFieldData;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LaneDescription;
import com.appshroom.leanpocket.dto.UpdateCardReplyData;
import com.appshroom.leanpocket.dto.v2.CreateCardRequest;
import com.appshroom.leanpocket.dto.v2.CreateCardResponse;
import com.appshroom.leanpocket.fragments.NewCardBasicFragment;
import com.appshroom.leanpocket.helpers.Consts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.RetrofitError;

public class NewCardActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    RetroLeanKitApi mRetroLeanKitApi;
    RetroLeanKitApiV2 mRetroLeanKitApiV2;
    String mBoardId;
    ArrayList<LaneDescription> mLanes;
    Card mExistingCard;
    BoardSettings mBoardSettings;

    ProgressDialog pd;
    int mPosition;


    public enum MODE {NEW_CARD, EDIT_EXISTING}

    private MODE mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the Up button in the action bar.
        setupActionBar();

        MyApplication app = (MyApplication) getApplication();
        mRetroLeanKitApi = app.getRetroLeanKitApiInstance();
        mRetroLeanKitApiV2 = app.getRetroLeanKitApiV2Instance();

        Intent srcIntent = getIntent();

        mBoardId = srcIntent.getStringExtra(Consts.BOARD_ID_EXTRA);
        mLanes = srcIntent.getParcelableArrayListExtra(Consts.ALL_CHILD_LANES_EXTRA);
        mBoardSettings = srcIntent.getParcelableExtra(Consts.BOARD_SETTINGS_EXTRA);

        mExistingCard = srcIntent.getParcelableExtra(Consts.EXISTING_CARD_EXTRA);
        mMode = mExistingCard == null ? MODE.NEW_CARD : MODE.EDIT_EXISTING;

        setPosition();

        setContentView(R.layout.activity_new_card);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(Consts.SHARED_PREFS_CARD_POSITION)) {

            setPosition();
        }
    }

    private void setPosition() {
        String position = PreferenceManager.getDefaultSharedPreferences(this).getString(Consts.SHARED_PREFS_CARD_POSITION, getString(R.string.top));

        if (position.equals(getString(R.string.top))) {

            mPosition = 0;
        } else {
            mPosition = 999999999;
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        // Inflate a "Done/Discard" custom action bar view.
        LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(
                R.layout.actionbar_custom_view_save_cancel, null);
        customActionBarView.findViewById(R.id.actionbar_save).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "Save"
                        saveCard();
                    }
                });
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // "cancel"
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });

        // Show the custom action bar view and hide the normal Home icon and title.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(
                ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
                        | ActionBar.DISPLAY_SHOW_TITLE );
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

    }

    public Card getExistingCard() {

        return mExistingCard;
    }

    public BoardSettings getBoardSettings(){
        return mBoardSettings;
    }


    public List<LaneDescription> getLanes() {
        return mLanes;
    }

    public MODE getMode() {
        return mMode;
    }

    private Activity getActivity() {

        return this;
    }

    private void saveCard() {

        if (validateCard()) {

            if (mMode == MODE.NEW_CARD) {

                CreateCardRequest request = buildCreateCardRequest();

                showProgressDialog();

                mRetroLeanKitApiV2.createCard(request, new RetroLeanKitApiV2Callback<CreateCardResponse>() {
                    @Override
                    public void onSuccess(int replyCode, String replyText, List<CreateCardResponse> replyData) {
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onLeanKitException(int replyCode, String replyText, List<CreateCardResponse> replyData) {
                        Crouton.makeText(getActivity(), replyText, Style.ALERT).show();
                        dismissProgressDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Crouton.makeText(getActivity(), getResources().getString(R.string.cant_create_card), Style.ALERT).show();
                        dismissProgressDialog();
                    }
                });

            } else {

                mExistingCard = retrieveAllCardSettings(mExistingCard);

                showProgressDialog();

                mRetroLeanKitApi.updateCard(mExistingCard, mBoardId, new RetroLeanKitCallback<UpdateCardReplyData>() {
                    @Override
                    public void onSuccess(int replyCode, String replyText, List<UpdateCardReplyData> replyData) {

                        setResult(Consts.RESULT_CODE_CARD_UPDATE_SUCCESS);
                        finish();
                    }

                    @Override
                    public void onLeanKitException(int replyCode, String replyText, List<UpdateCardReplyData> replyData) {
                        Crouton.makeText(getActivity(), replyText, Style.ALERT).show();
                        dismissProgressDialog();

                        //TODO: filter the reply codes so that I only show replyText to user of things that matter
                        //like telling them to contact admin, not leankit parsing errors
                    }

                    @Override
                    public void onWIPOverrideCommentRequired() {

                        Crouton.makeText(getActivity(), getString(R.string.wip_not_supported), Style.ALERT).show();
                        dismissProgressDialog();

                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                        Crouton.makeText(getActivity(), getResources().getString(R.string.cant_update_card), Style.ALERT).show();
                        dismissProgressDialog();


                    }
                });

            }

        }

    }

    private boolean validateCard() {

        NewCardBasicFragment basicSettingsFragment = (NewCardBasicFragment) getFragmentManager().findFragmentById(R.id.new_card_fragment);

        return basicSettingsFragment.validateCard();

    }

    private Card retrieveAllCardSettings(Card card) {
        //grab data from all fragments and populate in card.

        Card cardToFill = card.shallowCopy();

        NewCardBasicFragment newOrEditCardFragment = (NewCardBasicFragment) getFragmentManager().findFragmentById(R.id.new_card_fragment);
        CardFieldData basicSettings = newOrEditCardFragment.getFieldData();

        cardToFill.setTitle(basicSettings.getTitle());

        cardToFill.setDescription(basicSettings.getDescription());

        cardToFill.setPriority(basicSettings.getPriority());

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

        try {

            java.util.Date d = format.parse(basicSettings.getDueDate());

            format.applyPattern( mBoardSettings.getDateFormat() );

            cardToFill.setDueDate(format.format(d));

        } catch (ParseException ex) {

            //TODO: what to do?
        }


        cardToFill.setTypeId(basicSettings.getCardTypeId());

        cardToFill.setClassOfServiceId(basicSettings.getClassOfServiceId());

        cardToFill.setSize(basicSettings.getSize());

        cardToFill.setTags(basicSettings.getTagsAsList());

        cardToFill.setExternalCardID( basicSettings.getExternalCardId() );

        List<String> assignedUserIds = new ArrayList<String>();

        for (BoardUser assignedUser : basicSettings.getAssignedUsers()) {

            assignedUserIds.add(assignedUser.getId());

        }

        //clear existing card's users.
        cardToFill.setAssignedUsers(null);
        cardToFill.setAssignedUserId(null);
        cardToFill.setAssignedUserName(null);

        cardToFill.setAssignedUserIds(assignedUserIds);

        cardToFill.setBlocked(basicSettings.isBlocked());

        if (cardToFill.isBlocked()) {

            cardToFill.setBlockReason(basicSettings.getBlockedReason());
        }

        return cardToFill;
    }

    private CreateCardRequest buildCreateCardRequest() {
        NewCardBasicFragment newOrEditCardFragment = (NewCardBasicFragment) getFragmentManager().findFragmentById(R.id.new_card_fragment);
        CardFieldData basicSettings = newOrEditCardFragment.getFieldData();

        CreateCardRequest request = new CreateCardRequest();
        request.boardId = mBoardId;
        request.title = basicSettings.getTitle();
        request.typeId = basicSettings.getCardTypeId();
        request.assignedUserIds = new ArrayList<String>();

        for (BoardUser assignedUser : basicSettings.getAssignedUsers()) {
            request.assignedUserIds.add(assignedUser.getId());
        }
        request.description = basicSettings.getDescription();
        request.size = basicSettings.getSize();
        request.laneId = basicSettings.getLaneId();

        if (basicSettings.isBlocked()) {
            request.blockReason = basicSettings.getBlockedReason();
        }

        request.priority = basicSettings.getPriority();
        request.customId = basicSettings.getExternalCardId(); //is this supposed to be external link label instead?
        request.index = mPosition;

        SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date d = dateParser.parse(basicSettings.getDueDate());
            request.plannedFinish = dateFormatter.format(d);

        } catch (ParseException ex) {
            //for now just fail silently...
            String h = ex.getMessage();
        }

        request.tags = basicSettings.getTagsAsList();

        return request;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);

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

            case R.id.action_settings:
                startSettingsActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSettingsActivity() {

        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }


    private void showProgressDialog() {

        pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(R.string.saving_card));
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        pd.show();

    }

    private void dismissProgressDialog() {

        if (pd != null) {

            pd.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        dismissProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Crouton.clearCroutonsForActivity(this);
    }
}
