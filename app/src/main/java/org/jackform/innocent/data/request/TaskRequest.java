package org.jackform.innocent.data.request;

import android.os.Bundle;

import org.jackform.innocent.utils.BaseMethod;
import org.jackform.innocent.xmpp.XmppMethod;

/**
 * Created by jackform on 15-6-30.
 */
abstract public class TaskRequest {
    protected int mCaller;
    protected BaseMethod mDelegator;
    public TaskRequest(int caller) {
        mCaller = caller;
    }
    public TaskRequest() {
        mDelegator = XmppMethod.getInstance();
    }

    public BaseMethod getMethodDelegator() {
        return mDelegator;
    }

    public int getCaller() {
        return mCaller;
    }

    public void setCaller(int caller) {
        mCaller = caller;
    }

    abstract  public Bundle performDataFetcher();
}
