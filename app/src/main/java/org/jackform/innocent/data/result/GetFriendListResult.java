package org.jackform.innocent.data.result;

import android.os.Parcel;
import android.os.Parcelable;

import org.jackform.innocent.data.request.GetFriendListRequest;
import org.jivesoftware.smack.RosterGroup;

import java.util.List;

/**
 * Created by jackform on 15-7-16.
 */
public class GetFriendListResult implements Parcelable{
    private String mJsonFriendList;


    public String getmJsonFriendList() {
        return mJsonFriendList;
    }
    public GetFriendListResult(String friendList) {
        mJsonFriendList = friendList;
    }

    public GetFriendListResult(Parcel source) {
        mJsonFriendList = source.readString();
    }

    public static final Creator<GetFriendListResult> CREATOR = new Creator<GetFriendListResult>() {
        @Override
        public GetFriendListResult createFromParcel(Parcel source) {
            return new GetFriendListResult(source);
        }

        @Override
        public GetFriendListResult[] newArray(int size) {
            return new GetFriendListResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mJsonFriendList);
    }
}
