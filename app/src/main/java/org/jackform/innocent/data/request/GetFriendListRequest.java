package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.jackform.innocent.utils.DebugLog;

/**
 * Created by jackform on 15-7-16.
 */
public class GetFriendListRequest extends TaskRequest implements Parcelable{


    public GetFriendListRequest() {

    }

    public static final Creator<GetFriendListRequest> CREATOR = new Creator<GetFriendListRequest>() {
        @Override
        public GetFriendListRequest createFromParcel(Parcel source) {
            return new GetFriendListRequest(source);
        }

        @Override
        public GetFriendListRequest[] newArray(int size) {
            return new GetFriendListRequest[size];
        }
    };

    public GetFriendListRequest(Parcel source) {
        super();
        mCaller = source.readInt();
    }
    @Override
    public Bundle performDataFetcher() {
        if(null == mDelegator) {
            return null;
        } else {
            return mDelegator.getFriendList();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
    }
}
