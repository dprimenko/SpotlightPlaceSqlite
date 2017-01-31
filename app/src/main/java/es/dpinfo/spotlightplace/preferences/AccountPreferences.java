package es.dpinfo.spotlightplace.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import es.dpinfo.spotlightplace.interfaces.IPreferences;

/**
 * Created by dprimenko on 26/01/17.
 */
public class AccountPreferences implements IPreferences {

    private static AccountPreferences accountPrefences;
    private Context context;

    public static final String ID = "_id";
    public static final String NUMBER_PHONE = "number_phone";
    public static final String API_KEY = "sp_api_key_id";
    public static final String PROFILE_IMG = "profile_img";
    public static final String NICK = "nick";
    public static final String FULL_NAME = "full_name";
    public static final String EMAIL = "email";
    public static final String TYPE_ACC = "type_acc";
    public static final String LAST_LOGIN = "last_login";
    public static final String CREATED_ON = "created";
    private SharedPreferences sharedPreferences;


    private AccountPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("acc_pref", this.MODE);
    }

    public static AccountPreferences getInstance(Context context) {
        if (accountPrefences == null) {
            accountPrefences = new AccountPreferences(context);
        }
        return accountPrefences;
    }


    public void deletePrefs() {
        getEditor().clear().apply();
    }

    public boolean accountPrefsExists() {
        return sharedPreferences.contains(NUMBER_PHONE);
    }

    //region SETTER


    public void putId(String id) {
        getEditor().putString(ID, id).apply();
    }

    public void putNumberPhone(String nphone) {
        getEditor().putString(NUMBER_PHONE, nphone).apply();
    }

    public void putApiKey(String apiKey) {
        getEditor().putString(API_KEY, apiKey).apply();
    }

    public void putProfileImg(String imgUrl) {
        getEditor().putString(PROFILE_IMG, imgUrl).apply();
    }

    public void putNick(String nick) {
        getEditor().putString(NICK, nick).apply();
    }

    public void putFullName(String fullName) {
        getEditor().putString(FULL_NAME, fullName).apply();
    }

    public void putEmail(String email) {
        getEditor().putString(EMAIL, email).apply();
    }

    public void putTypeAcc(String typeAcc) {
        getEditor().putString(TYPE_ACC, typeAcc).apply();
    }

    public void putLastLogin(String lastLogin) {
        getEditor().putString(LAST_LOGIN, lastLogin).apply();
    }

    public void putCreatedOn(String createdOn) {
        getEditor().putString(CREATED_ON, createdOn).apply();
    }

    //endregion

    //region GETTER

    public String getId() {
        return sharedPreferences.getString(ID, null);
    }

    public String getNumberPhone() {
        return sharedPreferences.getString(NUMBER_PHONE, null);
    }

    public String getApiKey() {
        return sharedPreferences.getString(API_KEY, null);
    }

    public String getProfileImg() {
        return sharedPreferences.getString(PROFILE_IMG, null);
    }

    public String getNick() {
        return sharedPreferences.getString(NICK, null);
    }

    public String getFullName() {
        return sharedPreferences.getString(FULL_NAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public String getTypeAcc() {
        return sharedPreferences.getString(TYPE_ACC, null);
    }

    public String getLastLogin() {
        return sharedPreferences.getString(LAST_LOGIN, null);
    }

    public String getCreatedOn() {
        return sharedPreferences.getString(CREATED_ON, null);
    }

    private SharedPreferences.Editor getEditor(){
        return sharedPreferences.edit();
    }

    //endregion
}
