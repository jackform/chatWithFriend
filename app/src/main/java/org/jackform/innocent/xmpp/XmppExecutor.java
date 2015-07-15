package org.jackform.innocent.xmpp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.jackform.innocent.IResult;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.TaskRequest;
import org.jackform.innocent.data.result.TaskResult;
import org.jackform.innocent.service.NetworkService;
import org.jackform.innocent.utils.BaseMethod;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jackform on 15-7-6.
 */
public class XmppExecutor {
    private static ExecutorService mThreadPool;
    private static XmppExecutor sInstance;
    private Context mContext;
    private XmppExecutor(Context context) {
        mContext = context;
        mThreadPool = Executors.newCachedThreadPool();
    }

    synchronized static public XmppExecutor getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new XmppExecutor(context);
        }
        return sInstance;
    }


    public void submit(TaskRequest request) {
        String caller = request.getCaller();
        Log.v("hahaha","the caller is "+caller);
        IResult resultCallback = ((NetworkService)mContext).getCallerCallback(caller);
        XmppAsyncTask newTask = new XmppAsyncTask(resultCallback);
//        why can't ?
//        newTask.executeOnExecutor(mThreadPool, request);
        newTask.execute(request);
    }


    static class XmppAsyncTask extends AsyncTask<TaskRequest,Void,Bundle> {
        private IResult mResultCallBack;
        public XmppAsyncTask(IResult r) {
            mResultCallBack = r;
        }

        @Override
        protected Bundle doInBackground(TaskRequest... params) {
            Log.v("hahaha","start login!!!");
            TaskRequest request = params[0];
            BaseMethod baseMethod = request.getMethodDelegator();
            Bundle result = null;
            if(!baseMethod.isConnect()) {
                result = baseMethod.connect();
            }
            if(result.getString(ResponseConstant.CODE).equals(ResponseConstant.SUCCESS_CODE)){
                result = request.performDataFetcher();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bundle response) {
            super.onPostExecute(response);

            int responseID = response.getInt(ResponseConstant.ID);
            try {
                if(mResultCallBack == null) {
                    Log.v("hahaha","result callback is null");
                    return ;
                }
                mResultCallBack.onResult(responseID,response);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }

}
