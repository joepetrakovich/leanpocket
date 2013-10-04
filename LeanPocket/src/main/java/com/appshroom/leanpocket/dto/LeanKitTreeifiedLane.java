package com.appshroom.leanpocket.dto;

import java.util.List;

/**
 * Created by jpetrakovich on 8/28/13.
 */
public class LeanKitTreeifiedLane {

    private Lane lane;
    private Lane parentLane;
    private List<LeanKitTreeifiedLane> childLanes;
    private String dom;


    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public Lane getParentLane() {
        return parentLane;
    }

    public void setParentLane(Lane parentLane) {
        this.parentLane = parentLane;
    }

    public List<LeanKitTreeifiedLane> getChildLanes() {
        return childLanes;
    }

    public void setChildLanes(List<LeanKitTreeifiedLane> childLanes) {
        this.childLanes = childLanes;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }


}
