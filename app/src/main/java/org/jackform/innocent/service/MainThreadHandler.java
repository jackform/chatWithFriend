package org.jackform.innocent.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;

import org.jackform.innocent.IResult;
import org.jackform.innocent.data.ResponseConstant;

/**
 * Created by jackform on 15-7-17.
 */
public class MainThreadHandler extends Handler {

    private IResult mResult;

    private static MainThreadHandler sInstance;
    private MainThreadHandler() {
        super(Looper.getMainLooper());
    }

    public IResult getResult() {
        return mResult;
    }

    public void setResult(IResult result) {
        mResult = result;
    }

    public void executeMessage(Message msg){
        final int msgType = msg.what;

       switch(msgType) {
           case ResponseConstant.RECEIVE_CHAT_MESSAGE_ID:
               final Bundle bundle = (Bundle) msg.obj;
               super.post(new Runnable() {
                   @Override
                   public void run() {
                       if(mResult == null)
                           return ;
                       try {
                           mResult.onResult(msgType,bundle);
                       } catch (RemoteException e) {
                           e.printStackTrace();
                       }
                   }
               });
               break;
           default:
               break;

       }
    }

    public static synchronized MainThreadHandler getInstance() {
        if(sInstance == null) {
            sInstance = new MainThreadHandler();
        }
        return sInstance;
    }
}
