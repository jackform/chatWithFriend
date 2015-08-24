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
