package org.jackform.innocent.xmpp;

import android.os.Bundle;
import android.test.SyncBaseInstrumentation;
import android.util.Log;

import com.google.gson.Gson;

import org.jackform.innocent.data.FriendList;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.result.GetFriendListResult;
import org.jackform.innocent.utils.BaseMethod;
import org.jackform.innocent.utils.DebugLog;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.packet.VCard;
import org.xbill.DNS.MFRecord;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jackform on 15-7-6.
 */
public class XmppMethod implements BaseMethod{
    private XMPPConnection mConnect;
    private boolean isConnected = false;
    private static final String IP_ADRESS = "172.18.66.212";
    private boolean isLogined = false;
    private String loginName;
//     private static final String IP_ADRESS = "192.168.1.11";
    private static XmppMethod sInstance = null;

    private XmppMethod() {

    }

    public Roster getRoster() {
        return mConnect.getRoster();
    }

    public static XmppMethod getInstance() {
        if( null == sInstance ) {
           sInstance = new XmppMethod();
        }
        return sInstance;
    }

    @Override
    public boolean isConnect() {
        return isConnected;
    }

    @Override
    public Bundle connect() {
        Bundle result = new Bundle();
        result.putInt(ResponseConstant.ID,ResponseConstant.CONNECT_ID);
        ConnectionConfiguration config = new ConnectionConfiguration(IP_ADRESS);
        config.setReconnectionAllowed(true);
        config.setSecurityMode(SecurityMode.disabled);
        config.setSASLAuthenticationEnabled(false);
        config.setCompressionEnabled(false);

        mConnect = new XMPPConnection(config);
        try {
            mConnect.connect();
        }catch(Exception e) {
            e.printStackTrace();
        }
        result.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
        isConnected = true;
        return result;
    }

    @Override
    public Bundle login(String account,String password) {
        Bundle result = new Bundle();
        result.putInt(ResponseConstant.ID, ResponseConstant.LOGIN_ID);
        try {
            mConnect.login(account, password);
        } catch (XMPPException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        result.putString(ResponseConstant.CODE,ResponseConstant.SUCCESS_CODE);
        isLogined = true;
        return result;
    }

    public boolean isLogin() {
        return isConnected && isLogined;
    }

    @Override
    public Bundle register(String account,String password,byte[] headerImage) {
        Bundle res = new Bundle();
        res.putInt(ResponseConstant.ID,ResponseConstant.REGISTER_ID);
        Registration reg = new Registration();
        reg.setType(IQ.Type.SET);
        reg.setTo(mConnect.getServiceName());
        reg.setUsername(account);
        reg.setPassword(password);
        reg.addAttribute("android", "geolo_createUser_android");
        PacketFilter filter = new AndFilter(new PacketIDFilter(
                reg.getPacketID()), new PacketTypeFilter(IQ.class));
        PacketCollector collector = mConnect
                .createPacketCollector(filter);
        mConnect.sendPacket(reg);
        IQ result = (IQ) collector.nextResult(SmackConfiguration
                .getPacketReplyTimeout());
        // Stop queuing results
        collector.cancel();// 停止请求results（是否成功的结果）
        if (result == null) {
            res.putString(ResponseConstant.CODE,ResponseConstant.SEVER_NOT_RESPONSE);
        } else if (result.getType() == IQ.Type.ERROR) {
            if (result.getError().toString()
                    .equalsIgnoreCase("conflict(409)")) {
                res.putString(ResponseConstant.CODE,ResponseConstant.ACCOUNT_ALREADY_EXIST);
            } else {
                res.putString(ResponseConstant.CODE,ResponseConstant.REGISTER_FAILURE);
            }
        } else if (result.getType() == IQ.Type.RESULT) {
            try {

                VCard vcard = new VCard();
                login(account, password);
                vcard.load(mConnect);
                String encodedImage = StringUtils.encodeBase64(headerImage);
                vcard.setAvatar(headerImage, encodedImage);
                vcard.setEncodedImage(encodedImage);
                vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>"
                        + encodedImage + "</BINVAL>", true);
                vcard.save(mConnect);
//                XmppUtils.getConnection().getAccountManager().deleteAccount();
                // XmppUtils.closeConnection();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            res.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
        }
        return res;
    }

    @Override
    public Bundle chat() {
        return null;
    }

    @Override
    public Bundle getFriendList() {
        Bundle res = new Bundle();
        res.putInt(ResponseConstant.ID,ResponseConstant.GET_FRIEND_LIST_ID);
        Roster roster = mConnect.getRoster();
        FriendList mfriendList = new FriendList(roster);
//        String friendJson
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mfriendList);
        DebugLog.v("[friendList]:"+jsonStr);
        GetFriendListResult getFriendListResult = new GetFriendListResult(jsonStr);
        res.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
        res.putParcelable(ResponseConstant.PARAMS,getFriendListResult);
        return res;
    }
}
