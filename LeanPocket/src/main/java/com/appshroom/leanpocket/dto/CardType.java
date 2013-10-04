package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class CardType implements Parcelable {

    private String id;
    private String name;
    private String colorHex;
    private boolean isDefault;
    private String iconPath;

    public CardType() {
    }

    @Override
    public String toString() {
        return name;
    }

    public static final Creator<CardType> CREATOR = new Creator<CardType>() {

        @Override
        public CardType createFromParcel(Parcel source) {
            return new CardType(source);
        }

        @Override
        public CardType[] newArray(int size) {
            return new CardType[size];
        }
    };

    public CardType(Parcel p) {

        id = p.readString();
        name = p.readString();
        colorHex = p.readString();

        isDefault = p.readInt() == 1;

        iconPath = p.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(colorHex);
        dest.writeInt(isDefault ? 1 : 0);
        dest.writeString(iconPath);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
