package com.appshroom.leanpocket.helpers;

/**
 * Created by jpetrakovich on 7/31/13.
 */
public final class Consts {


    public static final int REQUEST_CODE_NEW_CARD = 1;
    public static final int REQUEST_CODE_ADD_ACCOUNT = 2;
    public static final int REQUEST_CODE_ACCOUNT_SELECTED = 3;
    public static final int REQUEST_CODE_CARD_DETAIL = 4;
    public static final int REQUEST_CODE_EDIT_EXISTING = 5;
    public static final int REQUEST_CODE_BILLING = 6;


    public static final int RESULT_CODE_CARD_DELETE_ATTEMPTED = 4;
    public static final int RESULT_CODE_CARD_UPDATE_SUCCESS = 5;


    public static final String BUNDLE_CARD_ID = "card_id_extra";

    public static final String LEANKIT_ACCOUNT_TYPE = "com.appshroom.leankit.auth";
    public static final String LEANKIT_AUTH_TOKEN_TYPE = "basic";
    public static final String LEANKIT_VERIFY_HOST_REQUEST = "verify_host";
    public static final String LEANKIT_ADD_ACCOUNT_REQUEST = "add";

    public static final String LEANKIT_USERDATA_ORG_TEXT = "org_text";
    public static final String LEANKIT_USERDATA_ORG_HOST = "org_host";
    public static final String LEANKIT_USERDATA_EMAIL = "email";
    public static final String LEANKIT_USERDATA_GRAVATAR = "gravatar";

    public static final String LEANKIT_ACCOUNT_TO_VERIFY = "acct_to_verify";
    public static final String LEANKIT_HOST_TO_VERIFY = "host_to_verify";

    public static final String SHARED_PREFS_USER_HAS_LEARNED_DRAWER = "user_has_learned_drawer";
    public static final String SHARED_PREFS_LAST_USED_ACCOUNT = "last_used_account";
    public static final String SHARED_PREFS_LAST_USED_BOARD_ID = "last_used_board_id";

    public static final String SHARED_PREFS_ANIMATE_CARDS = "animate_cards_key";
    public static final String SHARED_PREFS_KEEP_FORMAT = "keep_format_key";
    public static final String SHARED_PREFS_CARD_POSITION = "card_position_key";
    public static final String SHARED_PREFS_AUTO_LOAD = "auto_load_key";
    public static final String SHARED_PREFS_SHOW_ARCHIVED_BOARDS = "show_archived_boards_key";

    public static final String SHARED_PREFS_IS_PREMIUM = "is_premium";
    public static final String SHARED_PREFS_K = "k";


    //The user might select an account but not select a board with it.
    public static final String SHARED_PREFS_LAST_USED_BOARD_ACCOUNT = "last_used_board_account";

    public static final String LANE_LIST_EXTRAS = "LANE_LIST_EXTRAS";

    public static final String HTTPS_URL_PREFIX = "https://";
    public static final String API_URL_SUFFIX = ".leankitkanban.com/Kanban/Api/";
    public static final String LEANKIT_LOGIN_SEARCH_URL_PREFIX = "https://login.leankit.com/Account/Membership";

    public static final String PATH_TO_BLOCKED_ICON_AFTER_HOST = ".leankitkanban.com/Content/Images/Icons/Library/blocked.png";

    public static final String GRAVATAR_URL_PREFIX = "http://www.gravatar.com/avatar/";
    public static final String GRAVATAR_URL_SUFFIX_WITH_MYSTERY_MAN_DEFAULT = "d=mm";
    public static final String GRAVATAR_URL_SIZE_PREFIX = "?s=";

    public static final String BOARD_ID_EXTRA = "boardId";
    public static final String CARD_TYPES_EXTRA = "cardTypes";
    public static final String CLASS_OF_SERVICES_EXTRA = "classOfServices";
    public static final String USES_CLASS_OF_SERVICE_EXTRA = "usesClassOfService";
    public static final String USES_CLASS_OF_SERVICE_COLOR = "usesClassOfServiceColor";
    public static final String CARD_DETAIL_CARD_EXTRA = "card";
    public static final String SHARED_PREFS_SINGLE_COL_MODE = "single_column_mode";
    public static final String ALL_CHILD_LANES_EXTRA = "all_child_lanes";
    public static final String BOARD_USERS_EXTRA = "board_users";
    public static final String EXISTING_CARD_EXTRA = "existing_card";
    public static final String DATE_FORMAT_EXTRA = "date_format";
    public static final String MOVE_CARD_DIALOG_ARGS_LANES = "lanes";
    public static final String MOVE_CARD_DIALOG_ARGS_CARD = "card";
    public static final String MOVE_CARD_DIALOG_ARGS_BOARD_ID = "boardID";
    public static final String TITLE_ARGS = "titles";
    public static final String CONTENT_ARGS = "contents";
    public static final String DIALOG_TITLE_ARG = "title";
    public static final String HELP_PAGE_URL = "http://www.appshroom.com/leanpocket/help.html";
    public static final String SKU_PREMIUM = "prem100";
    public static final String INTENT_FILTER_PREM_PURCHASE = "premium_purchase";
    public static final String COLOR_FIELD_CLASS_OF_SERVICE = "ClassOfService";
    public static final String COLOR_FIELD_CARD_TYPE = "CardType";
    public static final String BOARD_SETTINGS_EXTRA = "BoardSettings";


    public final class PRIORITY {

        public static final int LOW = 0;
        public static final int NORMAL = 1;
        public static final int HIGH = 2;
        public static final int CRITICAL = 3;
    }

    public final class REPLY_CODE {

        public static final int NO_DATA = 100;

        public static final int DATA_RETRIEVAL_SUCCESS = 200;
        public static final int DATA_INSERT_SUCCESS = 201;
        public static final int DATA_UPDATE_SUCCESS = 202;
        public static final int DATA_DELETE_SUCCESS = 203;

        public static final int SYSTEM_EXCEPTION = 500;
        public static final int MINOR_EXCEPTION = 501;
        public static final int USER_EXCEPTION = 502;
        public static final int FATAL_EXCEPTION = 503;

        public static final int THROTTLE_WAIT_RESPONSE = 800;

        public static final int WIP_OVERRIDE_COMMENT_REQUIRED = 900;
        public static final int RESENDING_EMAIL_REQUIRED = 902;

        public static final int UNAUTHORIZED_ACCESS = 1000;

    }

    public final class LANE_STATE {

        public static final String CHILD = "child";
        public static final String PARENT = "parent";
        public static final String LANE = "lane";
        public static final String CHILD_PARENT = "childParent";

    }

    public final class CARD_SYSTEM_TYPE {

        public static final String CARD = "Card";
        public static final String GHOST_CARD = "GhostCard";

    }

    private Consts() {
    }


}
