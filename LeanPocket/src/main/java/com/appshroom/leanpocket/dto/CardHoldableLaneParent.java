package com.appshroom.leanpocket.dto;

import java.util.List;

/**
 * Created by jpetrakovich on 8/19/13.
 */
public class CardHoldableLaneParent {

    private String name;

    private List<Lane> cardHoldingLanes;

    public CardHoldableLaneParent(String name, List<Lane> cardHoldingLanes) {
        this.name = name;
        this.cardHoldingLanes = cardHoldingLanes;
    }

    public List<Lane> getCardHoldingLanes() {
        return cardHoldingLanes;
    }

    public void setCardHoldingLanes(List<Lane> cardHoldingLanes) {
        this.cardHoldingLanes = cardHoldingLanes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
