package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jackform on 15-7-9.
 */
public class RegisterTaskRequest extends TaskRequest implements Parcelable{

    private String registerAccount;
    private String registerPassword;
    private byte [] headerImage;
    private String  age;
    private String  male;
    public String getAccount() {
        return registerAccount;
    }

    public String getPassword() {
        return registerPassword;
    }

    public byte[] getHeaderImage() {
        return headerImage;
    }

    @Override
    public Bundle performDataFetcher() {
        if(null != mDelegator)
            return mDelegator.register(registerAccount,registerPassword,headerImage,age,male);
        return null;
    }

    public RegisterTaskRequest(String account,String password,byte[] headerBytes,String age,String male) {
        registerAccount = account;
        registerPassword = password;
        headerImage = headerBytes;
        this.age = age;
        this.male = male;
    }

    public RegisterTaskRequest(Parcel source)
    {
        super();
        mCaller = source.readInt();
        registerAccount = source.readString();
        registerPassword = source.readString();
        int byteLen = source.readInt();
        headerImage = new byte [byteLen];
        source.readByteArray(headerImage);
        age  = source.readString();
        male = source.readString();
    }

    public static final Creator<RegisterTaskRequest> CREATOR = new Creator<RegisterTaskRequest>() {
        @Override
        public RegisterTaskRequest createFromParcel(Parcel source) {
            return new RegisterTaskRequest(source);
        }

        @Override
        public RegisterTaskRequest[] newArray(int size) {
            return new RegisterTaskRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCaller);
        dest.writeString(registerAccount);
        dest.writeString(registerPassword);
        dest.writeInt(headerImage.length);
        dest.writeByteArray(headerImage);
        dest.writeString(age);
        dest.writeString(male);
    }
}
