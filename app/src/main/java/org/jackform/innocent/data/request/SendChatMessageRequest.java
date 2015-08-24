package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-7-16.
 */
public class SendChatMessageRequest extends TaskRequest implements Parcelable {
    private String mChatMessage;
    private String mUserJabberID;

    public SendChatMessageRequest(String userJabberID,String chatMessage) {
        mChatMessage = chatMessage;
        mUserJabberID = userJabberID;
    }

    public SendChatMessageRequest(Parcel source) {
        super();
        mCaller = source.readInt();
        mChatMessage = source.readString();
        mUserJabberID = source.readString();
    }

    public static final Creator<SendChatMessageRequest> CREATOR = new Creator<SendChatMessageRequest>() {
        @Override
        public SendChatMessageRequest createFromParcel(Parcel source) {
            return new SendChatMessageRequest(source);
        }

        @Override
        public SendChatMessageRequest[] newArray(int size) {
            return new SendChatMessageRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
        dest.writeString(mChatMessage);
        dest.writeString(mUserJabberID);
    }

    @Override
    public Bundle performDataFetcher() {
        if(null == mDelegator)
            return null;
        return mDelegator.chat(mUserJabberID,mChatMessage);
    }
}
