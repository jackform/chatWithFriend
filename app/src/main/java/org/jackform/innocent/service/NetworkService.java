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
import org.jackform.innocent.activity.RegisterActivity;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.GetFriendListRequest;
import org.jackform.innocent.data.request.LoginTaskRequest;
import org.jackform.innocent.data.request.RegisterTaskRequest;
import org.jackform.innocent.data.request.UnBindTaskRequest;
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

    private Handler mHandler = new Handler(mCallback);

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
            response.putString(ResponseConstant.CODE,ResponseConstant.SUCCESS_CODE);
            mResult = result;
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
                case RequestConstant.REQUEST_BASE:

                default:
                    //TODO invalid request ID

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
