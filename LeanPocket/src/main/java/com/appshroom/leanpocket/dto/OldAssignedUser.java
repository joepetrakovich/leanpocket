package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpetrakovich on 8/3/13.
 */
public class OldAssignedUser implements Parcelable {

    private String gravatarLink;
    private String smallGravatarLink;
    private String assignedUserName;
    private String assignedUserId;


    public OldAssignedUser() {
    }

    public static final Creator<AssignedUser> CREATOR = new Creator<AssignedUser>() {

        @Override
        public AssignedUser createFromParcel(Parcel source) {
            return new AssignedUser(source);
        }

        @Override
        public AssignedUser[] newArray(int size) {
            return new AssignedUser[size];
        }
    };

    public OldAssignedUser(Parcel p) {

        gravatarLink = p.readString();
        smallGravatarLink = p.readString();
        assignedUserName = p.readString();
        assignedUserId = p.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(gravatarLink);
        dest.writeString(smallGravatarLink);
        dest.writeString(assignedUserName);
        dest.writeString(assignedUserId);
    }

    public String getGravatarLink() {
        return gravatarLink;
    }

    public void setGravatarLink(String gravatarLink) {
        this.gravatarLink = gravatarLink;
    }

    public String getSmallGravatarLink() {
        return smallGravatarLink;
    }

    public void setSmallGravatarLink(String smallGravatarLink) {
        this.smallGravatarLink = smallGravatarLink;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }


}
