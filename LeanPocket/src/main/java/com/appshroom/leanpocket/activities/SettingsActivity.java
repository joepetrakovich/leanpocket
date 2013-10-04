package com.appshroom.leanpocket.activities;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.fragments.SettingsFragment;
import com.appshroom.leanpocket.helpers.Consts;

import java.io.IOException;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the Up button in the action bar.
        setupActionBar();

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayUseLogoEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
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
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;

            case R.id.action_add_account:
                startAddAccount();
                return true;

            case R.id.action_send_feedback:
                sendEmailIntentForFeedback();
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

    private void startAddAccount() {

        AccountManager mAccountManager = AccountManager.get(this);

        mAccountManager.addAccount(Consts.LEANKIT_ACCOUNT_TYPE, Consts.LEANKIT_AUTH_TOKEN_TYPE, null,
                null, null, new OnAddAccountTokenAcquired(), null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case Consts.REQUEST_CODE_ADD_ACCOUNT:

                handleAddAccountResult(resultCode);

                break;

        }
    }

    private void handleAddAccountResult(int resultCode) {

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

}
