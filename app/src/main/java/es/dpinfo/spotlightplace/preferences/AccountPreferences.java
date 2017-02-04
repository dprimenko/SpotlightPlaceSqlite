package es.dpinfo.spotlightplace.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import es.dpinfo.spotlightplace.interfaces.IPreferences;
import es.dpinfo.spotlightplace.schemas.SpotlightContract;

/**
 * Created by dprimenko on 26/01/17.
 */
public class AccountPreferences implements IPreferences {

    private static AccountPreferences accountPrefences;
    private static final String ACC_PREFERENCES = "acc_pref";
    private SharedPreferences sharedPreferences;

    private AccountPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(ACC_PREFERENCES, this.MODE);
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
        return sharedPreferences.contains(SpotlightContract.UserEntry.KEY_NUMBER_PHONE);
    }

    //region SETTER


    public void putId(String id) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_ID, id).apply();
    }

    public void putNumberPhone(String nphone) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE, nphone).apply();
    }

    public void putPublicApiKey(String publicApiKey) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_PUBLIC_API_KEY, publicApiKey).apply();
    }

    public void putProfileImg(String imgUrl) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_PROFILE_IMG, imgUrl).apply();
    }

    public void putBackgroundImg(String imgUrl) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_BACKGROUND_IMG, imgUrl).apply();
    }

    public void putUsername(String username) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_USERNAME, username).apply();
    }

    public void putFullName(String fullName) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_FULLNAME, fullName).apply();
    }

    public void putEmail(String email) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_EMAIL, email).apply();
    }

    public void putTypeAcc(String typeAcc) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_TYPEACC, typeAcc).apply();
    }

    public void putLastLogin(String lastLogin) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_LAST_LOGIN, lastLogin).apply();
    }

    public void putCreatedAt(String createdAt) {
        getEditor().putString(SpotlightContract.UserEntry.KEY_CREATED_AT, createdAt).apply();
    }

    //endregion

    //region GETTER

    public String getId() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_ID, null);
    }

    public String getNumberPhone() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_NUMBER_PHONE, null);
    }

    public String getPublicApiKey() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_PUBLIC_API_KEY, null);
    }

    public String getProfileImg() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_PROFILE_IMG, null);
    }

    public String getBackgroundImg() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_BACKGROUND_IMG, null);
    }

    public String getUsername() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_USERNAME, null);
    }

    public String getFullName() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_FULLNAME, null);
    }

    public String getEmail() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_EMAIL, null);
    }

    public String getTypeAcc() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_TYPEACC, null);
    }

    public String getLastLogin() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_LAST_LOGIN, null);
    }

    public String getCreatedAt() {
        return sharedPreferences.getString(SpotlightContract.UserEntry.KEY_CREATED_AT, null);
    }

    private SharedPreferences.Editor getEditor(){
        return sharedPreferences.edit();
    }

    //endregion
}
