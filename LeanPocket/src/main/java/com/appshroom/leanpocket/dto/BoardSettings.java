package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpetrakovich on 10/5/13.
 */
public class BoardSettings implements Parcelable {

    private List<CardType> cardTypes;
    private List<ClassOfService> classOfServices;
    private List<BoardUser> boardUsers;

    private String dateFormat;
    private String cardIdPrefix;

    private boolean usesClassOfService;
    private boolean usesClassOfServiceColor;
    private boolean usesExternalCardId;
    private boolean isCardHeaderEnabled;
    private boolean isCardIdPrefixEnabled;
    private boolean isAutoIncrementCardIdEnabled;


    public BoardSettings(){}

    public List<CardType> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(List<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public List<ClassOfService> getClassOfServices() {
        return classOfServices;
    }

    public void setClassOfServices(List<ClassOfService> classOfServices) {
        this.classOfServices = classOfServices;
    }

    public List<BoardUser> getBoardUsers() {
        return boardUsers;
    }

    public void setBoardUsers(List<BoardUser> boardUsers) {
        this.boardUsers = boardUsers;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getCardIdPrefix() {
        return cardIdPrefix;
    }

    public void setCardIdPrefix(String cardIdPrefix) {
        this.cardIdPrefix = cardIdPrefix;
    }

    public boolean usesClassOfService() {
        return usesClassOfService;
    }

    public void setUsesClassOfService(boolean usesClassOfService) {
        this.usesClassOfService = usesClassOfService;
    }

    public boolean usesClassOfServiceColor() {
        return usesClassOfServiceColor;
    }

    public void setUsesClassOfServiceColor(boolean usesClassOfServiceColor) {
        this.usesClassOfServiceColor = usesClassOfServiceColor;
    }

    public boolean usesExternalCardId() {
        return usesExternalCardId;
    }

    public void setUsesExternalCardId(boolean usesExternalCardId) {
        this.usesExternalCardId = usesExternalCardId;
    }

    public boolean isCardHeaderEnabled() {
        return isCardHeaderEnabled;
    }

    public void setCardHeaderEnabled(boolean cardHeaderEnabled) {
        isCardHeaderEnabled = cardHeaderEnabled;
    }

    public boolean isCardIdPrefixEnabled() {
        return isCardIdPrefixEnabled;
    }

    public void setCardIdPrefixEnabled(boolean cardIdPrefixEnabled) {
        isCardIdPrefixEnabled = cardIdPrefixEnabled;
    }

    public boolean isAutoIncrementCardIdEnabled() {
        return isAutoIncrementCardIdEnabled;
    }

    public void setAutoIncrementCardIdEnabled(boolean autoIncrementCardIdEnabled) {
        isAutoIncrementCardIdEnabled = autoIncrementCardIdEnabled;
    }

    public static final Creator<BoardSettings> CREATOR = new Creator<BoardSettings>() {

        @Override
        public BoardSettings createFromParcel(Parcel source) {
            return new BoardSettings(source);
        }

        @Override
        public BoardSettings[] newArray(int size) {
            return new BoardSettings[size];
        }
    };

    public BoardSettings(Parcel p){

        cardTypes = new ArrayList<CardType>();
        classOfServices = new ArrayList<ClassOfService>();
        boardUsers = new ArrayList<BoardUser>();

        p.readTypedList(cardTypes, CardType.CREATOR);
        p.readTypedList(classOfServices, ClassOfService.CREATOR);
        p.readTypedList(boardUsers, BoardUser.CREATOR);

        dateFormat = p.readString();
        cardIdPrefix = p.readString();

        usesClassOfService = p.readInt() == 1;
        usesClassOfServiceColor = p.readInt() == 1;
        usesExternalCardId = p.readInt() == 1;
        isCardHeaderEnabled = p.readInt() == 1;
        isCardIdPrefixEnabled = p.readInt() == 1;
        isAutoIncrementCardIdEnabled = p.readInt() == 1;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeTypedList(cardTypes);
        dest.writeTypedList(classOfServices);
        dest.writeTypedList(boardUsers);

        dest.writeString(dateFormat);
        dest.writeString(cardIdPrefix);

        dest.writeInt(usesClassOfService ? 1 : 0);
        dest.writeInt(usesClassOfServiceColor ? 1 : 0);
        dest.writeInt(usesExternalCardId ? 1 : 0);
        dest.writeInt(isCardHeaderEnabled ? 1 : 0);
        dest.writeInt(isCardIdPrefixEnabled ? 1 : 0);
        dest.writeInt(isAutoIncrementCardIdEnabled ? 1 : 0);

    }
}
