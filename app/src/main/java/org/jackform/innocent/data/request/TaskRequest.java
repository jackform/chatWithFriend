package org.jackform.innocent.data.request;

import android.os.Bundle;

import org.jackform.innocent.utils.BaseMethod;
import org.jackform.innocent.xmpp.XmppMethod;

/**
 * Created by jackform on 15-6-30.
 */
abstract public class TaskRequest {
    protected String mCaller;
    protected BaseMethod mDelegator;
    public TaskRequest(String caller) {
        mCaller = caller;
    }
    public TaskRequest() {
        mDelegator = new XmppMethod();
    }

    public BaseMethod getMethodDelegator() {
        return mDelegator;
    }

    public String getCaller() {
        return mCaller;
    }

    public void setCaller(String caller) {
        mCaller = caller;
    }

    abstract  public Bundle performDataFetcher();
}
