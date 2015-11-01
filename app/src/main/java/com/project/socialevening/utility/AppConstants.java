package com.project.socialevening.utility;

public interface AppConstants {

    String API_DOMAIN = "http://128.199.67.239";
    public static final String API_PREFIX = "http://128.199.67.239:3000/";
    public static final String API_PREFIX2 = "http://128.199.67.239:3001/";

    public static final String DEVICE_ID = "device_id";

    String SUB_CATEGORY_ID = "subcategory_id";
    String MERCHANT_ID = "merchant_id";

    String ZONE_ID = "zone_id";
    String CATEGORY_ID = "category_id";

    String PARSE_APP_ID = "t5e3EmTY3TxXVEYa2iDSE4cBtSI4UliMlygphyY2";
    String PARSE_CLIENT_ID = "FxXZFSlCCW4UmbIUZljxawZbJSDZRScW9hBa29Kw";
    String PARSE_REST_ID = "sSLcaJLv5gPTvbz73ryCNztPkRgwkxM9Fl8yWGmQ";
    String APP_LINK = "https://drive.google.com/file/d/0B8wKJnD6sONHeXlUbm5pOTk4dGM/view?usp=sharing";

    String APP_DOWNLOAD_MESSAGE = "Download social evening to grow your social network. Download it from ";

    public static interface PARAMS {
        String TEAM = "TEAM";
        String TEAM_NAME = "teamName";
        String TEAM_IMAGE = "teamImage";
        String USER = "user";
        String CHALLENGE_NAME = "challengeName";
        String MEMBERS = "members";
        String OBJECT_ID = "objectId";
        String MEMBER_ID = "memberId";
        String ALERT = "alert";
        String CHALLENGE_COUNT = "challengeCount";
        java.lang.String MEMBER_COUNT = "memberCount";
        String LAT = "latitude";
        String LNG = "longitude";;
        String LOCATION = "location";
        java.lang.String CHALLENGES_SENT = "challengesSent";
    }

    public static interface LOADER_CODES {

        public static final int CART_OBSERVER = 101;

    }


    public static interface ERROR_CODES {

        public static final int UNKNOWN_ERROR = 0;
        public static final int NO_INTERNET_ERROR = 1;
        public static final int NETWORK_SLOW_ERROR = 2;
        public static final int URL_INVALID = 3;
        public static final int DEVELOPMENT_ERROR = 4;

    }

    public static interface PAGE_URL {
        String MERCHANT = "merchant";
        String MASTER_LIST = "masterList";
        String CITIES = "cities";
        String CATEGORY = "category";
        String ITEMS = "items";
        String VERIFY_MOBILE = "account/verify";
        String VERIFY_OTP = "account/verifyotp";
        String USER_ADDRESS = "user/address";
        String ORDER = "order";
        String USER_ORDERS = "user/orders";
    }

    public static interface TASK_CODES {

        int MASTER_LIST = 1;
        int CITIES = 2;
        int CATEGORY = 3;
        int ITEMS = 4;
        int VERIFY_MOBILE = 5;
        int VERIFY_OTP = 6;
        int USER_ADDRESS = 7;
        int MERCHANT = 8;
        int ORDER = 9;
        int USER_ORDERS = 10;
        int ORDER_ID = 11;
        int TEST_API = 12;

        int SAVE_PARSE_FILE = 13;
        int SAVE_PARSE_OBJECT = 14;
        int PARSE_QUERY = 15;
        int GET_TEAM_MEMBERS = 16;
    }

    public static interface PREF_KEYS {

        String KEY_LOGIN = "IsUserLoggedIn";
        String KEY_USERNAME = "username";
        String KEY_PASSWORD = "password";
        String KEY_USER_ADDRESS = "userAddress";
        String KEY_CITY_ID = "KEY_CITY_ID";
        String KEY_CITY_NAME = "KEY_CITY_NAME";
        String KEY_LOC_ID = "KEY_LOC_ID";
        String KEY_LOC_NAME = "KEY_LOC_NAME";
        String KEY_LOC_ENTERED = "KEY_LOC_ENTERED";
        String KEY_IS_USER_VERIFIED = "KEY_IS_USER_VERIFIED";
        String KEY_AUTH_TOKEN = "auth_token";
        String MOBILE_NUMBER = "mobile_no";
        String KEY_ZONE_ID = "KEY_ZONE_ID";

        String ACCESS_CODE = "access";
        String USER_ID = "userId";
        String IS_LOGIN_SKIPPED = "isLoginSkipped";
        String SOCIAL_LOGIN = "socialLogin";
        String SUBMITTED_PROJECT_TIMESTAMP = "submittedProjectTimestamp";
        String CHALLENGE_COUNT = "challengeCount";
        String APP_LINK = "appLink";
        String IS_APP_LINK_SAVED = "isAppLinkSaved";
    }

    public static interface BUNDLE_KEYS {
        public static final String KEY_SERIALIZABLE_OBJECT = "KEY_SERIALIZABLE_OBJECT";
        public static final String KEY_ROOM_NAME = "KEY_ROOM_NAME";
        public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
        public static final String ORDER_ID = "order_id";
        public static String KEY_EXAM_DETAIL = "keyExamDetail";
        public static String KEY_NEWS_ARTICLE = "keyNewsArticle";
        public static String KEY_CATAGORY_NAME = "KeyCategory";
        public static String KEY_ARG_POSITION = "KEY_ARG_POSITION";
        String SELECTED_COLOR = "SelectedColor";
        String SELECTED_BG = "SelectedBackground";
        String CHILD_CATEGORY = "childCategory";
        String COLOR_CODES = "ColorCodes";
        String SELECTED_COLOR_CODE_POSITION = "SelectedColorCodePosition";
        String GUIDE = "guide";
        String ATTRIBUTE = "attribute";
        String PHONE_NUMBER = "mobileNumber";
        String JSON_STRING = "jsonString";
        String GUIDED_ID = "guidedid";
        String PROJECT_ID = "projectid";
        java.lang.String BG_COLOR = "bgColor";
        String KEY_IMAGE_ARRAY = "imageList";
        String CATEGORY_ID = "categoryId";
        java.lang.String TEAM_ID = "teamID";
        String EXTRA_BUNDLE = "bundle";
    }

    public static interface FRAGMENT_TYPE {
        int HOME_FRAGMENT = 0;
        int TEAMS_FRAGMENT = 1;
        int ADD_TEAM_MATE = 2;
        int MY_TEAMS = 3;
    }

    public static interface VIEW_TYPE {
        int CARD_MY_TEAM = 0;
    }

    public static interface CHALLENGES{
        String FACEBOOK = "Facebook Challenge";
        String INSTAGRAM = "Instagram Challenge";
        String WHATSAPP = "Whatsapp Challenge";
    }

}
