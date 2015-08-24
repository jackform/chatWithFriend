package org.jackform.innocent.data.result;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-8-24.
 */
public class SendChatMessageResult implements Parcelable {

    private String mJabberID;
    private String mSendMessage;
    private String mSendTime;

    public String getmSendTime() {
        return mSendTime;
    }

    public void setmSendTime(String mSendTime) {
        this.mSendTime = mSendTime;
    }

    public String getSendMessage() {
        return mSendMessage;
    }

    public void setSendMessage(String mSendMessage) {
        this.mSendMessage = mSendMessage;
    }

    public String getJabberID() {
        return mJabberID;
    }

    public void setJabberID(String mJabberID) {
        this.mJabberID = mJabberID;
    }

    public SendChatMessageResult(String jabberID, String sendMessage, String sendTime) {
        mJabberID = jabberID;
        mSendMessage = sendMessage;
        mSendTime = sendTime;
    }

    public SendChatMessageResult(Parcel source) {
        mJabberID = source.readString();
        mSendMessage = source.readString();
        mSendTime = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<SendChatMessageResult> CREATOR = new Creator<SendChatMessageResult>() {
        @Override
        public SendChatMessageResult createFromParcel(Parcel source) {
            return new SendChatMessageResult(source);
        }

        @Override
        public SendChatMessageResult[] newArray(int size) {
            return new SendChatMessageResult[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mJabberID);
        dest.writeString(mSendMessage);
        dest.writeString(mSendTime);
    }
}
