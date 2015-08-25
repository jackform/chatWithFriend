package org.jackform.innocent.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jackform on 15-7-16.
 */
public class FriendInfo {

    @SerializedName(APPConstant.KEY_USERNAME)
    String mUserName;

    @SerializedName(APPConstant.KEY_JABBERID)
    String mJabberID;

    @SerializedName(APPConstant.KEY_HEADER_IMAGE_PATH)
    String mHeaderImagePath;

    @SerializedName(APPConstant.KEY_MALE)
    String mMale;

    @SerializedName(APPConstant.KEY_AGE)
    String mAge;

    public String getmMale() {
        return mMale;
    }

    public void setmMale(String mMale) {
        this.mMale = mMale;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmJabberID() {
        return mJabberID;
    }

    public void setmJabberID(String mJabberID) {

        this.mJabberID = mJabberID;
    }

    public String getmHeaderImagePath() {
        return mHeaderImagePath;
    }

    public void setmHeaderImagePath(String mHeaderImagePath) {
        this.mHeaderImagePath = mHeaderImagePath;
    }
}
