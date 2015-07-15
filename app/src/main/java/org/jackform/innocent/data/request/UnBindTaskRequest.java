package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-7-8.
 */
public class UnBindTaskRequest extends TaskRequest implements Parcelable{

    public UnBindTaskRequest() {

    }

    public UnBindTaskRequest(Parcel source) {
        mCaller = source.readString();
    }

    public static final Creator<UnBindTaskRequest> CREATOR = new Creator<UnBindTaskRequest>() {
        @Override
        public UnBindTaskRequest createFromParcel(Parcel source) {
            return new UnBindTaskRequest(source);
        }

        @Override
        public UnBindTaskRequest[] newArray(int size) {
            return new UnBindTaskRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCaller);
    }

    @Override
    public Bundle performDataFetcher() {
        return null;
    }
}
