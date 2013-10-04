package com.appshroom.leanpocket.dto;

import java.util.List;

/**
 * Created by jpetrakovich on 8/24/13.
 */
public class CardFieldData {

    private String title;
    private String description;
    private String cardTypeId;
    private String dueDate;
    private String blockedReason;
    private String unblockedReason;
    private String tags;
    private String laneId;
    private List<BoardUser> assignedUsers;

    private int size;
    private int priority;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLaneId() {
        return laneId;
    }

    public void setLaneId(String laneId) {
        this.laneId = laneId;
    }

    public List<BoardUser> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<BoardUser> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    private boolean isBlocked;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getBlockedReason() {
        return blockedReason;
    }

    public void setBlockedReason(String blockedReason) {
        this.blockedReason = blockedReason;
    }

    public String getUnblockedReason() {
        return unblockedReason;
    }

    public void setUnblockedReason(String unblockedReason) {
        this.unblockedReason = unblockedReason;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
