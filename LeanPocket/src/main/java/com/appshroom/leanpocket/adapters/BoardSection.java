package com.appshroom.leanpocket.adapters;

/**
 * Created by jpetrakovich on 8/30/13.
 */
public class BoardSection implements DrawerListItem {

    public enum BoardSectionType {ARCHIVE, BACKLOG, INFLIGHT}

    private String name;
    private BoardSectionType sectionType;
    private boolean isActive = false;
    private boolean isReadyForUse = false;

    public BoardSection(String name, BoardSectionType sectionType) {
        this.name = name;
        this.sectionType = sectionType;
    }

    public BoardSectionType getSectionType() {
        return sectionType;
    }

    @Override
    public DrawerItemType getType() {
        return DrawerItemType.SECTION;
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isReadyForUse() {
        return isReadyForUse;
    }

    public void setReadyForUse(boolean readyForUse) {
        isReadyForUse = readyForUse;
    }
}

