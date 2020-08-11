package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.v2.Comment;
import com.appshroom.leanpocket.dto.v2.DeleteCardsRequest;
import com.appshroom.leanpocket.dto.v2.ListBoardsResponse;
import com.appshroom.leanpocket.dto.v2.ListCardsResponse;
import com.appshroom.leanpocket.dto.v2.ListCommentsResponse;
import com.appshroom.leanpocket.dto.v2.MoveCardRequest;
import com.appshroom.leanpocket.dto.v2.SaveCommentRequest;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.EncodedQuery;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RetroLeanKitApiV2 {

    @GET("/board")
    void listBoards(Callback<ListBoardsResponse> cb);

    @GET("/board/{boardId}")
    void getBoardDetails(@Path("boardId") String boardId, Callback<Board> cb);

    @GET("/card")
    void listCards(@Query("board") String boardId,
                   @EncodedQuery("lane_class_types") String commaSeparatedLaneClassTypes,
                   @Query("limit") int limit,
                   Callback<ListCardsResponse> cb);

    @POST("/card/move")
    void moveCards(@Body MoveCardRequest moveCardRequest, Callback<Void> cb);

    @DELETE("/card/{cardId}")
    void deleteCard(@Path("cardId") String cardId, Callback<Void> cb);

    @com.appshroom.leanpocket.api.retrofit.DELETE("/card/")
    void deleteCards(@Body DeleteCardsRequest deleteCardsRequest, Callback<Void> cb);

    @GET("/card/{cardId}/comment")
    void listComments(@Path("cardId") String cardId, Callback<ListCommentsResponse> cb);

    @POST("/card/{cardId}/comment")
    void saveComment(@Path("cardId") String cardId,
                     @Body SaveCommentRequest saveCommentRequest,
                     Callback<Comment> cb);
}
