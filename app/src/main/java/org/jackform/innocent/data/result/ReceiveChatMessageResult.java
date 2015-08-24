package org.jackform.innocent.data.result;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-7-16.
 */
public class ReceiveChatMessageResult implements Parcelable{
    private String mUserJabberID;
    private String mReceiveMessage;

    public ReceiveChatMessageResult(String userJabberID,String receiveMessage) {
        mUserJabberID = userJabberID;
        mReceiveMessage = receiveMessage;
    }

    public ReceiveChatMessageResult(Parcel source) {
        mUserJabberID = source.readString();
        mReceiveMessage = source.readString();
    }

    public String getmUserJabberID()
    {
        return mUserJabberID;
    }

    public String getmReceiveMessage() {
        return mReceiveMessage;
    }

    public static final Creator<ReceiveChatMessageResult> CREATOR = new Creator<ReceiveChatMessageResult>() {
        @Override
        public ReceiveChatMessageResult createFromParcel(Parcel source) {
            return new ReceiveChatMessageResult(source);
        }

        @Override
        public ReceiveChatMessageResult[] newArray(int size) {
            return new ReceiveChatMessageResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUserJabberID);
        dest.writeString(mReceiveMessage);
    }
}
