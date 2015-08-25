package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-8-25.
 */
public class PersonalInfoRequest extends TaskRequest implements Parcelable {

    public PersonalInfoRequest() {

    }

    public PersonalInfoRequest(Parcel source) {
        super();
        mCaller = source.readInt();

    }

    public static final Creator<PersonalInfoRequest> CREATOR = new Creator<PersonalInfoRequest>() {
        @Override
        public PersonalInfoRequest createFromParcel(Parcel source) {
            return new PersonalInfoRequest(source);
        }

        @Override
        public PersonalInfoRequest[] newArray(int size) {
            return new PersonalInfoRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
    }

    @Override
    public Bundle performDataFetcher() {
        if(mDelegator == null) {
            return null;
        } else {
            return mDelegator.getPersonalInfo();
        }
    }
}
