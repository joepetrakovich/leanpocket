package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.AddCardReplyData;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.CardHistory;
import com.appshroom.leanpocket.dto.Comment;
import com.appshroom.leanpocket.dto.DeleteCardsReplyData;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LeanKitResponse;
import com.appshroom.leanpocket.dto.LeanKitTreeifiedLane;
import com.appshroom.leanpocket.dto.UpdateCardReplyData;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by jpetrakovich on 8/16/13.
 */
public interface RetroLeanKitApi {

    @POST("/Board/{boardId}/AddCard/Lane/{laneId}/Position/{position}")
    void addCard(@Body Card card,
                 @Path("boardId") String boardId,
                 @Path("laneId") String laneId,
                 @Path("position") int position,
                 Callback<LeanKitResponse<AddCardReplyData>> cb); //TODO: do something like I did in volley to handle WIP stuff

    @POST("/Board/{boardId}/UpdateCard")
    void updateCard(@Body Card card,
                    @Path("boardId") String boardId,
                    Callback<LeanKitResponse<UpdateCardReplyData>> cb);

    @POST("/Board/{boardId}/DeleteCards")
    void deleteCards(@Body List<String> cardIds,
                     @Path("boardId") String boardId,
                     Callback<LeanKitResponse<DeleteCardsReplyData>> cb);


    @GET("/Board/{boardId}/Backlog")
    void backlog(@Path("boardId") String boardId,
                 Callback<LeanKitResponse<List<Lane>>> cb);

    @GET("/Board/{boardId}/Archive")
    void archive(@Path("boardId") String boardId,
                 Callback<LeanKitResponse<List<LeanKitTreeifiedLane>>> cb);

    @GET("/Card/History/{boardId}/{cardId}")
    void getCardHistory(@Path("boardId") String boardId,
                        @Path("cardId") String cardId,
                        Callback<LeanKitResponse<List<CardHistory>>> cb);

    @GET("/Card/GetComments/{boardId}/{cardId}")
    void getComments(@Path("boardId") String boardId,
                     @Path("cardId") String cardId,
                     Callback<LeanKitResponse<List<Comment>>> cb);

}
