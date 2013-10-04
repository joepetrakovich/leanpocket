package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class BoardUser implements Parcelable {

    private String id;
    private String fullName;
    private String userName;
    private String role;
    private String WIP;
    private String gravatarFeed;
    private String gravatarLink;
    private String emailAddress;
    private String dateFormat;
    private String settings;

    private boolean isEnabled;
    private boolean isAccountOwner;
    private boolean isDeleted;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(fullName);
        dest.writeString(userName);
        dest.writeString(role);
        dest.writeString(WIP);
        dest.writeString(gravatarFeed);
        dest.writeString(gravatarLink);
        dest.writeString(emailAddress);
        dest.writeString(dateFormat);
        dest.writeString(settings);

        dest.writeInt(isEnabled ? 1 : 0);
        dest.writeInt(isAccountOwner ? 1 : 0);
        dest.writeInt(isDeleted ? 1 : 0);

    }

    public static final Creator<BoardUser> CREATOR = new Creator<BoardUser>() {

        @Override
        public BoardUser createFromParcel(Parcel source) {
            return new BoardUser(source);
        }

        @Override
        public BoardUser[] newArray(int size) {
            return new BoardUser[size];
        }
    };

    public BoardUser(Parcel src) {

        id = src.readString();
        fullName = src.readString();
        userName = src.readString();
        role = src.readString();
        WIP = src.readString();
        gravatarFeed = src.readString();
        gravatarLink = src.readString();
        emailAddress = src.readString();
        dateFormat = src.readString();
        settings = src.readString();

        isEnabled = src.readInt() == 1;
        isAccountOwner = src.readInt() == 1;
        isDeleted = src.readInt() == 1;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWIP() {
        return WIP;
    }

    public void setWIP(String WIP) {
        this.WIP = WIP;
    }

    public String getGravatarFeed() {
        return gravatarFeed;
    }

    public void setGravatarFeed(String gravatarFeed) {
        this.gravatarFeed = gravatarFeed;
    }

    public String getGravatarLink() {
        return gravatarLink;
    }

    public void setGravatarLink(String gravatarLink) {
        this.gravatarLink = gravatarLink;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isAccountOwner() {
        return isAccountOwner;
    }

    public void setAccountOwner(boolean accountOwner) {
        isAccountOwner = accountOwner;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


}
