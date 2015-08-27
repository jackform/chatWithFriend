package org.jackform.innocent.data;

/**
 * Created by jackform on 15-8-27.
 */
public class DataEngine {
    static DataEngine sInstance;

    private boolean mIsFriendListUpdate;

    private DataEngine() {
        mIsFriendListUpdate = false;
    }

    synchronized public static DataEngine getInstance() {
        if(sInstance == null) {
            sInstance = new DataEngine();
        }
        return sInstance;
    }

    public boolean isFriendListUpdated() {
       return mIsFriendListUpdate;
    }

    public void setFriendListUpdated() {
        mIsFriendListUpdate = true;
    }
}
