package org.jackform.innocent.utils;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;

import org.jackform.innocent.INetworkService;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.request.TaskRequest;
import org.jackform.innocent.service.AsAResult;
import org.jackform.innocent.service.NetworkService;

/**
 * Created by jackform on 15-6-30.
 */
public class DataFetcher {
    private static Context mContext;
    private ExecuteListener mResultListener;
    private INetworkService mNetworkHandler;
    private AsAResult resultCallBack;
    private boolean isInit = false;
    private static DataFetcher sInstance = null;

    public static synchronized DataFetcher getInstance(Context context) {
        if( null == sInstance) {
            sInstance = new DataFetcher(context);
        }
        return sInstance;
    }


    private DataFetcher(Context context)
    {
        mContext = context;
        if(!isInit) {
            bind2Service(context);
        }
        resultCallBack = new AsAResult(this);
    }

    public void addExecuteListener(ExecuteListener resultListener) {
        if(!isInit) {
            bind2Service(mContext);
        }

        if(null == resultListener) {
            //TODO some error deal with
            return ;
        }
        resultCallBack.addResultListener(resultListener.getCaller(),resultListener);
    }

    public DataFetcher.ExecuteListener removeExecuteListener(int caller) {
        return resultCallBack.removeResultListener(caller);
    }

    public void getResultListener(ExecuteListener resultListener) {
        mResultListener = resultListener;
    }


    public ExecuteListener getResultListener() {
        return mResultListener;
    }

    public void setInitCompleted() {
        isInit = true;
    }

    public interface ExecuteListener {
        int getCaller();
        void onExecuteResult(int responseID, Bundle requestTask);
    }

    public void executeRequest(ExecuteListener l,int requestID,TaskRequest request) {

        if(!isInit) {
            bind2Service(mContext);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!isInit) {
            //TODO error:service connect time out, return connect service error
//            mResultListener.onExecuteResult();
        } else {
            try {
                Bundle parcel = new Bundle();
                request.setCaller(l.getCaller());
                parcel.putParcelable(RequestConstant.REQUEST_PARAMS,(Parcelable)request);
                mNetworkHandler.executeRequest(requestID,parcel);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }



    /*
   public class AsResult extends IResult.Stub {

        @Override
        public void onResult(Bundle result) throws RemoteException {
            if(null == result) {
                Log.v("nonono", "result callback is null");
                return ;
            }
	
            int responseID = result.getInt(ResponseConstant.ID);
            String responseCode = result.getString(ResponseConstant.CODE);
            if(!responseCode.equals(ResponseConstant.SUCCESS_CODE)) {
               //TODO deal with the error
            }
            switch(responseID) {
                case ResponseConstant.INIT_ID:
                   isInit = true;
                    break;
                case ResponseConstant.BASE_ID:
                default:
            }
            if(mResultListener == null)
                return;
            if(result.getString("ResultCode").equals("Success")) {
                mResultListener.onExecuteResult(new TaskResult(){});
            } else {
                mResultListener.onExcuteError(new TaskResult(){});
            }
            //TODO invalid requestID
        }
    }
    */

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mNetworkHandler = INetworkService.Stub.asInterface(service);
            DebugLog.v("connect service success");
            try {
                mNetworkHandler.init(resultCallBack,mContext.getClass().getName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mNetworkHandler = null;
            isInit = false;
        }
    };

    public void unbindService() {
        mContext.unbindService(conn);
    }

    public void bind2Service(Context context){
        Intent intent = new Intent(context, NetworkService.class);
        context.bindService(intent,conn,Service.BIND_AUTO_CREATE);
    }
}
