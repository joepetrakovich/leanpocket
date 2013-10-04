package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpetrakovich on 8/3/13.
 */
public class AssignedUser implements Parcelable {

    private String gravatarLink;
    private String smallGravatarLink;
    private String fullName;
    private String id;
    private String emailAddress;


    public AssignedUser() {
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

    public AssignedUser(Parcel p) {

        gravatarLink = p.readString();
        smallGravatarLink = p.readString();
        fullName = p.readString();
        id = p.readString();
        emailAddress = p.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(gravatarLink);
        dest.writeString(smallGravatarLink);
        dest.writeString(fullName);
        dest.writeString(id);
        dest.writeString(emailAddress);
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
