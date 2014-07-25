package com.appshroom.leanpocket.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitNonPublicApi;
import com.appshroom.leanpocket.dto.GetHostNamesRequestBody;
import com.appshroom.leanpocket.dto.LeanKitResponse;
import com.appshroom.leanpocket.dto.OrganizationLink;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.GravatarHelpers;
import com.appshroom.leanpocket.helpers.SecurePreferences;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends AccountAuthenticatorActivity {


    /**
     * Was the original caller asking for an entirely new account?
     */
    protected boolean mRequestNewAccount = false;
    private String mHostToVerify = "";

    private AccountManager mAccountManager;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;

    private RetroLeanKitNonPublicApi retroLeanKitNonPublicApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccountManager = AccountManager.get(this);

        final Intent intent = getIntent();
        mHostToVerify = intent.getStringExtra(Consts.LEANKIT_HOST_TO_VERIFY);
        mEmail = intent.getStringExtra(Consts.LEANKIT_ACCOUNT_TO_VERIFY);

        mRequestNewAccount = intent.getBooleanExtra(Consts.LEANKIT_ADD_ACCOUNT_REQUEST, false);

        requestWindowFeature(Window.FEATURE_LEFT_ICON);

        getWindow().setFeatureDrawableResource(
                Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);

        setContentView(R.layout.activity_login);

        GsonBuilder gb = new GsonBuilder();
        gb.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

        GsonConverter gsonConverter = new GsonConverter(gb.create());

        retroLeanKitNonPublicApi = new RestAdapter.Builder()
                .setEndpoint(Consts.LEANKIT_LOGIN_SEARCH_URL_PREFIX)
                .setConverter(gsonConverter)
                .build().create(RetroLeanKitNonPublicApi.class);

        // Set up the login form.

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mEmailView.setText(mEmail);
        mEmailView.setAdapter(getEmailAddressAdapter(this));


        if (mHostToVerify != null) {
            mEmailView.setEnabled(false);
        }


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    dismissSoftKeyboard();
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mPasswordView.setText(mPassword);

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismissSoftKeyboard();
                attemptLogin();


            }
        });
    }

    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        Account[] accounts = mAccountManager.getAccountsByType("com.google");
        String[] addresses = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            addresses[i] = accounts[i].name;
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }

    private void dismissSoftKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLoginStatusView.getWindowToken(), 0);
    }


    /**
     * Attempts to sign in with the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            authenticate();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void authenticate() {

        GetHostNamesRequestBody rb = new GetHostNamesRequestBody();
        rb.setUsername(mEmail);
        rb.setPassword(mPassword);

        retroLeanKitNonPublicApi.getHostNames(rb, new Callback<LeanKitResponse<List<OrganizationLink>>>() {
            @Override
            public void success(LeanKitResponse<List<OrganizationLink>> listLeanKitResponse, Response response) {
                showProgress(false);

                if (listLeanKitResponse.getReplyCode() == Consts.REPLY_CODE.NO_DATA) {

                    mEmailView.setError(listLeanKitResponse.getReplyText());
                    mEmailView.requestFocus();

                    return;
                }

                boolean successfulAuth = false;

                if (mRequestNewAccount) {

                    successfulAuth = addNewAccounts(mEmail, mPassword, listLeanKitResponse.getReplyData().get(0));


                } else {

                    successfulAuth = verifyPassword(mEmail, mPassword, mHostToVerify, listLeanKitResponse.getReplyData().get(0));

                }

                if (successfulAuth == false) {

                    //If we made it here then the password was wrong but emails correct.
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }

            }

            @Override
            public void failure(RetrofitError retrofitError) {

                showProgress(false);
                Crouton.clearCroutonsForActivity(getContext());
                Crouton.makeText(getContext(), getString(R.string.check_your_connection), Style.ALERT).show();
            }
        });

    }

    private Activity getContext() {
        return this;
    }

    private boolean addNewAccounts(String userName, String password, List<OrganizationLink> organizationLinks) {

        SharedPreferences securePrefs = new SecurePreferences(this);
        SharedPreferences.Editor editor = securePrefs.edit();

        String tempAccountToReturn = "";

        int numVerifiedAccounts = 0;

        for (OrganizationLink organization : organizationLinks) {

            if (organization.isPasswordVerified()) {

                String hostName = organization.getHostName();
                String accountName = userName + ":" + hostName;

                final Account account = new Account(accountName, Consts.LEANKIT_ACCOUNT_TYPE);

                Bundle userData = new Bundle();
                userData.putString(Consts.LEANKIT_USERDATA_ORG_TEXT, organization.getText());
                userData.putString(Consts.LEANKIT_USERDATA_ORG_HOST, organization.getHostName());
                userData.putString(Consts.LEANKIT_USERDATA_EMAIL, userName);

                String gravatar = GravatarHelpers.getGravatarLink(userName, this);

                userData.putString(Consts.LEANKIT_USERDATA_GRAVATAR, gravatar);

                String key = hostName + ":" + userName;

                mAccountManager.addAccountExplicitly(account, key, userData);
                mAccountManager.setAuthToken(account, Consts.LEANKIT_AUTH_TOKEN_TYPE, password);

                editor.putString(key, password);

                numVerifiedAccounts++;

                tempAccountToReturn = accountName;
            }
        }

        editor.commit();

        if (numVerifiedAccounts > 0) {

            finishLogin(tempAccountToReturn);
            return true;

        }

        return false;
    }


    private boolean verifyPassword(String email, String password, String host, List<OrganizationLink> orgs) {

        for (OrganizationLink org : orgs) {

            if (org.getHostName().equals(host)) {

                if (org.isPasswordVerified()) {

                    SharedPreferences securePrefs = new SecurePreferences(this);
                    SharedPreferences.Editor editor = securePrefs.edit();

                    String key = host + ":" + email;
                    editor.putString(key, password);

                    editor.commit();

                    finishLogin(email + ":" + host);
                    return true;

                }
            }
        }

        return false;

    }

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. We store the
     * authToken that's returned from the server as the 'password' for this
     * account - so we're never storing the user's actual password locally.
     */
    private void finishLogin(String accountName) {

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, accountName);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Consts.LEANKIT_ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }


}
