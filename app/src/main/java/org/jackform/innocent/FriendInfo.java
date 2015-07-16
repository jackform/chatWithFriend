package org.jackform.innocent;

import com.google.gson.annotations.SerializedName;

import org.jackform.innocent.data.APPConstant;

/**
 * Created by jackform on 15-7-16.
 */
public class FriendInfo {

    @SerializedName(APPConstant.KEY_USERNAME)
    String mUserName;

    @SerializedName(APPConstant.KEY_JABBERID)
    String mJabberID;

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
}
