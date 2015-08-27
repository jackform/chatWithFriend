package org.jackform.innocent.service;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.jackform.innocent.IResult;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.SendChatMessageRequest;
import org.jackform.innocent.data.result.GetFriendListResult;
import org.jackform.innocent.data.result.PersonalInfoResult;
import org.jackform.innocent.data.result.ReceiveChatMessageResult;
import org.jackform.innocent.data.result.SendChatMessageResult;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
                break;
            case ResponseConstant.SEND_CHAT_MESSAGE_ID:
                onSendChatMessageCompleted(result);
                break;
            case ResponseConstant.RECEIVE_CHAT_MESSAGE_ID:
                onReceiveChatMessage(result);
                return;
            case ResponseConstant.GET_PERSONAL_INFO_ID:
                onGetPersonalInfoCompleted(result);
                break;
            case ResponseConstant.UPDATE_PERSONAL_INFO_ID:
                onUpdatePersonalInfoCompleted(result);
                break; 
            case ResponseConstant.SEARCHUSERS_ID:
                onSearchUserListCompleted(result); 
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

    private void onSearchUserListCompleted(Bundle result) {
    }

    private void onUpdatePersonalInfoCompleted(Bundle result) {

    }

    private void onGetPersonalInfoCompleted(Bundle result) {
        result.setClassLoader(PersonalInfoResult.class.getClassLoader());
    }

    private void onSendChatMessageCompleted(Bundle result) {
        result.setClassLoader(SendChatMessageResult.class.getClassLoader());
    }

    private void onReceiveChatMessage(Bundle result) {
        result.setClassLoader(ReceiveChatMessageResult.class.getClassLoader());
        Iterator iter = mResultListeners.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            DataFetcher.ExecuteListener r = (DataFetcher.ExecuteListener) entry.getValue();
            r.onExecuteResult(result.getInt(ResponseConstant.ID),result);
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
