package com.appshroom.leanpocket.api.retrofit;

import com.appshroom.leanpocket.dto.AddCardReplyData;
import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.BoardHistoryMessage;
import com.appshroom.leanpocket.dto.BoardIdentifiers;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.CardHistory;
import com.appshroom.leanpocket.dto.Comment;
import com.appshroom.leanpocket.dto.DeleteCardsReplyData;
import com.appshroom.leanpocket.dto.GetBoardsBoard;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LeanKitResponse;
import com.appshroom.leanpocket.dto.LeanKitTreeifiedLane;
import com.appshroom.leanpocket.dto.MoveCardToAnotherBoardReplyData;
import com.appshroom.leanpocket.dto.MoveCardWipComment;
import com.appshroom.leanpocket.dto.UpdateCardReplyData;
import com.appshroom.leanpocket.dto.UpdateCardsReplyData;
import com.google.gson.JsonObject;

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


    @GET("/Boards/{boardId}")
    void getBoard(@Path("boardId") String boardId, Callback<LeanKitResponse<Board>> cb);

    @GET("/Boards")
    void getBoards(Callback<LeanKitResponse<List<GetBoardsBoard>>> cb);

    @POST("/Board/{boardId}/AddCard/Lane/{laneId}/Position/{position}")
    void addCard(@Body Card card,
                 @Path("boardId") String boardId,
                 @Path("laneId") String laneId,
                 @Path("position") int position,
                 Callback<LeanKitResponse<AddCardReplyData>> cb); //TODO: do something like I did in volley to handle WIP stuff

    @POST("/Board/{boardId}/AddCard/Lane/{laneId}/Position/{position}")
    void addCardWithWipOverride(@Body Card card,
                                @Path("boardId") String boardId,
                                @Path("laneId") String laneId,
                                @Path("position") int position,
                                Callback<LeanKitResponse<AddCardReplyData>> cb); //TODO: API written wrong? just AddCard/?


    @POST("/Board/{boardId}/AddCards?wipOverrideComment={comment}")
    void addCards(@Body List<Card> cards,
                  @Path("boardId") String boardId,
                  @Path("comment") String wipOverrideComment,
                  Callback<LeanKitResponse<List<Card>>> cb);

    @GET("/Board/{boardId}/GetCard/{cardId}")
    void getCard(@Path("boardId") String boardId,
                 @Path("cardId") String cardId,
                 Callback<LeanKitResponse<Card>> cb);

    @POST("/Board/{boardId}/MoveCard/{cardId}/Lane/{laneId}/Position/{position}")
    void moveCard(@Path("boardId") String boardId,
                  @Path("cardId") String cardId,
                  @Path("laneId") String laneId,
                  @Path("position") int position,
                  Callback<LeanKitResponse<JsonObject>> cb); //TODO: same as above todo.

    @POST("/Board/{boardId}/MoveCardWithWipOverride/{cardId}/Lane/{laneId}/Position/{position}")
    void moveCardWithWipOverride(@Body MoveCardWipComment moveCardWipComment,
                                 @Path("boardId") String boardId,
                                 @Path("cardId") String cardId,
                                 @Path("laneId") String laneId,
                                 @Path("position") int position,
                                 Callback<LeanKitResponse<String>> cb); //TODO: same as above todo.

    @POST("/Board/{boardId}/UpdateCard")
    void updateCard(@Body Card card,
                    @Path("boardId") String boardId,
                    Callback<LeanKitResponse<UpdateCardReplyData>> cb);

    @POST("/Board/{boardId}/UpdateCardWithWipOverride")
    void updateCardWithWipOverride(@Body Card card,
                                   @Path("boardId") String boardId,
                                   Callback<LeanKitResponse<UpdateCardReplyData>> cb);

    @POST("/Board/{boardId}/UpdateCards?wipOverrideComment={comment}")
    void updateCards(@Body List<Card> cards,
                     @Path("boardId") String boardId,
                     @Path("comment") String wipOverrideComment,
                     Callback<LeanKitResponse<UpdateCardsReplyData>> cb);

    @GET("/Board/{boardId}/GetBoardIdentifiers")
    void getBoardIdentifiers(@Path("boardId") String boardId,
                             Callback<LeanKitResponse<BoardIdentifiers>> cb);

    @GET("/Board/{boardId}/BoardVersion/{boardVersion}/GetNewerIfExists")
    void getNewerIfExists(@Path("boardId") String boardId,
                          @Path("boardVersion") String boardVersion,
                          Callback<LeanKitResponse<Board>> cb);

    @GET("/Board/{boardId}/BoardVersion/{boardVersion}/GetBoardHistorySince")
    void getBoardHistorySince(@Path("boardId") String boardId,
                              @Path("boardVersion") String boardVersion,
                              Callback<LeanKitResponse<List<BoardHistoryMessage>>> cb);

    @POST("/Board/{boardId}/DeleteCard/{cardId}")
    void deleteCard(@Path("boardId") String boardId,
                    @Path("cardId") String cardId,
                    Callback<LeanKitResponse<JsonObject>> cb); //TODO: same as above todo.

    @POST("/Board/{boardId}/DeleteCards")
    void deleteCards(@Body List<String> cardIds,
                     @Path("boardId") String boardId,
                     Callback<LeanKitResponse<DeleteCardsReplyData>> cb); //TODO: same as above todo.

    @POST("/Card/MoveCardToAnotherBoard/{cardId}/{boardId}")
    void moveCardToAnotherBoard(@Path("cardId") String cardId,
                                @Path("boardId") String boardId,
                                Callback<LeanKitResponse<MoveCardToAnotherBoardReplyData>> cb);

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

    @POST("/Card/SaveComment/{boardId}/{cardId}")
    void saveComment(@Body Comment comment,
                     @Path("boardId") String boardId,
                     @Path("cardId") String cardId,
                     Callback<LeanKitResponse<JsonObject>> cb); //Todo: API guide says just a "Text: oeuntou" object...not a whole comment obj.


}
