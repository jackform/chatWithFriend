package org.jackform.innocent.data;

/**
 * Created by jackform on 15-7-1.
 */
public interface ResponseConstant {
    String ID = "REMOTE_CALL_BACK_ID";
    String CODE = "REMOTE_CALL_BACK_CODE";
    String ESCRIPTION = "REMOTE_CALL_BACK_DESCRIPTION";
    String PARAMS = "REMOTE_CALL_BACK_PARAM";
    String CALLER = "REMOTE_CALLER_NAME";

    int BASE_ID = 0;
    int INIT_ID = BASE_ID + 1;
    int CONNECT_ID = BASE_ID + 2;
    int LOGIN_ID = BASE_ID + 3;
    int REGISTER_ID = BASE_ID + 4;
    int GET_FRIEND_LIST_ID = BASE_ID + 5;
    int SEND_CHAT_MESSAGE_ID = BASE_ID + 6;
    int RECEIVE_CHAT_MESSAGE_ID = BASE_ID + 7;
    int SEND_FILE_ID = BASE_ID + 8;
    int GET_PERSONAL_INFO_ID = BASE_ID + 9;
    int UPDATE_PERSONAL_INFO_ID = BASE_ID + 10;
    int SEARCHUSERS_ID = BASE_ID + 11;
    int ADD_FRIEND_ID = BASE_ID + 12;
    int RECEIVE_FRIEND_REQEST_ID = BASE_ID + 13;



    String SUCCESS_CODE = "0";
    String NOT_CONNECT_CODE = "1";
    String NOT_LOGIN_CODE = "2";
    String NETWORK_NOT_ACCESS = "3";
    String SEVER_NOT_RESPONSE = "4";
    String ACCOUNT_ALREADY_EXIST = "5";
    String REGISTER_FAILURE = "6";


    String SEARCH_USERS_FAILURE = "7";
    String ADD_FRIEND_FAILURE = "8";
    String USER_ALREADY_ADD = "9";
    String CANNOT_ADD_ME_AS_FRIEND = "10";
}
