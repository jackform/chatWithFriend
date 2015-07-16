package org.jackform.innocent.service;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.jackform.innocent.IResult;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.result.GetFriendListResult;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;

import java.util.HashMap;

/**
 * Created by jackform on 15-7-2.
 */
public class AsAResult extends IResult.Stub {
    private DataFetcher mDataFetcher;
    private HashMap<Integer,DataFetcher.ExecuteListener> mResultListeners;


    public void addResultListener(int caller,DataFetcher.ExecuteListener l) {
        mResultListeners.put(caller,l);
    }

    public DataFetcher.ExecuteListener removeResultListener(int caller) {
        return mResultListeners.remove(caller);
    }

    public AsAResult(DataFetcher a) {
        mDataFetcher = a;
        mResultListeners = new HashMap<Integer,DataFetcher.ExecuteListener>();
    }

    @Override
    public void onResult(int responseID,Bundle result) throws RemoteException {
        if(null == result) {
            Log.v("nonono", "result callback is null");
            return ;
        }
        switch(responseID) {
            case ResponseConstant.INIT_ID:
                onInitCompleted(result);
                return;
            case ResponseConstant.REGISTER_ID:
                onRegisterCompleted(result);
                break;
            case ResponseConstant.LOGIN_ID:
                onLoginCompleted(result);
                break;
            case ResponseConstant.GET_FRIEND_LIST_ID:
                onGetFriendListCompleted(result);
            case ResponseConstant.BASE_ID:
            default:
                //TODO deal invalid requestID
        }

        int caller = result.getInt(ResponseConstant.CALLER);
        DataFetcher.ExecuteListener excuteListener = mResultListeners.get(caller);
        DebugLog.v("in callback,the caller is:"+caller);
        if(null == excuteListener) {
            return ;
        } else {
            excuteListener.onExecuteResult(responseID, result);
        }
    }

    private void onGetFriendListCompleted(Bundle result) {
        result.setClassLoader(GetFriendListResult.class.getClassLoader());
    }

    private void onInitCompleted(Bundle result) {
        String responseCode = result.getString(ResponseConstant.CODE);
        mDataFetcher.setInitCompleted();
        if(responseCode.equals(ResponseConstant.SUCCESS_CODE)) {
            mDataFetcher.setInitCompleted();
        } else {
           //TODO deal some init error
        }
    }

    private void onRegisterCompleted(Bundle result) {

    }

    private void onLoginCompleted(Bundle result) {

    }
}
