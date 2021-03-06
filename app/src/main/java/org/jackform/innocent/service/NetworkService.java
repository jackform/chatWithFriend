package org.jackform.innocent.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import org.jackform.innocent.INetworkService;
import org.jackform.innocent.IResult;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.AddFriendRequest;
import org.jackform.innocent.data.request.GetFriendListRequest;
import org.jackform.innocent.data.request.LoginTaskRequest;
import org.jackform.innocent.data.request.PersonalInfoRequest;
import org.jackform.innocent.data.request.RegisterTaskRequest;
import org.jackform.innocent.data.request.SearchUserListRequest;
import org.jackform.innocent.data.request.SendChatMessageRequest;
import org.jackform.innocent.data.request.SendFileRequest;
import org.jackform.innocent.data.request.UnBindTaskRequest;
import org.jackform.innocent.data.request.UpdatePersonalInfoRequest;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.xmpp.XmppExecutor;

import java.util.HashMap;


public class NetworkService extends Service {
    private HashMap<String,IResult> mActivities = new HashMap<String,IResult>();
    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch(msg.what) {
                case 0x1:
                    String hash = (String) msg.obj;
                    IResult result = mActivities.get(hash);
                    Bundle response = new Bundle();
                    response.putInt(ResponseConstant.ID,ResponseConstant.INIT_ID);
                    response.putString(ResponseConstant.CODE,ResponseConstant.SUCCESS_CODE);
                    try {
                        if(result == null) {
                            Log.v("hahaha","result call back is null");
                        } else {
                            result.onResult(ResponseConstant.INIT_ID,response);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case ResponseConstant.RECEIVE_CHAT_MESSAGE_ID:
                    DebugLog.v("Handler");
                    Bundle bundle = (Bundle)msg.obj;
                    try {
                        mResult.onResult(ResponseConstant.RECEIVE_CHAT_MESSAGE_ID,bundle);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

            return false;
        }
    };

    public IResult getCallerCallback(String callerHash) {
        return mActivities.get(callerHash);
    }

    public IResult getCallback() {
        return mResult;
    }

    private IResult mResult;

    private Handler mHandler;// = new Handler(mCallback);


    private INetworkService.Stub mBinder = new INetworkService.Stub() {

        @Override
        public void init(IResult result,String hash) throws RemoteException {
            Log.v("hahaha", "enter the init");
            Log.v("hahaha", hash + "send the request");
//            if(!mActivities.containsKey(hash)) {
//                Log.v("hahaha","saved the ["+hash+"]:["+result+"]");
//                mActivities.put(hash,result);
//            }

            Bundle response = new Bundle();
            response.putInt(ResponseConstant.ID,ResponseConstant.INIT_ID);
            response.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
            mResult = result;
            MainThreadHandler.getInstance().setResult(result);
            result.onResult(ResponseConstant.INIT_ID,response);

//            Message m = Message.obtain();
//            m.obj = hash;
//            m.what = 0x1;
//            mHandler.sendMessage(m);
        }

        @Override
        public void connect() throws RemoteException {

        }

        @Override
        public void executeRequest(int requestID,Bundle request) throws RemoteException {
            switch(requestID) {
                case RequestConstant.REQUEST_LOGIN:
                    request.setClassLoader(LoginTaskRequest.class.getClassLoader());
                    LoginTaskRequest loginParams = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    Log.v("hahaha", "account:" + loginParams.getAccount() + "\npassword:" + loginParams.getPassword());
                    XmppExecutor.getInstance(NetworkService.this).submit(loginParams);
                    break;
                case RequestConstant.REQUEST_UNBIND:
                    request.setClassLoader(UnBindTaskRequest.class.getClassLoader());
                    UnBindTaskRequest unBindTaskRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    mActivities.remove(unBindTaskRequest.getCaller());
                    break;
                case RequestConstant.REQUEST_REGISTER:
                    request.setClassLoader(RegisterTaskRequest.class.getClassLoader());
                    RegisterTaskRequest registerTaskRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    Log.v("hahaha","register\naccount:"+registerTaskRequest.getAccount()+" password:"+registerTaskRequest.getPassword());
                    XmppExecutor.getInstance(NetworkService.this).submit(registerTaskRequest);
                    break;
                case RequestConstant.REQUEST_GET_FRIEND_LIST:
                    request.setClassLoader(GetFriendListRequest.class.getClassLoader());
                    GetFriendListRequest getFriendListRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    XmppExecutor.getInstance(NetworkService.this).submit(getFriendListRequest);
                    break;
                case RequestConstant.REQUEST_SEND_CHAT_MESSAGE:
                    request.setClassLoader(SendChatMessageRequest.class.getClassLoader());
                    SendChatMessageRequest sendChatMessageRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    XmppExecutor.getInstance(NetworkService.this).submit(sendChatMessageRequest);
                    break;
                case RequestConstant.REQUEST_SEND_FILE:
                    request.setClassLoader(SendFileRequest.class.getClassLoader());
                    SendFileRequest sendFileRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    XmppExecutor.getInstance(NetworkService.this).submit(sendFileRequest);
                    break;
                case RequestConstant.REQUEST_GET_PERSONAL_INFO:
                    request.setClassLoader(PersonalInfoRequest.class.getClassLoader());
                    PersonalInfoRequest personalInfoRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    XmppExecutor.getInstance(NetworkService.this).submit(personalInfoRequest);
                    break;
                case RequestConstant.REQUEST_UPDATE_PERSONAL_INFO:
                    request.setClassLoader(UpdatePersonalInfoRequest.class.getClassLoader());
                    UpdatePersonalInfoRequest updatepersonalInfoRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    XmppExecutor.getInstance(NetworkService.this).submit(updatepersonalInfoRequest);
                    break;
                case RequestConstant.REQUEST_SEARCH_USER_LIST:
                    request.setClassLoader(SearchUserListRequest.class.getClassLoader());
                    SearchUserListRequest searchUserListRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    XmppExecutor.getInstance(NetworkService.this).submit(searchUserListRequest);
                    break;
                case RequestConstant.REQUEST_ADD_FRIEND:
                    request.setClassLoader(AddFriendRequest.class.getClassLoader());
                    AddFriendRequest addFriendRequest = request.getParcelable(RequestConstant.REQUEST_PARAMS);
                    XmppExecutor.getInstance(NetworkService.this).submit(addFriendRequest);
                    break;
                case RequestConstant.REQUEST_BASE:
                default:
                    //TODO invalid request
            }

        }

        @Override
        public void close() throws RemoteException {

        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        mHandler = new Handler(getMainLooper(),mCallback);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v("hahaha","remove the callback");

        return super.onUnbind(intent);
    }
}
