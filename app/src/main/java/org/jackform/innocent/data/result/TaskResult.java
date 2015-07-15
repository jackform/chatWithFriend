package org.jackform.innocent.data.result;

import org.jackform.innocent.utils.BaseMethod;
import org.jackform.innocent.xmpp.XmppMethod;

/**
 * Created by jackform on 15-6-30.
 */
abstract public class TaskResult {
    protected int requestID;
    public int getRequestID() {
        return requestID;
    }

    abstract public boolean isSuccess();
}
