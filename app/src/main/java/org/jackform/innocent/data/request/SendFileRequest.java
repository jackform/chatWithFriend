package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-7-17.
 */
public class SendFileRequest extends TaskRequest implements Parcelable {

    private String mToUserJabberID;
    private String mFilePath;


    public SendFileRequest(String toUserJabberID,String filePath) {
        mToUserJabberID = toUserJabberID;
        mFilePath = filePath;
    }

    public SendFileRequest(Parcel source) {
        super();
        mCaller = source.readInt();
        mToUserJabberID = source.readString();
        mFilePath = source.readString();
    }

    public static final Creator<SendFileRequest> CREATOR = new Creator<SendFileRequest>() {
        @Override
        public SendFileRequest createFromParcel(Parcel source) {
            return new SendFileRequest(source);
        }

        @Override
        public SendFileRequest[] newArray(int size) {
            return new SendFileRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
        dest.writeString(mToUserJabberID);
        dest.writeString(mFilePath);
    }

    @Override
    public Bundle performDataFetcher() {
        if(mDelegator == null) {
            return null;
        } else {
            return mDelegator.sendFile(mToUserJabberID,mFilePath);
        }
    }
}
