package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by jackform on 15-8-25.
 */
public class UpdatePersonalInfoRequest extends TaskRequest implements Parcelable {
//    private byte[] mBitmapBytes;
    private int mIsBitmapModified;
    private String mMale;
    private String mAge;


    public UpdatePersonalInfoRequest(int isHeaderImageModify,String male,String age) {
//        mBitmapBytes = bitmapBytes;
        mIsBitmapModified = isHeaderImageModify;
        mMale = male;
        mAge = age;
    }

    public UpdatePersonalInfoRequest(Parcel source) {
        super();
        mCaller = source.readInt();
//        int byteLen = source.readInt();
//        mBitmapBytes = new byte [byteLen];
//        source.readByteArray(mBitmapBytes);
        mIsBitmapModified = source.readInt();
        mMale = source.readString();
        mAge = source.readString();

    }

    public static final Creator<UpdatePersonalInfoRequest> CREATOR = new Creator<UpdatePersonalInfoRequest>() {
        @Override
        public UpdatePersonalInfoRequest createFromParcel(Parcel source) {
            return new UpdatePersonalInfoRequest(source);
        }

        @Override
        public UpdatePersonalInfoRequest[] newArray(int size) {
            return new UpdatePersonalInfoRequest[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
        dest.writeInt(mIsBitmapModified);
        dest.writeString(mMale);
        dest.writeString(mAge);
    }

    @Override
    public Bundle performDataFetcher() {
        if(mDelegator == null) {
            return null;
        } else {
            return mDelegator.updatePersonalInfo(mIsBitmapModified,mMale,mAge);
        }
    }
}
