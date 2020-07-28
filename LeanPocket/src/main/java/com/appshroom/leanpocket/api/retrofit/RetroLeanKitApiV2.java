package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.v2.ListBoardUsersResponse;
import com.appshroom.leanpocket.dto.v2.ListBoardsResponse;
import com.appshroom.leanpocket.dto.v2.ListCardsResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.EncodedQuery;
import retrofit.http.GET;
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

    @GET("/board/{boardId}/user")
    void listBoardUsers(@Path("boardId") String boardId, Callback<ListBoardUsersResponse> cb);

    @GET("/user/me/card")
    void getCardsAssignedToRequestingUser(Callback<ListCardsResponse> cb);
}
