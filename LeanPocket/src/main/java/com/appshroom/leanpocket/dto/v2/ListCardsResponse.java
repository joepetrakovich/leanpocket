package com.appshroom.leanpocket.dto.v2;

import com.appshroom.leanpocket.dto.Card;

import java.util.List;

public class ListCardsResponse {
    private PageMeta pageMeta;
    private List<Card> cards;

    public List<Card> getCards() {
        return cards;
    }
}
