package com.appshroom.leanpocket.helpers;

import com.appshroom.leanpocket.dto.AssignedUser;
import com.appshroom.leanpocket.dto.Board;
import com.appshroom.leanpocket.dto.Card;
import com.appshroom.leanpocket.dto.Identifier;
import com.appshroom.leanpocket.dto.Lane;
import com.appshroom.leanpocket.dto.LeanKitTreeifiedLane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by jpetrakovich on 8/8/13.
 */
public class BoardHelpers {

    private BoardHelpers() {
    }

    /**
     * Gets all the cards extracted from each lane provided.
     *
     * @param lanes The lanes to retrieve the cards from.
     * @return The list of all aggregated cards.
     */
    public static List<Card> getAllCards(List<Lane> lanes) {

        List<Card> cards = new ArrayList<Card>();

        for (Lane lane : lanes) {

            String laneState = lane.getLaneState();

            //only regular lanes or child lanes have cards.

            if (laneState.equals(Consts.LANE_STATE.PARENT) || laneState.equals(Consts.LANE_STATE.CHILD_PARENT)) {

                continue;
            }

            cards.addAll(lane.getCards());

        }

        return cards;

    }

    /**
     * This method transforms the flat structure of the board coming from the RetroLeanKitApi API into a nested structure.
     * Lanes, including the backlog and archive, will contain a list of their child lanes.  We do this for navigation
     * in the app.  A user will be able to navigate down the lane structure quickly.
     *
     * @param board The flat board, recently inflated from RetroLeanKitApi's JSON version.
     */
    public static void treeifyBoard(Board board) {

        //Treeify the main board lanes

        List<Lane> allBoardLanes = new ArrayList<Lane>(board.getLanes());

        List<Lane> topLevelLanes = getLanesByIds(board.getTopLevelLaneIds(), allBoardLanes);

        board.setTreeifiedLanes(treeifyLanes(topLevelLanes, allBoardLanes));

        //Treeify the backlog

        List<Lane> flattenedBacklog = new ArrayList<Lane>(board.getBacklog());

        Lane backlogTopLane = getLaneById(board.getBacklogTopLevelLaneId(), flattenedBacklog);

        board.setTreeifiedBacklog(treeifyLane(backlogTopLane, flattenedBacklog));

        //Treeify the archive

        List<Lane> flattenedArchive = new ArrayList<Lane>(board.getArchive());

        Lane archiveTopLane = getLaneById(board.getArchiveTopLevelLaneId(), flattenedArchive);

        board.setTreeifiedArchive(treeifyLane(archiveTopLane, flattenedArchive));


    }

    /**
     * Gets all the lanes that are capable of holding cards (lane state must be 'child' or 'lane')
     *
     * @param availableLanes the lanes to sift through.
     * @return a list containing only child and regular lanes, in the order found.
     */
    public static List<Lane> getCardHoldableLanes(List<Lane> availableLanes) {

        List<Lane> cardHoldingLanes = new ArrayList<Lane>();

        for (Lane lane : availableLanes) {

            String state = lane.getLaneState();

            if (state.equals(Consts.LANE_STATE.LANE) || state.equals(Consts.LANE_STATE.CHILD)) {

                cardHoldingLanes.add(lane);
            }
        }

        return cardHoldingLanes;

    }

    public static List<Lane> getCardHoldableLanesInOrderFromArchive(LeanKitTreeifiedLane archiveLane) {

        List<Lane> cardHoldableLanes = new ArrayList<Lane>();
        Stack<LeanKitTreeifiedLane> unexploredLanes = new Stack<LeanKitTreeifiedLane>();


        unexploredLanes.push(archiveLane);

        while (unexploredLanes.size() > 0) {

            LeanKitTreeifiedLane lane = unexploredLanes.pop();

            String laneState = lane.getLane().getLaneState();

            if (laneState.equals(Consts.LANE_STATE.LANE) || laneState.equals(Consts.LANE_STATE.CHILD)) {

                cardHoldableLanes.add(lane.getLane());
            } else {

                List<LeanKitTreeifiedLane> childLanes = lane.getChildLanes();
                Collections.reverse(childLanes); //
                unexploredLanes.addAll(childLanes);
            }
        }


        return cardHoldableLanes;

    }

    public static List<Lane> getCardHoldableLanesInOrder(List<Lane> topLevelLanes) {

        List<Lane> cardHoldableLanes = new ArrayList<Lane>();
        Stack<Lane> unexploredLanes = new Stack<Lane>();

        for (Lane topLevelLane : topLevelLanes) {

            unexploredLanes.push(topLevelLane);

            while (unexploredLanes.size() > 0) {

                Lane lane = unexploredLanes.pop();
                String laneState = lane.getLaneState();

                if (laneState.equals(Consts.LANE_STATE.LANE) || laneState.equals(Consts.LANE_STATE.CHILD)) {

                    cardHoldableLanes.add(lane);
                } else {

                    List<Lane> childLanes = lane.getChildLanes();
                    Collections.reverse(childLanes); //
                    unexploredLanes.addAll(childLanes);
                }
            }
        }

        return cardHoldableLanes;

    }

    public static void applyContextualLaneNamesToArchive(String parentName, List<LeanKitTreeifiedLane> siblingLanes) {

        if (siblingLanes != null) {

            for (LeanKitTreeifiedLane lane : siblingLanes) {

                String contextualName = parentName + lane.getLane().getTitle();

                lane.getLane().setTitle(contextualName);

                applyContextualLaneNamesToArchive(contextualName + " - ", lane.getChildLanes());

            }
        }
    }

    public static void applyContextualLaneNames(String parentName, List<Lane> siblingLanes) {

        if (siblingLanes != null) {

            for (Lane lane : siblingLanes) {

                String contextualName = parentName + lane.getTitle();

                lane.setTitle(contextualName);

                applyContextualLaneNames(contextualName + " - ", lane.getChildLanes());

            }
        }

    }

    public static Lane getLaneById(String id, List<Lane> availableLanes) {

        for (Lane lane : availableLanes) {

            if (id.equals(lane.getId())) {

                return lane;
            }
        }

        return null;

    }


    public static List<Card> getCardsAssignedToUser(String userName, List<Card> cards){

        List<Card> cardsAssignedToUser = new ArrayList<Card>();

        for (Card card : cards){

            for (AssignedUser assignedUser : card.getAssignedUsers()){

                if (userName.equals(assignedUser.getEmailAddress())){
                    cardsAssignedToUser.add(card);
                    break;
                }
            }
        }

        return cardsAssignedToUser;
    }

    private static String getIdentifierNameById(String id, List<Identifier> identifiers) {

        String name = "";


        for (Identifier identity : identifiers) {

            if (identity.getId().equals(id)) {

                name = identity.getName();
                break;
            }
        }

        return name;

    }

    private static Lane treeifyLane(Lane lane, List<Lane> flattenedLanes) {

        String laneState = lane.getLaneState();

        if (laneState.equals(Consts.LANE_STATE.CHILD) || laneState.equals(Consts.LANE_STATE.LANE)) {

            //this is where I would gather cards...

            return lane;
        }

        lane.setChildLanes(getLanesByIds(lane.getChildLaneIds(), flattenedLanes));

        treeifyLanes(lane.getChildLanes(), flattenedLanes);

        return lane;

    }

    private static List<Lane> treeifyLanes(List<Lane> siblingLanes, List<Lane> flattenedLanes) {

        List<Lane> treeifiedLanes = new ArrayList<Lane>();

        for (Lane lane : siblingLanes) {

            treeifiedLanes.add(treeifyLane(lane, flattenedLanes));

        }

        return treeifiedLanes;
    }

    private static List<Lane> getLanesByIds(List<String> ids, List<Lane> availableLanes) {

        List<Lane> matchedLanes = new ArrayList<Lane>();

        for (String id : ids) {

            Lane match = getLaneById(id, availableLanes);

            if (match != null) {

                matchedLanes.add(match);

                availableLanes.remove(match);
            }

        }

        return matchedLanes;

    }


}
