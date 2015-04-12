package com.appshroom.leanpocket.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.appshroom.leanpocket.adapters.BoardSection;

/**
 * Created by Joe on 4/11/2015.
 */
public class LaneDescription implements Parcelable {

    private String name;
    private String id;
    private BoardSection.BoardSectionType boardSectionType;

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

    public BoardSection.BoardSectionType getBoardSectionType() {
        return boardSectionType;
    }

    public void setBoardSectionType(BoardSection.BoardSectionType boardSectionType) {
        this.boardSectionType = boardSectionType;
    }

    public LaneDescription(){}

    protected LaneDescription(Parcel in) {
        name = in.readString();
        id = in.readString();

        String boardSectionTypeString = in.readString();

        if (boardSectionTypeString.equals("b")) {
            boardSectionType = BoardSection.BoardSectionType.BACKLOG;
        } else if (boardSectionTypeString.equals("a")) {
            boardSectionType = BoardSection.BoardSectionType.ARCHIVE;
        } else if (boardSectionTypeString.equals("i")) {
            boardSectionType = BoardSection.BoardSectionType.INFLIGHT;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);

        switch (boardSectionType) {

            case ARCHIVE:
                dest.writeString("a");
                break;
            case BACKLOG:
                dest.writeString("b");
                break;
            case INFLIGHT:
                dest.writeString("i");
                break;
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LaneDescription> CREATOR = new Parcelable.Creator<LaneDescription>() {
        @Override
        public LaneDescription createFromParcel(Parcel in) {
            return new LaneDescription(in);
        }

        @Override
        public LaneDescription[] newArray(int size) {
            return new LaneDescription[size];
        }
    };
}