package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class ClassOfService implements Parcelable {

    private String id;
    private String boardId;
    private String title;
    private String policy;
    private String iconPath;
    private String colorHex;
    private boolean useColor;

    public ClassOfService(String title) {

        this.title = title;
        this.colorHex = "#ffffff";

    }

    @Override
    public String toString() {
        return title;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(boardId);
        dest.writeString(title);
        dest.writeString(policy);
        dest.writeString(iconPath);
        dest.writeString(colorHex);
        dest.writeInt(useColor ? 1 : 0);
    }

    public static final Creator<ClassOfService> CREATOR = new Creator<ClassOfService>() {


        @Override
        public ClassOfService createFromParcel(Parcel source) {
            return new ClassOfService(source);
        }

        @Override
        public ClassOfService[] newArray(int size) {
            return new ClassOfService[size];
        }
    };

    public ClassOfService(Parcel p) {

        id = p.readString();
        boardId = p.readString();
        title = p.readString();
        policy = p.readString();
        iconPath = p.readString();
        colorHex = p.readString();
        useColor = p.readInt() == 1;

    }


}
