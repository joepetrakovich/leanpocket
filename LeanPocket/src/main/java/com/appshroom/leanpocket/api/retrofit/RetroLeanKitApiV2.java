package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.v2.ListBoardsResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;


public interface RetroLeanKitApiV2 {

    @GET("/board")
    void listBoards(Callback<ListBoardsResponse> cb);

    @GET("/board/{boardId}")
    void getBoardDetails(@Path("boardId") String boardId, Callback<Board> cb);

}
