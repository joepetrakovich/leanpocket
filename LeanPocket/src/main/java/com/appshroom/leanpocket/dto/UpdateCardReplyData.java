package com.appshroom.leanpocket.dto;

/**
 * Created by jpetrakovich on 8/7/13.
 */
public class UpdateCardReplyData {

    public String boardVersion;

    public Card cardDTO;

    public String getBoardVersion() {
        return boardVersion;
    }

    public void setBoardVersion(String boardVersion) {
        this.boardVersion = boardVersion;
    }

    public Card getCardDTO() {
        return cardDTO;
    }

    public void setCardDTO(Card cardDTO) {
        this.cardDTO = cardDTO;
    }
}
