package com.appshroom.leanpocket.api.retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;

public abstract class RetroLeanKitApiV2Callback<T> implements Callback<T> {

    @Override
    public void success(T tLeanKitResponse, Response response) {

        int replyCode = response.getStatus();
        String replyText = response.getReason();
        List<T> replyData = new ArrayList<T>();
        replyData.add(tLeanKitResponse);

        if (replyCode >= 200 && replyCode < 300) {
            onSuccess(replyCode, replyText, replyData);
        } else {
            onLeanKitException(replyCode, replyText, replyData);
        }
    }

    public abstract void onSuccess(int replyCode, String replyText, List<T> replyData);

    public abstract void onLeanKitException(int replyCode, String replyText, List<T> replyData);

}
