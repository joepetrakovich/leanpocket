package com.appshroom.leanpocket.dto;

/**
 * Created by jpetrakovich on 8/7/13.
 */
public class AddCardReplyData {

    public String boardVersion;
    public Lane lane;
    public String cardId;

    public String getBoardVersion() {
        return boardVersion;
    }

    public void setBoardVersion(String boardVersion) {
        this.boardVersion = boardVersion;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
