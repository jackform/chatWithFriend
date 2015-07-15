package org.jackform.innocent.data.request;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by jackform on 15-7-1.
 */
public class LoginTaskRequest extends TaskRequest implements Parcelable {
    private String mAccount;
    private String mPassword;

    public LoginTaskRequest(String caller,String account,String password) {
        super(caller);
        mAccount = account;
        mPassword = password;
    }

    public LoginTaskRequest(Parcel source) {
        super();
        mCaller = source.readString();
        mAccount = source.readString();
        mPassword = source.readString();

    }

    public static final Creator<LoginTaskRequest> CREATOR = new Creator<LoginTaskRequest>() {
        @Override
        public LoginTaskRequest createFromParcel(Parcel source) {
            return new LoginTaskRequest(source);
        }

        @Override
        public LoginTaskRequest[] newArray(int size) {
            return new LoginTaskRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCaller);
        dest.writeString(mAccount);
        dest.writeString(mPassword);
    }

    @Override
    public Bundle performDataFetcher() {

        if(null != mDelegator)
            return mDelegator.login(mAccount,mPassword);
        else {
            //TODO some error deal with

            return null;
        }
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String mAccount) {
        this.mAccount = mAccount;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
