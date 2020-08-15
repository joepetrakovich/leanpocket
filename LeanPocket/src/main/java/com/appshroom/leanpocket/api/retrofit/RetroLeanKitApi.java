package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.LeanKitResponse;
import com.appshroom.leanpocket.dto.UpdateCardReplyData;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;

public interface RetroLeanKitApi {

    @POST("/Board/{boardId}/UpdateCard")
    void updateCard(@Body Card card,
                    @Path("boardId") String boardId,
                    Callback<LeanKitResponse<UpdateCardReplyData>> cb);

}
