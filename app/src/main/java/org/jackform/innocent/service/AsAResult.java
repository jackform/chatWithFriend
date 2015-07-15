package org.jackform.innocent.service;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import org.jackform.innocent.IResult;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.utils.DataFetcher;

/**
 * Created by jackform on 15-7-2.
 */
public class AsAResult extends IResult.Stub {
    private DataFetcher mDataFetcher;
    public AsAResult(DataFetcher a) {
        mDataFetcher = a;
    }

    @Override
    public void onResult(int responseID,Bundle result) throws RemoteException {
        if(null == result) {
            Log.v("nonono", "result callback is null");
            return ;
        }

        String responseCode = result.getString(ResponseConstant.CODE);
        if(!responseCode.equals(ResponseConstant.SUCCESS_CODE)) {
            //TODO deal with the error
        }
        switch(responseID) {
            case ResponseConstant.INIT_ID:
                mDataFetcher.setInitCompleted();
                break;
            case ResponseConstant.BASE_ID:
            default:
        }

        DataFetcher.ExcuteListener excuteListener = mDataFetcher.getResultListener();
        if(null == excuteListener) {
            return ;
        } else {
            excuteListener.onExcuteResult(responseID,result);
        }
        //TODO invalid requestID
    }
}
