package com.appshroom.leanpocket.dto;

/**
 * Created by jpetrakovich on 8/7/13.
 */
public class MoveCardToAnotherBoardReplyData {

    public String boardVersion;
    public String destinationLaneName;
    public String destinationLaneId;
    public String sourceBoardId;
    public String destinationBoardId;

    public String getBoardVersion() {
        return boardVersion;
    }

    public void setBoardVersion(String boardVersion) {
        this.boardVersion = boardVersion;
    }

    public String getDestinationLaneName() {
        return destinationLaneName;
    }

    public void setDestinationLaneName(String destinationLaneName) {
        this.destinationLaneName = destinationLaneName;
    }

    public String getDestinationLaneId() {
        return destinationLaneId;
    }

    public void setDestinationLaneId(String destinationLaneId) {
        this.destinationLaneId = destinationLaneId;
    }

    public String getSourceBoardId() {
        return sourceBoardId;
    }

    public void setSourceBoardId(String sourceBoardId) {
        this.sourceBoardId = sourceBoardId;
    }

    public String getDestinationBoardId() {
        return destinationBoardId;
    }

    public void setDestinationBoardId(String destinationBoardId) {
        this.destinationBoardId = destinationBoardId;
    }
}
