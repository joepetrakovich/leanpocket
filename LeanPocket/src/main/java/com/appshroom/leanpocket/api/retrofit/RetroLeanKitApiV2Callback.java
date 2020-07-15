package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.helpers.Consts;

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

        switch (replyCode) {

            case Consts.REPLY_CODE.DATA_DELETE_SUCCESS:
            case Consts.REPLY_CODE.DATA_INSERT_SUCCESS:
            case Consts.REPLY_CODE.DATA_RETRIEVAL_SUCCESS:
            case Consts.REPLY_CODE.DATA_UPDATE_SUCCESS:
                onSuccess(replyCode, replyText, replyData);
                break;

            case Consts.REPLY_CODE.WIP_OVERRIDE_COMMENT_REQUIRED:
                onWIPOverrideCommentRequired();

            default:
                onLeanKitException(replyCode, replyText, replyData);

        }

    }

    public abstract void onSuccess(int replyCode, String replyText, List<T> replyData);

    public abstract void onLeanKitException(int replyCode, String replyText, List<T> replyData);

    public abstract void onWIPOverrideCommentRequired();

}
