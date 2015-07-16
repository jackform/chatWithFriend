package org.jackform.innocent.data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.jackform.innocent.FriendInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jackform on 15-7-16.
 */
public class FriendGroup implements Serializable {
    @SerializedName(APPConstant.KEY_GROUPNAME)
    private String mGrouName;
    @SerializedName(APPConstant.KEY_GROUPENTRY)
    private List<FriendInfo> mFriendInfo;

    public String getmGrouName() {
        return mGrouName;
    }

    public void setmGrouName(String mGrouName) {
        this.mGrouName = mGrouName;
    }

    public List<FriendInfo> getmFriendInfo() {
        return mFriendInfo;
    }

    public void setmFriendInfo(List<FriendInfo> mFriendInfo) {
        this.mFriendInfo = mFriendInfo;
    }
}

