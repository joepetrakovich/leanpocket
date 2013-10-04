package com.appshroom.leanpocket.dto;

import java.util.List;

/**
 * Created by jpetrakovich on 8/7/13.
 */
public class CardHistory {

    public String cardId;
    public String cardTitle;
    public String toLaneId;
    public String toLaneTitle;
    public String type;
    public String userName;
    public String userFullName;
    public String dateTime;
    public String gravatarLink;
    public String timeDifference;
    public String lastDate;
    public String overrideType;
    public String taskboardContainingCardTitle;
    public String taskboardContainingCardId;
    public String commentText;
    public String fromLaneId;
    public String fromLaneTitle;
    public String comment;
    public boolean isBlocked;
    private List<Change> changes;
    public String assignedUserId;
    public String assignedUserFullName;
    public String assignedUserEmailAddres;
    public boolean isUnassigning;
    public String userToOverrideWipId;
    public String userToOverrideWipName;
    public String wipOverrideComment;
    public String userToOverrideWipEmail;
    public String laneToOverrideWipTitle;
    public String laneToOverrideWipId;
    public boolean isDelete;
    public String fileName;

    @Override
    public String toString() {
        return getType();
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getToLaneId() {
        return toLaneId;
    }

    public void setToLaneId(String toLaneId) {
        this.toLaneId = toLaneId;
    }

    public String getToLaneTitle() {
        return toLaneTitle;
    }

    public void setToLaneTitle(String toLaneTitle) {
        this.toLaneTitle = toLaneTitle;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getGravatarLink() {
        return gravatarLink;
    }

    public void setGravatarLink(String gravatarLink) {
        this.gravatarLink = gravatarLink;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getOverrideType() {
        return overrideType;
    }

    public void setOverrideType(String overrideType) {
        this.overrideType = overrideType;
    }

    public String getTaskboardContainingCardTitle() {
        return taskboardContainingCardTitle;
    }

    public void setTaskboardContainingCardTitle(String taskboardContainingCardTitle) {
        this.taskboardContainingCardTitle = taskboardContainingCardTitle;
    }

    public String getTaskboardContainingCardId() {
        return taskboardContainingCardId;
    }

    public void setTaskboardContainingCardId(String taskboardContainingCardId) {
        this.taskboardContainingCardId = taskboardContainingCardId;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(String timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getFromLaneId() {
        return fromLaneId;
    }

    public void setFromLaneId(String fromLaneId) {
        this.fromLaneId = fromLaneId;
    }

    public String getFromLaneTitle() {
        return fromLaneTitle;
    }

    public void setFromLaneTitle(String fromLaneTitle) {
        this.fromLaneTitle = fromLaneTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }


    public String getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(String assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public String getAssignedUserFullName() {
        return assignedUserFullName;
    }

    public void setAssignedUserFullName(String assignedUserFullName) {
        this.assignedUserFullName = assignedUserFullName;
    }

    public String getAssignedUserEmailAddres() {
        return assignedUserEmailAddres;
    }

    public void setAssignedUserEmailAddres(String assignedUserEmailAddres) {
        this.assignedUserEmailAddres = assignedUserEmailAddres;
    }

    public boolean isUnassigning() {
        return isUnassigning;
    }

    public void setUnassigning(boolean unassigning) {
        isUnassigning = unassigning;
    }

    public String getUserToOverrideWipId() {
        return userToOverrideWipId;
    }

    public void setUserToOverrideWipId(String userToOverrideWipId) {
        this.userToOverrideWipId = userToOverrideWipId;
    }

    public String getUserToOverrideWipName() {
        return userToOverrideWipName;
    }

    public void setUserToOverrideWipName(String userToOverrideWipName) {
        this.userToOverrideWipName = userToOverrideWipName;
    }

    public String getWipOverrideComment() {
        return wipOverrideComment;
    }

    public void setWipOverrideComment(String wipOverrideComment) {
        this.wipOverrideComment = wipOverrideComment;
    }

    public String getUserToOverrideWipEmail() {
        return userToOverrideWipEmail;
    }

    public void setUserToOverrideWipEmail(String userToOverrideWipEmail) {
        this.userToOverrideWipEmail = userToOverrideWipEmail;
    }

    public String getLaneToOverrideWipTitle() {
        return laneToOverrideWipTitle;
    }

    public void setLaneToOverrideWipTitle(String laneToOverrideWipTitle) {
        this.laneToOverrideWipTitle = laneToOverrideWipTitle;
    }

    public String getLaneToOverrideWipId() {
        return laneToOverrideWipId;
    }

    public void setLaneToOverrideWipId(String laneToOverrideWipId) {
        this.laneToOverrideWipId = laneToOverrideWipId;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
