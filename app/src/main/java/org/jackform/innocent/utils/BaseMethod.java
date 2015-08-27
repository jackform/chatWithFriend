package org.jackform.innocent.utils;

import android.os.Bundle;

import org.jackform.innocent.data.result.TaskResult;

/**
 * Created by jackform on 15-7-6.
 */
public interface BaseMethod {
    boolean isConnect();

    Bundle connect();
    Bundle login(String account,String password);
    Bundle register(String account ,String password,byte[] imageHeader,String age,String male);
    Bundle chat(String userJabberID,String chatMessage,String sendTime);
    Bundle getFriendList();
    Bundle sendFile(String toUserJabberID,String filePath);
    Bundle getPersonalInfo();
    Bundle updatePersonalInfo(int isHeaderModified,String male,String age);
    Bundle searchUserList(String keyword);
    Bundle addFriend(String user);

}
