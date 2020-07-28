package com.appshroom.leanpocket.dto.v2;

import java.util.ArrayList;
import java.util.List;

public class MoveCardRequest {
    List<String> cardIds = new ArrayList<String>();
    CardDestination destination;

    public void setCardDestination(String laneId, int index) {
        destination = new CardDestination(laneId, index);
    }

    public void AddCardToMove(String cardId) {
        this.cardIds.add(cardId);
    }
}

