package com.appshroom.leanpocket.dto;

import java.util.List;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class BoardIdentifiers {

    private String boardId;

    private List<Identifier> cardTypes;
    private List<Identifier> boardUsers;
    private List<Identifier> lanes;
    private List<Identifier> classesOfServices;
    private List<Identifier> priorities;

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public List<Identifier> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(List<Identifier> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public List<Identifier> getBoardUsers() {
        return boardUsers;
    }

    public void setBoardUsers(List<Identifier> boardUsers) {
        this.boardUsers = boardUsers;
    }

    public List<Identifier> getLanes() {
        return lanes;
    }

    public void setLanes(List<Identifier> lanes) {
        this.lanes = lanes;
    }

    public List<Identifier> getClassesOfServices() {
        return classesOfServices;
    }

    public void setClassesOfServices(List<Identifier> classesOfServices) {
        this.classesOfServices = classesOfServices;
    }

    public List<Identifier> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<Identifier> priorities) {
        this.priorities = priorities;
    }
}
