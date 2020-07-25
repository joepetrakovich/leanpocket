package com.appshroom.leanpocket.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jpetrakovich on 8/2/13.
 */
public class Board {

    private String id;
    private String title;
    private String description;
    private String version;
    private String organizationId;
    private String format;
    private String prefix;
    private String cardColorField;
    private String currentUserRole;
    private String availableTags;

    private String backlogTopLevelLaneId;
    private String archiveTopLevelLaneId;

    private String dateFormat;

    private List<OrganizationActivity> organizationActivities;

    private List<BoardUser> users;

    private List<ClassOfService> classesOfService;

    private List<CardType> cardTypes;

    private List<Card> cardsInFlight;
    private List<Card> cardsInBacklog;
    private List<Card> cardsInArchive;

    private List<Lane> lanes;
    private List<Lane> swimLanes;
    private List<Lane> backlog;
    private List<Lane> archive;

    private List<Lane> treeifiedLanes;
    private Lane treeifiedBacklog;
    private Lane treeifiedArchive;

    private List<Lane> orderedInFlightChildLanes;
    private List<Lane> orderedBacklogChildLanes;
    private List<Lane> orderedArchiveChildLanes;

    private ArrayList<Lane> allOrderedChildLanes;

    private ArrayList<LaneDescription> allChildLaneNames;

    private List<Card> cardsAssignedToAppUser;

    private HashMap<String, Integer> cardColorMap;
    private HashMap<Integer, Integer> cardAccentColorMap;
    private HashMap<String, String> userGravatarUrlMap;

    private List<String> topLevelLaneIds;

    private boolean isActive;
    private boolean classOfServiceEnabled;
    private boolean isCardIdEnabled;
    private boolean isHeaderEnabled;
    private boolean isPrefixEnabled;
    private boolean isPrefixIncludedInHyperlink;
    private boolean isHyperlinkEnabled;
    private boolean isAutoIncrementCardIdEnabled;

    private boolean hasLoadedArchive;

    private BoardSettings settings;

    public List<Card> getCardsAssignedToAppUser() {
        return cardsAssignedToAppUser;
    }

    public void setCardsAssignedToAppUser(List<Card> cardsAssignedToAppUser) {
        this.cardsAssignedToAppUser = cardsAssignedToAppUser;
    }

    public boolean isAutoIncrementCardIdEnabled() {
        return isAutoIncrementCardIdEnabled;
    }

    public BoardSettings getSettings() {
        return settings;
    }

    public void setSettings(BoardSettings settings) {
        this.settings = settings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCardColorField() {
        return cardColorField;
    }

    public void setCardColorField(String cardColorField) {
        this.cardColorField = cardColorField;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }

    public void setCurrentUserRole(String currentUserRole) {
        this.currentUserRole = currentUserRole;
    }

    public String getAvailableTags() {
        return availableTags;
    }

    public void setAvailableTags(String availableTags) {
        this.availableTags = availableTags;
    }

    public List<OrganizationActivity> getOrganizationActivities() {
        return organizationActivities;
    }

    public void setOrganizationActivities(List<OrganizationActivity> organizationActivities) {
        this.organizationActivities = organizationActivities;
    }

    public List<BoardUser> getUsers() {
        return users;
    }

    public void setUsers(List<BoardUser> users) {
        this.users = users;
    }

    public List<ClassOfService> getClassesOfService() {
        return classesOfService;
    }

    public void setClassesOfService(List<ClassOfService> classesOfService) {
        this.classesOfService = classesOfService;
    }

    public List<CardType> getCardTypes() {
        return cardTypes;
    }

    public void setCardTypes(List<CardType> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    public List<Lane> getSwimLanes() {
        return swimLanes;
    }

    public void setSwimLanes(List<Lane> swimLanes) {
        this.swimLanes = swimLanes;
    }

    public List<Lane> getBacklog() {
        return backlog;
    }

    public void setBacklog(List<Lane> backlog) {
        this.backlog = backlog;
    }

    public List<Lane> getArchive() {
        return archive;
    }

    public void setArchive(List<Lane> archive) {
        this.archive = archive;
    }

    public List<String> getTopLevelLaneIds(String laneClassType) {
        List<String> topLevelLaneIds = new ArrayList<>();

        for (Lane lane : lanes) {
            if (lane.getParentLaneId() == null && lane.getLaneClassType().equals(laneClassType)) {
                topLevelLaneIds.add(lane.getId());
            }
        }

        return topLevelLaneIds;
    }

    public void setTopLevelLaneIds(List<String> topLevelLaneIds) {
        this.topLevelLaneIds = topLevelLaneIds;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isClassOfServiceEnabled() {
        return classOfServiceEnabled;
    }

    public void setClassOfServiceEnabled(boolean classOfServiceEnabled) {
        this.classOfServiceEnabled = classOfServiceEnabled;
    }

    public boolean isCardIdEnabled() {
        return isCardIdEnabled;
    }

    public void setCardIdEnabled(boolean cardIdEnabled) {
        isCardIdEnabled = cardIdEnabled;
    }

    public boolean isHeaderEnabled() {
        return isHeaderEnabled;
    }

    public void setHeaderEnabled(boolean headerEnabled) {
        isHeaderEnabled = headerEnabled;
    }

    public boolean isPrefixEnabled() {
        return isPrefixEnabled;
    }

    public void setPrefixEnabled(boolean prefixEnabled) {
        isPrefixEnabled = prefixEnabled;
    }

    public boolean isPrefixIncludedInHyperlink() {
        return isPrefixIncludedInHyperlink;
    }

    public void setPrefixIncludedInHyperlink(boolean prefixIncludedInHyperlink) {
        isPrefixIncludedInHyperlink = prefixIncludedInHyperlink;
    }

    public boolean isHyperlinkEnabled() {
        return isHyperlinkEnabled;
    }

    public void setHyperlinkEnabled(boolean hyperlinkEnabled) {
        isHyperlinkEnabled = hyperlinkEnabled;
    }

    public List<Lane> getTreeifiedLanes() {
        return treeifiedLanes;
    }

    public void setTreeifiedLanes(List<Lane> treeifiedLanes) {
        this.treeifiedLanes = treeifiedLanes;
    }

    public Lane getTreeifiedBacklog() {
        return treeifiedBacklog;
    }

    public void setTreeifiedBacklog(Lane treeifiedBacklog) {
        this.treeifiedBacklog = treeifiedBacklog;
    }

    public Lane getTreeifiedArchive() {
        return treeifiedArchive;
    }

    public void setTreeifiedArchive(Lane treeifiedArchive) {
        this.treeifiedArchive = treeifiedArchive;
    }

    public List<Card> getCardsInFlight() {
        return cardsInFlight;
    }

    public void setCardsInFlight(List<Card> cardsInFlight) {
        this.cardsInFlight = cardsInFlight;
    }

    public List<Card> getCardsInBacklog() {
        return cardsInBacklog;
    }

    public void setCardsInBacklog(List<Card> cardsInBacklog) {
        this.cardsInBacklog = cardsInBacklog;
    }

    public List<Card> getCardsInArchive() {
        return cardsInArchive;
    }

    public void setCardsInArchive(List<Card> cardsInArchive) {
        this.cardsInArchive = cardsInArchive;
    }

    public List<Lane> getOrderedInFlightChildLanes() {
        return orderedInFlightChildLanes;
    }

    public void setOrderedInFlightChildLanes(List<Lane> orderedInFlightChildLanes) {
        this.orderedInFlightChildLanes = orderedInFlightChildLanes;
    }

    public List<Lane> getOrderedBacklogChildLanes() {
        return orderedBacklogChildLanes;
    }

    public void setOrderedBacklogChildLanes(List<Lane> orderedBacklogChildLanes) {
        this.orderedBacklogChildLanes = orderedBacklogChildLanes;
    }

    public List<Lane> getOrderedArchiveChildLanes() {
        return orderedArchiveChildLanes;
    }

    public void setOrderedArchiveChildLanes(List<Lane> orderedArchiveChildLanes) {
        this.orderedArchiveChildLanes = orderedArchiveChildLanes;
    }

    public boolean archiveNotLoaded() {
        return !hasLoadedArchive;
    }

    public void setHasLoadedArchive(boolean hasLoadedArchive) {
        this.hasLoadedArchive = hasLoadedArchive;
    }

    public HashMap<String, Integer> getCardColorMap() {
        return cardColorMap;
    }

    public void setCardColorMap(HashMap<String, Integer> cardColorMap) {
        this.cardColorMap = cardColorMap;
    }

    public HashMap<Integer, Integer> getCardAccentColorMap() {
        return cardAccentColorMap;
    }

    public void setCardAccentColorMap(HashMap<Integer, Integer> cardAccentColorMap) {
        this.cardAccentColorMap = cardAccentColorMap;
    }

    public HashMap<String, String> getUserGravatarUrlMap() {
        return userGravatarUrlMap;
    }

    public void setUserGravatarUrlMap(HashMap<String, String> userGravatarUrlMap) {
        this.userGravatarUrlMap = userGravatarUrlMap;
    }

    public ArrayList<Lane> getAllOrderedChildLanes() {
        return allOrderedChildLanes;
    }

    public void setAllOrderedChildLanes(ArrayList<Lane> allOrderedChildLanes) {
        this.allOrderedChildLanes = allOrderedChildLanes;
    }

    public ArrayList<LaneDescription> getAllChildLaneNames(){
        return allChildLaneNames;
    }
    public void setAllChildLaneNames(ArrayList<Lane> allOrderedChildLanes) {

        ArrayList<LaneDescription> allNames = new ArrayList<LaneDescription>();

        for (Lane laneHeavy: allOrderedChildLanes){
            LaneDescription laneLite = new LaneDescription();
            laneLite.setName(laneHeavy.getName());
            laneLite.setId(laneHeavy.getId());
            laneLite.setBoardSectionType(laneHeavy.getBoardSectionType());
            allNames.add(laneLite);
        }

        this.allChildLaneNames = allNames;
    }
}

