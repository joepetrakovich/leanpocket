package com.appshroom.leanpocket.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.fragments.InfoDialog;

import org.codechimp.apprater.AppRater;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupActionBar();

        String versionName;

        try {

            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException ex) {

            versionName = "X";
        }

        TextView version = (TextView) findViewById(R.id.tv_version);

        version.setText(getString(R.string.version) + " " + versionName);
    }

    public void onCreditsClick(View v) {

        InfoDialog creditsDialogFragment = InfoDialog.newInstance(getString(R.string.credits), getResources().getStringArray(R.array.credits_titles),
                getResources().getStringArray(R.array.credits_contents));

        creditsDialogFragment.show(getFragmentManager(), "credits");


    }

    public void onRateAppClick(View v) {

        AppRater.rateNow(this);

    }

    public void onOpenSourceLicensesClick(View v) {

        InfoDialog openSourceInfoDialogFragment = InfoDialog.newInstance(getString(R.string.open_source_licenses_dialog_title),
                getResources().getStringArray(R.array.open_source_license_titles), getResources().getStringArray(R.array.open_source_licenses));

        openSourceInfoDialogFragment.show(getFragmentManager(), "openSource");
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

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
        }
        return super.onOptionsItemSelected(item);
    }

}
