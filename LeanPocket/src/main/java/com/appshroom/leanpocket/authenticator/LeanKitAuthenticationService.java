package com.appshroom.leanpocket.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service to handle Account authentication. It instantiates the authenticator
 * and returns its IBinder.
 */
public class LeanKitAuthenticationService extends Service {

    private static final String TAG = "LeanKitAuthenticationService";

    private LeanKitAccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {

        mAuthenticator = new LeanKitAccountAuthenticator(this);
    }


    @Override
    public IBinder onBind(Intent intent) {

        return mAuthenticator.getIBinder();
    }
}
