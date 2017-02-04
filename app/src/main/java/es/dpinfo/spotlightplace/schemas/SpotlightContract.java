package es.dpinfo.spotlightplace.schemas;

/**
 * Created by dprimenko on 3/02/17.
 */
public final class SpotlightContract {

    private SpotlightContract() {}

    public static class PlaceEntry {
        public static final String COLLECTION_NAME = "places";

        public static final String KEY_ID = "_id";
        public static final String KEY_CREATOR = "creator";
        public static final String KEY_TITLE = "title";
        public static final String KEY_IMG = "img";
        public static final String KEY_ADDRESS = "address";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_CATEGORY = "category";
        public static final String KEY_DATETIMEFROM = "datetime_from";
        public static final String KEY_DATETIMETO = "datetime_to";
        public static final String KEY_USERSIN = "users_in";

        public static final String CATEGORY_DISCO = "disco";
        public static final String CATEGORY_BAR = "bar";
        public static final String CATEGORY_FESTIVAL = "festival";
        public static final String CATEGORY_CONCERT = "concert";
        public static final String CATEGORY_SPORTS = "sports";
        public static final String CATEGORY_PUBLIC = "public";
        public static final String CATEGORY_TECHNOLOGY = "technology";
        public static final String CATEGORY_GASTRONOMY = "gastronomy";

    }

    public static class UserEntry {
        public static final String COLLECTION_NAME = "users";

        public static final String KEY_ID = "_id";
        public static final String KEY_NUMBER_PHONE = "number_phone";
        public static final String KEY_PROFILE_IMG = "profile_img";
        public static final String KEY_BACKGROUND_IMG = "background_img";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_FULLNAME = "full_name";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_TYPEACC = "type_acc";
        public static final String KEY_PUBLIC_API_KEY = "public_api_key";
        public static final String KEY_LAST_LOGIN = "last_login";
        public static final String KEY_CREATED_AT = "created_at";
        public static final String KEY_FOLLOWERS = "followers";
        public static final String KEY_FOLLOWING = "following";

        public static final String TYPE_ACC_NORMAL = "normal";
        public static final String TYPE_ACC_BUSINESS = "business";
        public static final String TYPE_ACC_MOD = "mod";
        public static final String TYPE_ACC_ADMIN = "admin";
    }
}
