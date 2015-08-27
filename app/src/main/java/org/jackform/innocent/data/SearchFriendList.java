package org.jackform.innocent.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jackform on 15-8-27.
 */
public class SearchFriendList {
    @SerializedName(APPConstant.KEY_SEARCH_PRIEND_LIST)
    public List<FriendInfo> mfriendList;

    public SearchFriendList(List<FriendInfo> friendList) {
       mfriendList = friendList;
    }

    public List<FriendInfo> getMfriendList() {
        return mfriendList;
    }

    public void setMfriendList(List<FriendInfo> mfriendList) {
        this.mfriendList = mfriendList;
    }
}
