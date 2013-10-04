package com.appshroom.leanpocket.dto;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class ClassOfService {

    private String id;
    private String boardId;
    private String title;
    private String policy;
    private String iconPath;
    private String colorHex;
    private boolean useColor;

    public String getId() {
        return id;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getTitle() {
        return title;
    }

    public String getPolicy() {
        return policy;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getColorHex() {
        return colorHex;
    }

    public boolean isUseColor() {
        return useColor;
    }
}
