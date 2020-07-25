package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.appshroom.leanpocket.adapters.BoardSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class Lane implements Parcelable {

    private int index;
    private int cardLimit;
    private int width;

    private boolean isActive;

    private String id;
    private String name;
    private String laneClassType;
    private String type;
    private String activityId;
    private String activityName;
    private String parentLaneId;
    private String orientation;
    private List<Card> cards = new ArrayList<>();

    private List<String> childLaneIds;

    private List<String> siblingLaneIds;

    private List<Lane> childLanes;

    @Override
    public String toString() {
        return name;
    }

    public static final Creator<Lane> CREATOR = new Creator<Lane>() {
        @Override
        public Lane createFromParcel(Parcel source) {
            return new Lane(source);
        }

        @Override
        public Lane[] newArray(int size) {
            return new Lane[size];
        }
    };

    public Lane(Parcel p) {

        index = p.readInt();
        cardLimit = p.readInt();
        width = p.readInt();

        isActive = p.readInt() == 1;

        id = p.readString();
        name = p.readString();
        laneClassType = p.readString();
        type = p.readString();
        activityId = p.readString();
        activityName = p.readString();
        parentLaneId = p.readString();
        orientation = p.readString();

        cards = new ArrayList<Card>();
        p.readTypedList(cards, Card.CREATOR);

        childLaneIds = new ArrayList<String>();
        p.readStringList(childLaneIds);

        siblingLaneIds = new ArrayList<String>();
        p.readStringList(siblingLaneIds);

        childLanes = new ArrayList<Lane>();
        p.readTypedList(childLanes, CREATOR);


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(index);
        dest.writeInt(cardLimit);
        dest.writeInt(width);

        dest.writeInt(isActive ? 1 : 0);

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(laneClassType);
        dest.writeString(type);
        dest.writeString(activityId);
        dest.writeString(activityName);
        dest.writeString(parentLaneId);
        dest.writeString(orientation);

        dest.writeTypedList(cards);
        dest.writeStringList(childLaneIds);
        dest.writeStringList(siblingLaneIds);
        dest.writeTypedList(childLanes);
    }

    public Lane() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCardLimit() {
        return cardLimit;
    }

    public void setCardLimit(int cardLimit) {
        this.cardLimit = cardLimit;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public String getLaneClassType() {
        return laneClassType;
    }

    public void setLaneClassType(String laneClassType) {
        this.laneClassType = laneClassType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getParentLaneId() {
        return parentLaneId;
    }

    public void setParentLaneId(String parentLaneId) {
        this.parentLaneId = parentLaneId;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<String> getChildLaneIds() {
        return childLaneIds;
    }

    public void setChildLaneIds(List<String> childLaneIds) {
        this.childLaneIds = childLaneIds;
    }

    public List<String> getSiblingLaneIds() {
        return siblingLaneIds;
    }

    public void setSiblingLaneIds(List<String> siblingLaneIds) {
        this.siblingLaneIds = siblingLaneIds;
    }

    public List<Lane> getChildLanes() {
        return childLanes;
    }

    public void setChildLanes(List<Lane> childLanes) {
        this.childLanes = childLanes;
    }

    public BoardSection.BoardSectionType getBoardSectionType(){
        switch (laneClassType) {
            case "backlog": return BoardSection.BoardSectionType.BACKLOG;
            case "archive": return BoardSection.BoardSectionType.ARCHIVE;
            case "active": return BoardSection.BoardSectionType.INFLIGHT;
        }
        return BoardSection.BoardSectionType.INFLIGHT;
    }
}
