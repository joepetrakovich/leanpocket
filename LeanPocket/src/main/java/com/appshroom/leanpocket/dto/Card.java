package com.appshroom.leanpocket.dto;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class Card implements Parcelable {

    private int index;
    private String priority;
    private int size;

    private boolean active;
    private boolean isBlocked;

    private String dueDate;

    private String id;
    private String title;
    private String description;
    private String typeId;
    private String typeName;
    private String typeIconPath;
    private String typeColorHex;
    private String color;
    private List<String> tags;
    private String version;
    private String assignedUserId;
    private String assignedUserName;
    private String gravatarLink;
    private String blockReason;
    private String smallGravatarLink;
    private String externalSystemName;
    private String externalSystemUrl;
    private String externalCardID;
    private String classOfServiceId;
    private String classOfServiceTitle;
    private String classOfServiceIconPath;
    private String classOfServiceColorHex;
    private String dueDateText;
    private String userWipOverrideComment;
    private String systemType;

    private CardType type;

    private List<AssignedUser> assignedUsers;
    private List<String> assignedUserIds;

    private int countOfOldCards;
    private String dateArchived;
    private String lastComment;
    private int attachmentsCount;
    private String drillThroughBoardId;
    private int drillThroughBoardCompletionPercent;
    private boolean hasDrillThroughBoard;
    private String currentTaskBoardId;
    private String taskBoardCompletionPercent;
    private int taskBoardTotalCards;
    private String currentContext;
    private String parentCardId;
    private Lane lane;



    public static final Creator<Card> CREATOR = new Creator<Card>() {

        @Override
        public Card createFromParcel(Parcel source) {
            return new Card(source);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    public Card(Parcel p) {

        index = p.readInt();
        priority = p.readString();
        size = p.readInt();

        active = p.readInt() == 1;
        isBlocked = p.readInt() == 1;

        dueDate = p.readString();
        id = p.readString();
        title = p.readString();
        description = p.readString();
        typeId = p.readString();
        typeName = p.readString();
        typeIconPath = p.readString();
        typeColorHex = p.readString();
        color = p.readString();
        tags = new ArrayList<String>();
        p.readStringList(tags);
        version = p.readString();
        assignedUserId = p.readString();
        assignedUserName = p.readString();
        gravatarLink = p.readString();
        blockReason = p.readString();
        smallGravatarLink = p.readString();
        externalSystemName = p.readString();
        externalSystemUrl = p.readString();
        externalCardID = p.readString();
        classOfServiceId = p.readString();
        classOfServiceTitle = p.readString();
        classOfServiceIconPath = p.readString();
        classOfServiceColorHex = p.readString();
        dueDateText = p.readString();
        userWipOverrideComment = p.readString();
        systemType = p.readString();

        lane = p.readParcelable(Lane.class.getClassLoader());

        type = p.readParcelable(CardType.class.getClassLoader());

        assignedUsers = new ArrayList<AssignedUser>();
        p.readTypedList(assignedUsers, AssignedUser.CREATOR);

        assignedUserIds = new ArrayList<String>();
        p.readStringList(assignedUserIds);

        countOfOldCards = p.readInt();
        dateArchived = p.readString();
        lastComment = p.readString();
        attachmentsCount = p.readInt();
        drillThroughBoardId = p.readString();
        drillThroughBoardCompletionPercent = p.readInt();
        hasDrillThroughBoard = p.readInt() == 1;
        currentTaskBoardId = p.readString();
        taskBoardCompletionPercent = p.readString();
        taskBoardTotalCards = p.readInt();
        currentContext = p.readString();
        parentCardId = p.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(index);
        dest.writeString(priority);
        dest.writeInt(size);

        dest.writeInt(active ? 1 : 0);
        dest.writeInt(isBlocked ? 1 : 0);

        dest.writeString(dueDate);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(typeId);
        dest.writeString(typeName);
        dest.writeString(typeIconPath);
        dest.writeString(typeColorHex);
        dest.writeString(color);
        dest.writeStringList(tags);
        dest.writeString(version);
        dest.writeString(assignedUserId);
        dest.writeString(assignedUserName);
        dest.writeString(gravatarLink);
        dest.writeString(blockReason);
        dest.writeString(smallGravatarLink);
        dest.writeString(externalSystemName);
        dest.writeString(externalSystemUrl);
        dest.writeString(externalCardID);
        dest.writeString(classOfServiceId);
        dest.writeString(classOfServiceTitle);
        dest.writeString(classOfServiceIconPath);
        dest.writeString(classOfServiceColorHex);
        dest.writeString(dueDateText);
        dest.writeString(userWipOverrideComment);
        dest.writeString(systemType);

        dest.writeParcelable(type, flags);
        dest.writeParcelable(lane, flags);

        dest.writeTypedList(assignedUsers);

        dest.writeStringList(assignedUserIds);

        dest.writeInt(countOfOldCards);
        dest.writeString(dateArchived);
        dest.writeString(lastComment);
        dest.writeInt(attachmentsCount);
        dest.writeString(drillThroughBoardId);
        dest.writeInt(drillThroughBoardCompletionPercent);
        dest.writeInt(hasDrillThroughBoard ? 1 : 0);
        dest.writeString(currentTaskBoardId);
        dest.writeString(taskBoardCompletionPercent);
        dest.writeInt(taskBoardTotalCards);
        dest.writeString(currentContext);
        dest.writeString(parentCardId);

    }

    public Card() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public int getIndex() {
        return index;
    }

    public String getPriority() {
        return priority;
    }

    public int getSize() {
        return size;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getId() {
        return id;
    }

    public String getLaneId() {
        return lane.getId();
    }

    public String getTitle() {
        return title;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeIconPath() {
        return typeIconPath;
    }

    public String getTypeColorHex() {
        return typeColorHex;
    }

    public String getColor() {
        return color;
    }

    public String getVersion() {
        return version;
    }

    public String getAssignedUserId() {
        return assignedUserId;
    }

    public String getAssignedUserName() {
        return assignedUserName;
    }

    public String getGravatarLink() {
        return gravatarLink;
    }

    public String getBlockReason() {
        return blockReason;
    }

    public String getSmallGravatarLink() {
        return smallGravatarLink;
    }

    public String getExternalSystemName() {
        return externalSystemName;
    }

    public String getExternalSystemUrl() {
        return externalSystemUrl;
    }

    public String getExternalCardID() {
        return externalCardID;
    }

    public String getClassOfServiceId() {
        return classOfServiceId;
    }

    public String getClassOfServiceTitle() {
        return classOfServiceTitle;
    }

    public String getClassOfServiceIconPath() {
        return classOfServiceIconPath;
    }

    public String getClassOfServiceColorHex() {
        return classOfServiceColorHex;
    }

    public String getDueDateText() {
        return dueDateText;
    }

    public CardType getType() {
        return type;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<AssignedUser> getAssignedUsers() {
        return assignedUsers;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTypeIconPath(String typeIconPath) {
        this.typeIconPath = typeIconPath;
    }

    public void setTypeColorHex(String typeColorHex) {
        this.typeColorHex = typeColorHex;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }

    public void setGravatarLink(String gravatarLink) {
        this.gravatarLink = gravatarLink;
    }

    public void setBlockReason(String blockReason) {
        this.blockReason = blockReason;
    }

    public void setSmallGravatarLink(String smallGravatarLink) {
        this.smallGravatarLink = smallGravatarLink;
    }

    public void setExternalSystemName(String externalSystemName) {
        this.externalSystemName = externalSystemName;
    }

    public void setExternalSystemUrl(String externalSystemUrl) {
        this.externalSystemUrl = externalSystemUrl;
    }

    public void setExternalCardID(String externalCardID) {
        this.externalCardID = externalCardID;
    }

    public void setClassOfServiceId(String classOfServiceId) {
        this.classOfServiceId = classOfServiceId;
    }

    public void setClassOfServiceTitle(String classOfServiceTitle) {
        this.classOfServiceTitle = classOfServiceTitle;
    }

    public void setClassOfServiceIconPath(String classOfServiceIconPath) {
        this.classOfServiceIconPath = classOfServiceIconPath;
    }

    public void setClassOfServiceColorHex(String classOfServiceColorHex) {
        this.classOfServiceColorHex = classOfServiceColorHex;
    }

    public void setDueDateText(String dueDateText) {
        this.dueDateText = dueDateText;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public void setAssignedUsers(List<AssignedUser> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public String getUserWipOverrideComment() {
        return userWipOverrideComment;
    }

    public void setUserWipOverrideComment(String userWipOverrideComment) {
        this.userWipOverrideComment = userWipOverrideComment;
    }

    public List<String> getAssignedUserIds() {
        return assignedUserIds;
    }

    public void setAssignedUserIds(List<String> assignedUserIds) {
        this.assignedUserIds = assignedUserIds;
    }

    @Override
    public String toString() {
        return getTitle() + "--" + getDueDateText();
    }

    public Card shallowCopy() {

        Parcel parcel = Parcel.obtain();

        this.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        Card cardCopy = Card.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        return cardCopy;
    }

}
