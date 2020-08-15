package com.appshroom.leanpocket.dto.v2;

import java.util.List;

public class CreateCardRequest {
    public String boardId;
    public String title;
    public String typeId;
    public String description;
    public float size;
    public String laneId;
    public String copiedFromCardId;
    public String blockReason;
    public String priority;
    public String customIconId;
    public String customId;
    public ExternalLink externalLink;
    public float index;
    public String plannedStart;
    public String plannedFinish;
    public List<String> tags;
    public String wipOverrideComment;
    public List<String> assignedUserIds;
}
