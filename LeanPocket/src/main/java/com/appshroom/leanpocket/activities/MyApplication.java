package com.appshroom.leanpocket.activities;

import android.app.Application;
import android.util.Base64;

import com.appshroom.leanpocket.R;
import com.appshroom.leanpocket.api.retrofit.RetroLeanKitApi;
import com.appshroom.leanpocket.helpers.Consts;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


/**
 * Created by jpetrakovich on 8/9/13.
 */

@ReportsCrashes(
        formKey = "",
        formUri = "https://appshroom.cloudant.com/acra-leanpocket/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        formUriBasicAuthLogin = "andayousevilectichadvine",
        formUriBasicAuthPassword = "bLaAsNjNvT4NburLrPXR1uJn",
        // Your usual ACRA configuration
        mode = ReportingInteractionMode.DIALOG,
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = R.drawable.ic_launcher,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogOkToast = R.string.crash_dialog_ok_toast

)
public class MyApplication extends Application {


    private static RetroLeanKitApi mRetroLeanKitApi;
    private static RestAdapter mRestAdapter;
    private static String mCurrentHostname = "";


    @Override
    public void onCreate() {
        super.onCreate();

        ACRA.init(this);

    }


    /**
     * Returns an instance of the API singleton.  .init() must be called first to
     * configure the RestAdapter.
     *
     * @return
     */
    public static RetroLeanKitApi getRetroLeanKitApiInstance() {

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
                .setServer(Consts.HTTPS_URL_PREFIX + hostName + Consts.API_URL_SUFFIX)
                .setConverter(gsonConverter)
                .setRequestInterceptor(new BasicAuthInterceptor(userName, pwd))
                .build();

        mRetroLeanKitApi = mRestAdapter.create(RetroLeanKitApi.class);

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
