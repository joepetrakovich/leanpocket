package com.appshroom.leanpocket.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.activities.NewCardActivity;
import com.appshroom.leanpocket.adapters.AssignUserCheckableListAdapter;
import com.appshroom.leanpocket.adapters.LanesSpinnerAdapter;
import com.appshroom.leanpocket.dto.AssignedUser;
import com.appshroom.leanpocket.dto.BoardSettings;
import com.appshroom.leanpocket.dto.BoardUser;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.CardFieldData;
import com.appshroom.leanpocket.dto.CardType;
import com.appshroom.leanpocket.dto.ClassOfService;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LaneDescription;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.GravatarHelpers;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jpetrakovich on 8/24/13.
 */
public class NewCardBasicFragment extends Fragment {

    View titleFrame;
    EditText etTitle;
    EditText etDescription;
    EditText etSize;
    EditText etTags;
    EditText etBlockReason;
    EditText etExternalCardId;
    TextView mDueDateView;
    View classOfServiceFrame;
    View externalCardIdFrame;

    TextView mLaneSectionHeader;

    ToggleButton toggleBlocked;

    Spinner mSpinnerCardTypes;
    Spinner mSpinnerClassOfServices;
    Spinner mSpinnerPriorities;
    Spinner mSpinnerLanes;

    ArrayAdapter<CardType> mCardTypeAdapter;
    ArrayAdapter<ClassOfService> mClassOfServiceAdapter;
    ArrayAdapter<String> mPrioritiesAdapter;
    ArrayAdapter<LaneDescription> mLanesAdapter;

    DatePickerFragment datePickerFragment;
    AssignUsersDialogFragment assignUserDialog;

    List<LaneDescription> mLanes;

    List<BoardUser> mAssignedUsers;
    BoardSettings mBoardSettings;

    LinearLayout mAssignedUsersHolder;
    LinearLayout mLayoutAfterTitleWrapper;

    private static final String datePickerTag = "datePicker";
    private static final String dueDateStateKey = "dueDateState";
    private static final String datePickerDayKey = "day";
    private static final String datePickerMonthKey = "month";
    private static final String datePickerYearKey = "year";

    private static final String assignedUsersKey = "assignedUsers";
    private static final String assignUserTag = "assign";

    private NewCardActivity.MODE mMode;
    private Card mExistingCard;
    private boolean mKeepFormat;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBoardSettings = ((NewCardActivity) getActivity()).getBoardSettings();
        mLanes = ((NewCardActivity) getActivity()).getLanes();

        mMode = ((NewCardActivity) getActivity()).getMode();

        if (mMode == NewCardActivity.MODE.EDIT_EXISTING) {

            mExistingCard = ((NewCardActivity) getActivity()).getExistingCard();
        }

        mKeepFormat = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(Consts.SHARED_PREFS_KEEP_FORMAT, false);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMode == NewCardActivity.MODE.EDIT_EXISTING) {

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(dueDateStateKey, mDueDateView.getText().toString());

        outState.putInt(datePickerDayKey, datePickerFragment.getDay());
        outState.putInt(datePickerMonthKey, datePickerFragment.getMonth());
        outState.putInt(datePickerYearKey, datePickerFragment.getYear());

        outState.putParcelableArrayList(assignedUsersKey, new ArrayList<BoardUser>(mAssignedUsers));

    }

    public NewCardBasicFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = initUI(inflater, container);

        switch (mMode) {

            case NEW_CARD:
                populateUIForNewCard();
                break;

            case EDIT_EXISTING:
                populateUIForExistingCard();
                break;

        }

        if (savedInstanceState != null) {

            repopulateFromConfigChange(savedInstanceState);
        }

        return v;
    }

    /**
     * Initializes and stores references to all UI components needed in the fragment
     *
     * @param inflater
     * @param container
     * @return the inflated layout view
     */
    private View initUI(LayoutInflater inflater, ViewGroup container) {

        View v = inflater.inflate(R.layout.fragment_new_card_basic, container);

        mLayoutAfterTitleWrapper = (LinearLayout) v.findViewById(R.id.layout_after_title_wrapper);

        titleFrame = v.findViewById(R.id.new_card_title_frame);
        etTitle = (EditText) v.findViewById(R.id.et_title);

        etDescription = (EditText) v.findViewById(R.id.et_desc);

        etSize = (EditText) v.findViewById(R.id.et_size);

        mSpinnerPriorities = (Spinner) v.findViewById(R.id.spinner_priority);
        mPrioritiesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.priorities));
        mSpinnerPriorities.setAdapter(mPrioritiesAdapter);

        mDueDateView = (TextView) v.findViewById(R.id.tv_due_date);
        mDueDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerFragment.show(getFragmentManager(), datePickerTag);
            }
        });

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        int lastSetDay = c.get(Calendar.DAY_OF_MONTH);
        int lastSetMonth = c.get(Calendar.MONTH);
        int lastSetYear = c.get(Calendar.YEAR);

        datePickerFragment = new DatePickerFragment(this, lastSetYear, lastSetDay, lastSetMonth);

        ImageButton clearButton = (ImageButton) v.findViewById(R.id.ib_clear_due_date);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDueDateView.setText("");
            }
        });

        mSpinnerCardTypes = (Spinner) v.findViewById(R.id.spinner_card_type);
        mCardTypeAdapter = new ArrayAdapter<CardType>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mBoardSettings.getCardTypes());
        mSpinnerCardTypes.setAdapter(mCardTypeAdapter);
        mSpinnerCardTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!mBoardSettings.usesClassOfServiceColor()) {

                    CardType selectedType = (CardType) parent.getItemAtPosition(position);

                    titleFrame.setBackgroundColor(Color.parseColor(selectedType.getColorHex()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classOfServiceFrame = v.findViewById(R.id.layout_class_of_service);

        mSpinnerClassOfServices = (Spinner) v.findViewById(R.id.spinner_new_card_class_of_service);

        mClassOfServiceAdapter = new ArrayAdapter<ClassOfService>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mBoardSettings.getClassOfServices());
        mSpinnerClassOfServices.setAdapter(mClassOfServiceAdapter);
        mSpinnerClassOfServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (mBoardSettings.usesClassOfServiceColor()) {

                    ClassOfService selectedClass = (ClassOfService) parent.getItemAtPosition(position);

                    titleFrame.setBackgroundColor(Color.parseColor(selectedClass.getColorHex()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!mBoardSettings.usesClassOfService()) {
            classOfServiceFrame.setVisibility(View.GONE);
        }


        etTags = (EditText) v.findViewById(R.id.et_new_card_tags);

        externalCardIdFrame = v.findViewById(R.id.layout_external_card_id);
        etExternalCardId = (EditText) externalCardIdFrame.findViewById(R.id.et_ext_card_id);

        if (!mBoardSettings.usesExternalCardId() || mBoardSettings.isAutoIncrementCardIdEnabled()){

            externalCardIdFrame.setVisibility(View.GONE);
        }

        mLaneSectionHeader = (TextView) v.findViewById(R.id.new_card_lane_section_header);
        mSpinnerLanes = (Spinner) v.findViewById(R.id.spinner_new_card_lane);
        mLanesAdapter = new LanesSpinnerAdapter(getActivity(), mLanes);
        mSpinnerLanes.setAdapter(mLanesAdapter);

        mAssignedUsersHolder = (LinearLayout) v.findViewById(R.id.layout_new_card_assigned_users_holder);
        mAssignedUsers = new ArrayList<BoardUser>();
        assignUserDialog = new AssignUsersDialogFragment(this);
        TextView assignUsersView = (TextView) v.findViewById(R.id.tv_assign_users);
        assignUsersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assignUserDialog.show(getFragmentManager(), assignUserTag);
            }
        });

        toggleBlocked = (ToggleButton) v.findViewById(R.id.toggle_blocked);
        etBlockReason = (EditText) v.findViewById(R.id.tv_new_card_block_reason);
        toggleBlocked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                etBlockReason.setVisibility(isChecked ? View.VISIBLE : View.GONE);

                if (isChecked) {
                    etBlockReason.requestFocus();
                }

            }
        });
        return v;
    }

    private void populateUIForNewCard() {

        //Begin priority spinner on "Normal" priority
        mSpinnerPriorities.setSelection(1);

        //Begin card type spinner on default type if exists
        for (int i = 0; i < mBoardSettings.getCardTypes().size(); i++) {

            if (mBoardSettings.getCardTypes().get(i).isDefault()) {
                mSpinnerCardTypes.setSelection(i);
                break;
            }
        }

    }

    private void populateUIForExistingCard() {

        etTitle.setText(mExistingCard.getTitle());

        if (mKeepFormat) {

            etDescription.setText(mExistingCard.getDescription());
        } else {

            etDescription.setText(Html.fromHtml(mExistingCard.getDescription()).toString());

        }

        int priority = mExistingCard.getPriority();

        if (priority > -1 && priority < 4) { //TODO: preventative measures until we dynamically build priorities

            mSpinnerPriorities.setSelection(priority);
        }

        if (TextUtils.isEmpty(mExistingCard.getDueDate().trim()) == false) {

            populateDueDateFromExisting();

        }

        //Begin card type spinner on default type if exists
        for (int i = 0; i < mBoardSettings.getCardTypes().size(); i++) {

            if (mExistingCard.getTypeId().equals(mBoardSettings.getCardTypes().get(i).getId())) {

                mSpinnerCardTypes.setSelection(i);
                break;
            }
        }


        for (int i = 0; i < mBoardSettings.getClassOfServices().size(); i++) {

            if (mExistingCard.getClassOfServiceId().equals(mBoardSettings.getClassOfServices().get(i).getId())) {

                mSpinnerClassOfServices.setSelection(i);
                break;
            }
        }

        etExternalCardId.setText( mExistingCard.getExternalCardID() ); //TODO: make sure doesn't conflict with auto-increment

        int size = mExistingCard.getSize();

        if (size > 0) {

            etSize.setText(Integer.toString(size));
        }

        etTags.setText(mExistingCard.getTags());

        mLaneSectionHeader.setVisibility(View.GONE);
        mSpinnerLanes.setVisibility(View.GONE);

        mAssignedUsers = getBoardUsersFromAssignedUsers(mExistingCard.getAssignedUsers());
        refreshAssignedUserViews();

        toggleBlocked.setChecked(mExistingCard.isBlocked());
        etBlockReason.setText(mExistingCard.getBlockReason());

        titleFrame.requestFocus();

    }

    private void populateDueDateFromExisting() {
        try {

            SimpleDateFormat format = new SimpleDateFormat( mBoardSettings.getDateFormat() );

            Date date = format.parse(mExistingCard.getDueDate());

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            int year = cal.get(Calendar.YEAR);
            int monthOfYear = cal.get(Calendar.MONTH);
            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

            format.applyPattern("MM/dd/yyy");

            mDueDateView.setText(format.format(date));

            datePickerFragment = new DatePickerFragment(this, year, dayOfMonth, monthOfYear);

        } catch (ParseException ex) {

            //TODO: what to do?
        }
    }

    private List<BoardUser> getBoardUsersFromAssignedUsers(List<AssignedUser> assignedUsers) {

        List<BoardUser> matches = new ArrayList<BoardUser>();

        for (AssignedUser assignedUser : assignedUsers) {

            for (BoardUser boardUser : mBoardSettings.getBoardUsers()) {

                if (boardUser.getId().equals(assignedUser.getId())) {

                    matches.add(boardUser);
                }
            }
        }

        return matches;
    }

    private void repopulateFromConfigChange(Bundle icicle) {

        FragmentManager fm = getFragmentManager();

        repopulateDueDateFromConfigChange(icicle, fm);

        repopulateAssignedUsersFromConfigChange(icicle, fm);

    }

    private void repopulateDueDateFromConfigChange(Bundle icicle, FragmentManager fm) {

        String retainedDueDateText = icicle.getString(dueDateStateKey);

        mDueDateView.setText(retainedDueDateText);

        int lastSetDay = icicle.getInt(datePickerDayKey);
        int lastSetMonth = icicle.getInt(datePickerMonthKey);
        int lastSetYear = icicle.getInt(datePickerYearKey);

        DatePickerFragment retainedDatePicker = (DatePickerFragment) fm.findFragmentByTag(datePickerTag);

        if (retainedDatePicker == null) {

            datePickerFragment = new DatePickerFragment(this, lastSetYear, lastSetDay, lastSetMonth);

        } else {

            fm.beginTransaction().remove(retainedDatePicker).commit();
            datePickerFragment = new DatePickerFragment(this, retainedDatePicker.getYear(),
                    retainedDatePicker.getDay(),
                    retainedDatePicker.getMonth());
            datePickerFragment.show(fm, datePickerTag);
        }
    }

    private void repopulateAssignedUsersFromConfigChange(Bundle icicle, FragmentManager fm) {

        mAssignedUsers = icicle.getParcelableArrayList(assignedUsersKey);

        refreshAssignedUserViews();

        AssignUsersDialogFragment retainedAssignUserDialog = (AssignUsersDialogFragment) fm.findFragmentByTag(assignUserTag);

        if (retainedAssignUserDialog == null) {

            assignUserDialog = new AssignUsersDialogFragment(this);

        } else {

            fm.beginTransaction().remove(assignUserDialog).commit();
            assignUserDialog = new AssignUsersDialogFragment(this);
            assignUserDialog.show(getFragmentManager(), assignUserTag);

        }
    }

    public void refreshAssignedUserViews() {

        //There are three permanent children in this holder.
        int numAssignedUsers = mAssignedUsersHolder.getChildCount() - 3;

        mAssignedUsersHolder.removeViews(0, numAssignedUsers);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        int gravSize = getResources().getInteger(R.integer.gravatar_assigned_user_list_size);
        assignedUserClearClickListener clearListener = new assignedUserClearClickListener();

        for (BoardUser user : mAssignedUsers) {

            View assignedUserLayout = inflater.inflate(R.layout.layout_assigned_user_clearable, null);

            ImageView gravatar = (ImageView) assignedUserLayout.findViewById(R.id.iv_assigned_user_gravatar);
            TextView userName = (TextView) assignedUserLayout.findViewById(R.id.tv_assigned_user_name);
            ImageButton clearButton = (ImageButton) assignedUserLayout.findViewById(R.id.ib_clear_assigned_user);

            Picasso.with(getActivity())
                    .load(GravatarHelpers.buildGravatarUrl(user.getGravatarLink(), gravSize))
                    .placeholder(R.drawable.mysterymans)
                    .into(gravatar);

            userName.setText(user.getFullName());

            clearButton.setTag(user);

            clearButton.setOnClickListener(clearListener);

            mAssignedUsersHolder.addView(assignedUserLayout, mAssignedUsersHolder.getChildCount() - 3);

        }
    }

    public boolean validateCard() {

        etTitle.setError(null);
        etBlockReason.setError(null);

        if (TextUtils.isEmpty(etTitle.getText().toString().trim())) {
            etTitle.setError(getString(R.string.error_title_required));
            etTitle.requestFocus();
            return false;
        }

        if (toggleBlocked.isChecked()) {

            if (TextUtils.isEmpty(etBlockReason.getText().toString().trim())) {

                etBlockReason.setError(getString(R.string.error_title_required));
                etBlockReason.requestFocus();
                return false;
            }
        }

        return true;
    }

    public CardFieldData getFieldData() {

        CardFieldData data = new CardFieldData();

        data.setTitle(etTitle.getText().toString());

        if (mMode == NewCardActivity.MODE.EDIT_EXISTING && mKeepFormat) {

            data.setDescription(etDescription.getText().toString());
        } else {

            data.setDescription(Html.toHtml(etDescription.getText()));

        }

        data.setPriority(mSpinnerPriorities.getSelectedItemPosition());
        data.setDueDate(mDueDateView.getText().toString());

        data.setCardTypeId(((CardType) mSpinnerCardTypes.getSelectedItem()).getId());

        data.setClassOfServiceId(((ClassOfService) mSpinnerClassOfServices.getSelectedItem()).getId());

        String size = etSize.getText().toString();
        if (TextUtils.isEmpty(size.trim())) {
            data.setSize(0);
        } else {
            data.setSize(Integer.parseInt(size));
        }

        data.setTags(etTags.getText().toString().trim());

        data.setExternalCardId( etExternalCardId.getText().toString() );

        data.setLaneId(((LaneDescription) mSpinnerLanes.getSelectedItem()).getId());

        data.setAssignedUsers(mAssignedUsers);

        data.setBlocked(toggleBlocked.isChecked());

        if (toggleBlocked.isChecked()) {

            data.setBlockedReason(etBlockReason.getText().toString().trim());

        }

        return data;


    }

    public void setDueDateText(String text) {

        mDueDateView.setText(text);

    }

    private class assignedUserClearClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            BoardUser associatedUser = (BoardUser) v.getTag();

            mAssignedUsers.remove(associatedUser);

            mAssignedUsersHolder.removeView((View) v.getParent());

        }
    }

    public class AssignUsersDialogFragment extends DialogFragment {

        private NewCardBasicFragment mHost;

        public AssignUsersDialogFragment(NewCardBasicFragment hostFragment) {

            mHost = hostFragment;

        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            setRetainInstance(true);

            AssignUserCheckableListAdapter a = new AssignUserCheckableListAdapter(getActivity(), mBoardSettings.getBoardUsers());

            final ListView list = new ListView(getActivity());
            list.setAdapter(a);
            list.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

            for (BoardUser assignedUser : mAssignedUsers) {

                for (int i = 0; i < mBoardSettings.getBoardUsers().size(); i++) {

                    if (assignedUser.getId().equals(mBoardSettings.getBoardUsers().get(i).getId())) {

                        list.setItemChecked(i, true);
                    }
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.hint_assign_users)
                    .setView(list)

                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mAssignedUsers.clear();
                            SparseBooleanArray checkedPositions = list.getCheckedItemPositions();

                            for (int i = 0; i < checkedPositions.size(); i++) {

                                if (checkedPositions.valueAt(i)) {

                                    int key = checkedPositions.keyAt(i);
                                    mAssignedUsers.add((BoardUser) list.getItemAtPosition(key));
                                }
                            }

                            mHost.refreshAssignedUserViews();

                        }
                    })

                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //DO NOTHING!
                        }
                    });


            return builder.create();
        }
    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private NewCardBasicFragment mFragment;

        int year;
        int month;
        int day;

        public String getDateText() {

            return String.valueOf(month + 1) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
        }

        public DatePickerFragment(NewCardBasicFragment hostFragment, int year, int day, int month) {
            mFragment = hostFragment;

            this.year = year;
            this.day = day;
            this.month = month;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            setRetainInstance(true);

            return new DatePickerDialog(getActivity(), this, year, month, day);

        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            this.year = year;
            this.day = dayOfMonth;
            this.month = monthOfYear;

            mFragment.setDueDateText(getDateText());

        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }


}
