package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-8-27.
 */
public class SearchUserListRequest extends TaskRequest implements Parcelable{
    private String mKeyword;

    public SearchUserListRequest(String keyword) {
        mKeyword = keyword;
    }

    public SearchUserListRequest(Parcel source) {
        super();
        mCaller = source.readInt();
        mKeyword = source.readString();
    }

    public static final Creator<SearchUserListRequest> CREATOR = new Creator<SearchUserListRequest>() {
        @Override
        public SearchUserListRequest createFromParcel(Parcel source) {
            return new SearchUserListRequest(source);
        }

        @Override
        public SearchUserListRequest[] newArray(int size) {
            return new SearchUserListRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
        dest.writeString(mKeyword);
    }

    @Override
    public Bundle performDataFetcher() {
        if(mDelegator == null) {
            return null;
        } else {
            return mDelegator.searchUserList(mKeyword);
        }

    }
}
