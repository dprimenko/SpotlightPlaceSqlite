package es.dpinfo.spotlightplace.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dprimenko on 10/11/16.
 */
public class User {

    private String mId;
    private String mNumberPhone;
    private String mProfileImg;
    private String mNick;
    private String mFullName;
    private String mEmail;
    private String mTypeAcc;
    private String mApiKey;
    private List<String> mFollowers;
    private List<String> mFollowing;
    private String mLastLogin;
    private String mCreated;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmNumberPhone() {
        return mNumberPhone;
    }

    public void setmNumberPhone(String mNumberPhone) {
        this.mNumberPhone = mNumberPhone;
    }

    public String getmProfileImg() {
        return mProfileImg;
    }

    public void setmProfileImg(String mProfileImg) {
        this.mProfileImg = mProfileImg;
    }

    public String getmNick() {
        return mNick;
    }

    public void setmNick(String mNick) {
        this.mNick = mNick;
    }

    public String getmFullName() {
        return mFullName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmTypeAcc() {
        return mTypeAcc;
    }

    public void setmTypeAcc(String mTypeAcc) {
        this.mTypeAcc = mTypeAcc;
    }

    public String getmApiKey() {
        return mApiKey;
    }

    public void setmApiKey(String mApiKey) {
        this.mApiKey = mApiKey;
    }

    public List<String> getmFollowers() {
        return mFollowers;
    }

    public void setmFollowers(List<String> mFollowers) {
        this.mFollowers = mFollowers;
    }

    public List<String> getmFollowing() {
        return mFollowing;
    }

    public void setmFollowing(List<String> mFollowing) {
        this.mFollowing = mFollowing;
    }

    public String getmLastLogin() {
        return mLastLogin;
    }

    public void setmLastLogin(String mLastLogin) {
        this.mLastLogin = mLastLogin;
    }

    public String getmCreated() {
        return mCreated;
    }

    public void setmCreated(String mCreated) {
        this.mCreated = mCreated;
    }

    public User() {
        this.mId = "";
        this.mNumberPhone = "";
        this.mProfileImg = "https://developers.google.com/experts/img/user/user-default.png";
        this.mNick = "";
        this.mFullName = "";
        this.mEmail = "";
        this.mTypeAcc = "Normal";
        this.mApiKey = "";
        this.mFollowers = new ArrayList<>();
        this.mFollowing = new ArrayList<>();
        this.mLastLogin = "";
        this.mCreated = "";
    }

    public User(String mId, String mCountryCode, String mNumberPhone, String mProfileImg, String mNick, String mFullName, String mEmail, String mTypeAcc, String mApiKey, List<String> mFollowers, List<String> mFollowing, String mLastLogin, String mCreated) {
        this.mId = mId;
        this.mNumberPhone = mNumberPhone;
        this.mProfileImg = mProfileImg;
        this.mNick = mNick;
        this.mFullName = mFullName;
        this.mEmail = mEmail;
        this.mTypeAcc = mTypeAcc;
        this.mApiKey = mApiKey;
        this.mFollowers = mFollowers;
        this.mFollowing = mFollowing;
        this.mLastLogin = mLastLogin;
        this.mCreated = mCreated;
    }

    @Override
    public String toString() {
        return "{" +
                "\"number_phone\" : \"" + mNumberPhone + '"' +
                ", \"profile_img\" : \"" + mProfileImg + '"' +
                ", \"nick\" : \"" + mNick + '"' +
                ", \"full_name\" : \"" + mFullName + '"' +
                ", \"email\" : \"" + mEmail + '"' +
                ", \"type_acc\" : \"" + mTypeAcc + '"' +
                ", \"sp_api_key_id\" : \"" + mApiKey + '"' +
                ", \"last_login\" : \"" + mLastLogin + '"' +
                ", \"created\" : \"" + mCreated + '"' +
                ", \"followers\" : [" + followersToString() + "]" +
                ", \"following\" : [" + followingToString() + "]" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;

        if (o instanceof  User) {
            User user = (User) o;

            if (user.getmId() == this.getmId()) {
                result = true;
            }
        }

        return result;
    }

    public String followersToString() {
        String result = "";

        for (int i = 0; i < mFollowers.size(); i++) {
            if (i != (mFollowers.size() - 1)) {
                result += ("\"" + mFollowers.get(i) + "\"" + ",");
            } else {
                result += ("\"" + mFollowers.get(i) + "\"");
            }
        }

        return result;
    }

    public String followingToString() {
        String result = "";

        for (int i = 0; i < mFollowing.size(); i++) {
            if (i != (mFollowing.size() - 1)) {
                result += ("\"" + mFollowing.get(i) + "\"" + ",");
            } else {
                result += ("\"" + mFollowing.get(i) + "\"");
            }
        }

        return result;
    }
}
