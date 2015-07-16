package org.jackform.innocent.data;

/**
 * Created by jackform on 15-7-1.
 */
public interface RequestConstant {
    int REQUEST_BASE = 0;
    int REQUEST_CONNECT = REQUEST_BASE + 1;
    int REQUEST_LOGIN = REQUEST_BASE + 2;
    int REQUEST_UNBIND = REQUEST_BASE + 3;
    int REQUEST_REGISTER = REQUEST_BASE + 4;
    int REQUEST_GET_FRIEND_LIST = REQUEST_BASE + 5;

    String REQUEST_ID = "REMOTE_CALL_REQUEST_ID";
    String REQUEST_PARAMS = "REMOTE_CALL_REQUEST_PARAMS";
}
