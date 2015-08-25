package org.jackform.innocent.data.result;

import android.os.Parcel;
import android.os.Parcelable;

import org.jackform.innocent.data.request.PersonalInfoRequest;

/**
 * Created by jackform on 15-8-25.
 */
public class PersonalInfoResult implements Parcelable {

    private String mJabberID;
    private String mMale;
    private String mAge;
    private String mImageHeaderPath;

    public PersonalInfoResult(Parcel source) {
        mJabberID = source.readString();
        mMale = source.readString();
        mAge = source.readString();
        mImageHeaderPath = source.readString();

    }

    public PersonalInfoResult(String jabberID, String male ,String age, String imageHeaderPath) {
        mJabberID = jabberID;
        mMale = male;
        mAge = age;
        mImageHeaderPath = imageHeaderPath;

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mJabberID);
        dest.writeString(mMale);
        dest.writeString(mAge);
        dest.writeString(mImageHeaderPath);
    }

    public static final Creator<PersonalInfoResult> CREATOR = new Creator<PersonalInfoResult>() {
        @Override
        public PersonalInfoResult createFromParcel(Parcel source) {
            return new PersonalInfoResult(source);
        }

        @Override
        public PersonalInfoResult[] newArray(int size) {
            return new PersonalInfoResult[size];
        }
    };

    public String getmJabberID() {
        return mJabberID;
    }

    public void setmJabberID(String mJabberID) {
        this.mJabberID = mJabberID;
    }

    public String getmMale() {
        return mMale;
    }

    public void setmMale(String mMale) {
        this.mMale = mMale;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmImageHeaderPath() {
        return mImageHeaderPath;
    }

    public void setmImageHeaderPath(String mImageHeaderPath) {
        this.mImageHeaderPath = mImageHeaderPath;
    }
}
