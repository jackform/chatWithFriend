package org.jackform.innocent.data;

import com.google.gson.annotations.SerializedName;


import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.xmpp.XmppMethod;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jackform on 15-7-16.
 */
public class FriendList {
    @SerializedName(APPConstant.KEY_ROSTER)
    private List<FriendGroup> mFriendList;

    public FriendList(Roster roster) {
        Collection<RosterGroup> rosterGroup = roster.getGroups();
        DebugLog.v("the sizeof group" + rosterGroup.size());
        List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
        for (RosterGroup r : rosterGroup) {
            groupsList.add(r);
        }
        DebugLog.v("the sizeof group" + groupsList.size());
        mFriendList = new ArrayList<FriendGroup>();

        int i;
        int j;
        RosterGroup groupEntry = null;
        FriendGroup friendGroup = null;
        for(i = 0 ; i < groupsList.size(); i++) {
            groupEntry = groupsList.get(i);
            friendGroup = new FriendGroup();
            friendGroup.setmGrouName(groupEntry.getName());
            DebugLog.v(groupEntry.getName());

            List<FriendInfo> friendInfos = new ArrayList<FriendInfo>();
            List<RosterEntry> rosterEntries = new ArrayList<RosterEntry>();
            for (RosterEntry r : groupEntry.getEntries())
                rosterEntries.add(r);
            FriendInfo friendInfo = null;
            for (j = 0; j < rosterEntries.size(); j++) {
                friendInfo = new FriendInfo();
                friendInfo.setmUserName(rosterEntries.get(j).getName());
                DebugLog.v(rosterEntries.get(j).getName());
                friendInfo.setmJabberID(rosterEntries.get(j).getUser());
                String imagePath =  XmppMethod.getInstance().getHeaderImagePaht(friendInfo.getmJabberID());
                friendInfo.setmHeaderImagePath(imagePath);
                friendInfo.setmMale(XmppMethod.getInstance().getMale(friendInfo.getmJabberID()));
                friendInfo.setmAge(XmppMethod.getInstance().getAge(friendInfo.getmJabberID()));
                friendInfos.add(friendInfo);
            }
            friendGroup.setmFriendInfo(friendInfos);
            mFriendList.add(friendGroup);
        }

    }

    public List<FriendGroup> getmFriendList() {
        return mFriendList;
    }

    public void setmFriendList(List<FriendGroup> mFriendList) {
        this.mFriendList = mFriendList;
    }
}
