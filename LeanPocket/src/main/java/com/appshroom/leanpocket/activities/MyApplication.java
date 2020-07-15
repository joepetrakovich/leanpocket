package com.appshroom.leanpocket.activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Base64;

import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApiV2;
import com.appshroom.leanpocket.helpers.Consts;
import com.appshroom.leanpocket.helpers.SecurePreferences;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;


import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


public class MyApplication extends Application {


    private static RetroLeanKitApi mRetroLeanKitApi;
    private static RetroLeanKitApiV2 mRetroLeanKitApiV2;
    private static RestAdapter mRestAdapter;
    private static String mCurrentHostname = "";

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * Returns an instance of the API singleton.  .init() must be called first to
     * configure the RestAdapter.
     *
     * @return
     */
    public RetroLeanKitApi getRetroLeanKitApiInstance() {

        if (mRetroLeanKitApi == null){

            SharedPreferences mSharedPreferences = new SecurePreferences(getApplicationContext());
            String host = mSharedPreferences.getString(Consts.SHARED_PREFS_HOST_NAME, "");
            String email = mSharedPreferences.getString(Consts.SHARED_PREFS_USER_NAME, "");
            String pwd = mSharedPreferences.getString(Consts.SHARED_PREFS_PWD, "");

            initRetroLeanKitApi(host, email, pwd);
        }

        return mRetroLeanKitApi;
    }

    public static String getCurrentHostname() {
        return mCurrentHostname;
    }

    /**
     * must be called before getInstance().
     *
     * @param hostName the LeanKit hostname assigned to the user
     * @param userName the LeanKit username (an email address)
     * @param pwd      the users password
     */
    public static void initRetroLeanKitApi(String hostName, String userName, String pwd) {

        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("MM/dd/yyyy")
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);

        mCurrentHostname = hostName;

        GsonConverter gsonConverter = new GsonConverter(gb.create());

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Consts.HTTPS_URL_PREFIX + hostName + Consts.API_V1_URL_SUFFIX)
                .setConverter(gsonConverter)
                .setRequestInterceptor(new BasicAuthInterceptor(userName, pwd))
                .build();

        mRetroLeanKitApi = mRestAdapter.create(RetroLeanKitApi.class);

    }

    /**
     * Returns an instance of the v2 API singleton.  .init() must be called first to
     * configure the RestAdapter.
     *
     * @return
     */
    public RetroLeanKitApiV2 getRetroLeanKitApiV2Instance() {

        if (mRetroLeanKitApiV2 == null){

            SharedPreferences mSharedPreferences = new SecurePreferences(getApplicationContext());
            String host = mSharedPreferences.getString(Consts.SHARED_PREFS_HOST_NAME, "");
            String email = mSharedPreferences.getString(Consts.SHARED_PREFS_USER_NAME, "");
            String pwd = mSharedPreferences.getString(Consts.SHARED_PREFS_PWD, "");

            initRetroLeanKitApiV2(host, email, pwd);
        }

        return mRetroLeanKitApiV2;
    }

    /**
     * must be called before getInstance().
     *
     * @param hostName the LeanKit hostname assigned to the user
     * @param userName the LeanKit username (an email address)
     * @param pwd      the users password
     */
    public static void initRetroLeanKitApiV2(String hostName, String userName, String pwd) {

        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("MM/dd/yyyy");

        mCurrentHostname = hostName;

        GsonConverter gsonConverter = new GsonConverter(gb.create());

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(Consts.HTTPS_URL_PREFIX + hostName + Consts.API_V2_URL_SUFFIX)
                .setConverter(gsonConverter)
                .setRequestInterceptor(new BasicAuthInterceptor(userName, pwd))
                .build();

        mRetroLeanKitApiV2 = mRestAdapter.create(RetroLeanKitApiV2.class);

    }


    private static class BasicAuthInterceptor implements RequestInterceptor {

        private String authHeader;

        public BasicAuthInterceptor(String userName, String pwd) {

            authHeader = "Basic "
                    + Base64.encodeToString((userName + ":" + pwd).getBytes(), Base64.NO_WRAP);
        }

        @Override
        public void intercept(RequestFacade requestFacade) {
            requestFacade.addHeader("Authorization", authHeader);
        }
    }


}
