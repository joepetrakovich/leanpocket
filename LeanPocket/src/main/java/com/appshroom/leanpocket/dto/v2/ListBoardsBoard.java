package com.appshroom.leanpocket.dto.v2;

import com.appshroom.leanpocket.adapters.DrawerListItem;

import java.util.Date;

/**
 * Created by jpetrakovich on 8/3/13.
 */
public class ListBoardsBoard implements DrawerListItem {

    private String id;

    private String title;

    private String description;

    private int boardRoleId;

    private boolean isWelcome;

    private String boardRole;

    private boolean isActiveBoard;

    //Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    public boolean isActiveBoard() {
        return isActiveBoard;
    }

    public boolean isArchived() { return false; } //TODO: this changed in V2

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
