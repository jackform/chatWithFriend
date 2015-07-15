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

import java.util.ArrayList;

/**
 * Created by jackform on 15-6-30.
 */
public class DataFetcher {
    private Context mContext;
    private ExcuteListener mResultListener;
    private INetworkService mNetworkHandler;
    private AsAResult resultCallBack;
    private boolean isInit = false;

    public DataFetcher(Context context)
    {
        mContext = context;
        bind2Service();
        resultCallBack = new AsAResult(this);
    }

    public void getResultListener(ExcuteListener resultListener) {
        mResultListener =resultListener;
    }

    public ExcuteListener getResultListener() {
        return mResultListener;
    }

    public void setInitCompleted() {
        isInit = true;
    }

    public interface ExcuteListener {
        void onExcuteResult(int responseID,Bundle requestTask);
    }

    public void executeRequest(int requestID,TaskRequest request) {

        if( null == mResultListener )
            return ;
        if(!isInit) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(!isInit) {
            //TODO error:service connect time out
//            mResultListener.onExcuteResult();
        } else if( mNetworkHandler == null ) {
            //TODO error:service disconnected
//            mResultListener.onExcuteResult();
        } else {
            try {
                Bundle parcel = new Bundle();
                request.setCaller(mContext.getClass().getName());
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
                mResultListener.onExcuteResult(new TaskResult(){});
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
            try {
                mNetworkHandler.init(resultCallBack,mContext.getClass().getName());
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mNetworkHandler = null;
        }
    };

    public void unbindService() {
        mContext.unbindService(conn);
    }

    public void bind2Service(){
        Intent intent = new Intent(mContext, NetworkService.class);
        mContext.bindService(intent,conn,Service.BIND_AUTO_CREATE);
        /*
        mContext.bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
//                mContext.toast("activity bind service successd!");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, Service.BIND_AUTO_CREATE);
        */
    }
}
