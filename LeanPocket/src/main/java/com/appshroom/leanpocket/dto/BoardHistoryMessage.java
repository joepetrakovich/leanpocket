package com.appshroom.leanpocket.dto;


/**
 * Created by jpetrakovich on 8/2/13.
 */

public class BoardHistoryMessage {

    String cardId;
    String message;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
