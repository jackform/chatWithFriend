package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-8-27.
 */
public class AddFriendRequest extends TaskRequest implements Parcelable {
    private String mUsername;

    public AddFriendRequest(String username) {
        mUsername = username;
    }

    public AddFriendRequest(Parcel source) {
        super();
        mCaller = source.readInt();
        mUsername = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddFriendRequest> CREATOR = new Creator<AddFriendRequest>() {
        @Override
        public AddFriendRequest createFromParcel(Parcel source) {
            return new AddFriendRequest(source);
        }

        @Override
        public AddFriendRequest[] newArray(int size) {
            return new AddFriendRequest[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
        dest.writeString(mUsername);
    }

    @Override
    public Bundle performDataFetcher() {
        if(mDelegator == null) {
            return null;
        } else {
            return mDelegator.addFriend(mUsername);
        }
    }
}
