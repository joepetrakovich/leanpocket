package com.appshroom.leanpocket.adapters;

/**
 * Created by jpetrakovich on 8/30/13.
 */
public interface DrawerListItem {

    public enum DrawerItemType {BOARD, SECTION}

    public DrawerItemType getType();

    public String getName();

}
