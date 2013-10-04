package com.appshroom.leanpocket.dto;

import com.appshroom.leanpocket.adapters.DrawerListItem;

import java.util.Date;

/**
 * Created by jpetrakovich on 8/3/13.
 */
public class GetBoardsBoard implements DrawerListItem {

    private String id;

    private String title;

    private String description;

    private boolean isArchived;

    private Date creationDate;

    private boolean isActiveBoard;

    //Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return title;
    }

    public boolean isActiveBoard() {
        return isActiveBoard;
    }

    public void setActiveBoard(boolean activeBoard) {
        isActiveBoard = activeBoard;
    }

    @Override
    public DrawerItemType getType() {
        return DrawerItemType.BOARD;
    }

    @Override
    public String getName() {
        return title;
    }
}
