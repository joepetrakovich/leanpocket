package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.LeanKitResponse;
import com.appshroom.leanpocket.helpers.Consts;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;

/**
 * Created by jpetrakovich on 8/20/13.
 * <p/>
 * Consider requiring an error handling class implementation.
 */
public abstract class RetroLeanKitCallback<T> implements Callback<LeanKitResponse<T>> {


    @Override
    public void success(LeanKitResponse<T> tLeanKitResponse, Response response) {

        int replyCode = tLeanKitResponse.getReplyCode();
        String replyText = tLeanKitResponse.getReplyText();
        List<T> replyData = tLeanKitResponse.getReplyData();

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
