package com.appshroom.leanpocket.authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.SecurePreferences;

/**
 * Created by jpetrakovich on 8/14/13.
 */
public class LeanKitAccountAuthenticator extends AbstractAccountAuthenticator {


    Context mContext;

    public LeanKitAccountAuthenticator(Context context) {
        super(context);

        this.mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(mContext, LoginActivity.class);

        intent.putExtra(Consts.LEANKIT_ACCOUNT_TYPE, accountType);
        intent.putExtra(Consts.LEANKIT_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(Consts.LEANKIT_ADD_ACCOUNT_REQUEST, true);

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;

    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    private String getPasswordFromPrefs(String key) {

        SharedPreferences securePrefs = new SecurePreferences(mContext);

        return securePrefs.getString(key, "");

    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {

        // Extract the username and password from the Account Manager, and ask
        // the server for an appropriate AuthToken.
        final AccountManager am = AccountManager.get(mContext);

        String authToken = getPasswordFromPrefs(am.getPassword(account));

        // If we get an authToken - we return it
        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        // If we get here, then we couldn't access the user's password - so we
        // need to re-prompt them for their credentials. We do that by creating
        // an intent to display our AuthenticatorActivity.
        final Intent intent = new Intent(mContext, LoginActivity.class);

        String host = am.getUserData(account, Consts.LEANKIT_USERDATA_ORG_HOST);

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(Consts.LEANKIT_ACCOUNT_TYPE, account.type);
        intent.putExtra(Consts.LEANKIT_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(Consts.LEANKIT_VERIFY_HOST_REQUEST, true);
        intent.putExtra(Consts.LEANKIT_HOST_TO_VERIFY, host);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
