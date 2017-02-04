package es.dpinfo.spotlightplace.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dprimenko on 10/11/16.
 */
public class User {

    public static final String NUMBER_PHONE_STR = "number_phone";

    private String mId;
    private String mNumberPhone;
    private String mProfileImg;
    private String mBackgoundImg;
    private String mUsername;
    private String mFullName;
    private String mEmail;
    private String mTypeAcc;
    private String mPublicApiKey;
    private List<String> mFollowers;
    private List<String> mFollowing;
    private String mLastLogin;
    private String mCreatedAt;

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

    public String getmBackgoundImg() {
        return mBackgoundImg;
    }

    public void setmBackgoundImg(String mBackgoundImg) {
        this.mBackgoundImg = mBackgoundImg;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
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

    public String getmPublicApiKey() {
        return mPublicApiKey;
    }

    public void setmPublicApiKey(String mPublicApiKey) {
        this.mPublicApiKey = mPublicApiKey;
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

    public String getmCreatedAt() {
        return mCreatedAt;
    }

    public void setmCreatedAt(String mCreatedAt) {
        this.mCreatedAt = mCreatedAt;
    }

    public User() {
        this.mId = "";
        this.mNumberPhone = "";
        this.mProfileImg = "https://developers.google.com/experts/img/user/user-default.png";
        this.mBackgoundImg = "http://the8guild.com/themes/html/appica2/android/img/intro/intro-bg.png";
        this.mUsername = "";
        this.mFullName = "";
        this.mEmail = "";
        this.mTypeAcc = "normal";
        this.mPublicApiKey = "";
        this.mFollowers = new ArrayList<>();
        this.mFollowing = new ArrayList<>();
        this.mLastLogin = "";
        this.mCreatedAt = "";
    }

    public User(String mId, String mNumberPhone, String mProfileImg, String mBackgoundImg,String mUsername, String mFullName, String mEmail, String mTypeAcc, String mPublicApiKey, List<String> mFollowers, List<String> mFollowing, String mLastLogin, String mCreatedAt) {
        this.mId = mId;
        this.mNumberPhone = mNumberPhone;
        this.mProfileImg = mProfileImg;
        this.mBackgoundImg = mBackgoundImg;
        this.mUsername = mUsername;
        this.mFullName = mFullName;
        this.mEmail = mEmail;
        this.mTypeAcc = mTypeAcc;
        this.mPublicApiKey = mPublicApiKey;
        this.mFollowers = mFollowers;
        this.mFollowing = mFollowing;
        this.mLastLogin = mLastLogin;
        this.mCreatedAt = mCreatedAt;
    }

    @Override
    public String toString() {
        return "{" +
                "\"number_phone\" : \"" + mNumberPhone + '"' +
                ", \"profile_img\" : \"" + mProfileImg + '"' +
                ", \"background_img\" : \"" + mBackgoundImg + '"' +
                ", \"username\" : \"" + mUsername + '"' +
                ", \"full_name\" : \"" + mFullName + '"' +
                ", \"email\" : \"" + mEmail + '"' +
                ", \"type_acc\" : \"" + mTypeAcc + '"' +
                ", \"sp_api_key_id\" : \"" + mPublicApiKey + '"' +
                ", \"last_login\" : \"" + mLastLogin + '"' +
                ", \"created\" : \"" + mCreatedAt + '"' +
                ", \"followers\" : [" + followersToString() + "]" +
                ", \"following\" : [" + followingToString() + "]" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;

        if (o instanceof  User) {
            User user = (User) o;

            if (user.getmId().equals(this.getmId())) {
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
