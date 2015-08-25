package org.jackform.innocent.xmpp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DebugUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.jackform.innocent.data.APPConstant;
import org.jackform.innocent.data.FriendList;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.result.GetFriendListResult;
import org.jackform.innocent.data.result.PersonalInfoResult;
import org.jackform.innocent.data.result.ReceiveChatMessageResult;
import org.jackform.innocent.data.result.SendChatMessageResult;
import org.jackform.innocent.service.MainThreadHandler;
import org.jackform.innocent.utils.BaseMethod;
import org.jackform.innocent.utils.DebugLog;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.filetransfer.StreamNegotiator;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by jackform on 15-7-6.
 */
public class XmppMethod implements BaseMethod{
    private XMPPConnection mConnect;
    private boolean isConnected = false;
//    private static final String IP_ADRESS = "172.18.66.212";
    private static final String IP_ADRESS = "172.18.66.184";
    private boolean isLogined = false;
    private String loginName;

    private ChatManager mChatManager;

    private HashMap<String,Chat> mChatList;

    private Handler mMainThreadHandler ;//= new Handler(Looper.getMainLooper());
//     private static final String IP_ADRESS = "192.168.1.11";
    private static XmppMethod sInstance = null;

    public XMPPConnection getConnection() {
        ConnectionConfiguration config = new ConnectionConfiguration(IP_ADRESS);
        config.setReconnectionAllowed(true);
        config.setSecurityMode(SecurityMode.disabled);
        config.setSASLAuthenticationEnabled(false);
        config.setCompressionEnabled(false);
        mConnect = new XMPPConnection(config);

        return mConnect;
    }

    private XmppMethod() {
        mMainThreadHandler = new Handler(Looper.getMainLooper());
        mChatList = new HashMap<String,Chat>();
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
        result.putInt(ResponseConstant.ID, ResponseConstant.CONNECT_ID);
        ConnectionConfiguration config = new ConnectionConfiguration(IP_ADRESS);
        config.setReconnectionAllowed(true);
        config.setSecurityMode(SecurityMode.disabled);
        config.setSASLAuthenticationEnabled(false);
        config.setCompressionEnabled(false);

        mConnect = new XMPPConnection(config);

        ProviderManager pm = ProviderManager.getInstance();
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());
        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());

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
        if(isConnect() == false) {
            try {
                mConnect.connect();
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
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
    public Bundle updatePersonalInfo(int isHeadImageModified, String male, String age) {
        Bundle res = new Bundle();
        res.putInt(ResponseConstant.ID,ResponseConstant.UPDATE_PERSONAL_INFO_ID);
        DebugLog.v("updatePersonalInfo\n male:"+male+" age:"+age);

        VCard vcard = new VCard();
        try {
            vcard.load(mConnect);

        if( isHeadImageModified == 1 ) {
            byte[] headerImage;
            String dirPath = Environment.getExternalStorageDirectory() + "/.IMTONG/Vcard/Head/";
            String fileName =  dirPath + APPConstant.KEY_MYSELF_IMAGE_HEADER + ".png";
            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(null == bitmap ) {
                //TODO deal with some error
                return res;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            headerImage = baos.toByteArray();
            String encodedImage = StringUtils.encodeBase64(headerImage);
            vcard.setAvatar(headerImage, encodedImage);
            vcard.setEncodedImage(encodedImage);
            vcard.setField("PHOTO", "<TYPE>image/jpg</TYPE><BINVAL>"
                    + encodedImage + "</BINVAL>", true);
        }
            vcard.setFirstName(male);
            vcard.setLastName(age);
            vcard.save(mConnect);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        res.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
        return res;
    }

    @Override
    public Bundle register(String account,String password,byte[] headerImage,String age,String male) {
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
                vcard.setFirstName(male);
                vcard.setLastName(age);
                vcard.save(mConnect);
                mConnect.disconnect();
                isConnected = false;
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
    public Bundle chat(String userJabberID,String chatMessage,String sendTime) {
        Bundle res = new Bundle();
        res.putInt(ResponseConstant.ID, ResponseConstant.SEND_CHAT_MESSAGE_ID);
        Chat chat = mChatList.get(userJabberID);
        try {
            chat.sendMessage(chatMessage);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        res.putString(ResponseConstant.CODE,ResponseConstant.SUCCESS_CODE);
        SendChatMessageResult sendChatMessageResult = new SendChatMessageResult(userJabberID,chatMessage,sendTime);
        res.putParcelable(ResponseConstant.PARAMS, sendChatMessageResult);
        return res;
    }


    ChatManagerListener mChatManagerListener = new ChatManagerListener() {

        @Override
        public void chatCreated(Chat chat, boolean able) {
            chat.addMessageListener(mMessageListener);
        }
    };


    MessageListener mMessageListener = new MessageListener() {

        @Override
        public void processMessage(Chat chat, Message message) {
            if(null == message.getBody())
                return;

            DebugLog.v("[receive]:" + message.getBody());

            android.os.Message msg = mMainThreadHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt(ResponseConstant.ID, ResponseConstant.RECEIVE_CHAT_MESSAGE_ID);
            ReceiveChatMessageResult receiveChatMessageResult = new ReceiveChatMessageResult(message.getFrom(), message.getBody());
            bundle.putParcelable(ResponseConstant.PARAMS, receiveChatMessageResult);
            msg.what = ResponseConstant.RECEIVE_CHAT_MESSAGE_ID;
            msg.obj = bundle;
//            mMainThreadHandler.sendMessage(msg);

            MainThreadHandler.getInstance().executeMessage(msg);
        }
    };


    @Override
    public Bundle getFriendList() {
        Bundle res = new Bundle();
        res.putInt(ResponseConstant.ID,ResponseConstant.GET_FRIEND_LIST_ID);
        Roster roster = mConnect.getRoster();
        FriendList mfriendList = new FriendList(roster);

        mChatManager =  mConnect.getChatManager();
        mChatManager.addChatListener(mChatManagerListener);
        Collection<RosterEntry> rosterEntries = roster.getEntries();
        for(RosterEntry rosterEntry : rosterEntries) {
            Chat chat = mChatManager.createChat(rosterEntry.getUser(),null);
            mChatList.put(rosterEntry.getUser(),chat);

            //TODO add message receive listener
        }

//        String friendJson
        Gson gson = new Gson();
        String jsonStr = gson.toJson(mfriendList);
        DebugLog.v("[friendList]:"+jsonStr);
        GetFriendListResult getFriendListResult = new GetFriendListResult(jsonStr);
        res.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
        res.putParcelable(ResponseConstant.PARAMS, getFriendListResult);
        return res;
    }

    @Override
    public Bundle sendFile(String toUserJabberID, String filePath) {
        Bundle res = new Bundle();
        res.putInt(ResponseConstant.ID, ResponseConstant.SEND_FILE_ID);

        ServiceDiscoveryManager sdm =  ServiceDiscoveryManager.getInstanceFor(mConnect);
        if (sdm == null)
            sdm = new  ServiceDiscoveryManager(mConnect);
        sdm.addFeature("http://jabber.org/protocol/disco#info");
        sdm.addFeature("jabber:iq:privacy");
        FileTransferNegotiator.setServiceEnabled(mConnect, true);

        FileTransferManager manager = new FileTransferManager(mConnect);
        OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(toUserJabberID);
//        FileTransferNegotiator negotiator =  FileTransferNegotiator.getInstanceFor(mConnect);
        try {
            DebugLog.v("filePath:" + filePath);
            transfer.sendFile(new File(filePath), "data backup");
//            StreamNegotiator streamNegotiator = negotiator.negotiateOutgoingTransfer();
            boolean isTransferError = false;
            while(!transfer.isDone()) {
                if(transfer.getStatus().equals(FileTransfer.Status.error)) {
                    isTransferError = true;
                    break;
                } else {
                    DebugLog.v("status:"+transfer.getStatus()+"  progress:"+transfer.getProgress());
                }
            }
        } catch (XMPPException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        res.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
        return res;
    }

    @Override
    public Bundle getPersonalInfo() {
        Bundle result = new Bundle();
        result.putInt(ResponseConstant.ID,ResponseConstant.GET_PERSONAL_INFO_ID);

        DebugLog.v("deal the request getpersonal info ");

        String jabberID;
        String male;
        String age;
        String HeaderImagePath;
        String userName;

        VCard card = new VCard();
        try {
            card.load(mConnect);
        }catch(XMPPException e) {
            e.printStackTrace();
        }

        jabberID = mConnect.getUser();
        male = card.getFirstName();
        age  = card.getLastName();

        ByteArrayInputStream bais = new ByteArrayInputStream(card.getAvatar());
        byte buf[] = new byte[1024];
        String dirPath = Environment.getExternalStorageDirectory() + "/.IMTONG/Vcard/Head/";
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String fileName =  dirPath + APPConstant.KEY_MYSELF_IMAGE_HEADER + ".png";
        File download = new File(fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(download);
            do {
                int numread = bais.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);
            bais.close();
            fos.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DebugLog.v("jabberID:" + jabberID + " male:" + male + " age:" + age + " fileName:" + fileName);
        if(!TextUtils.isEmpty(jabberID)) {
            result.putString(ResponseConstant.CODE, ResponseConstant.SUCCESS_CODE);
            result.putParcelable(ResponseConstant.PARAMS,new PersonalInfoResult(jabberID,male,age,fileName));
        } else {
           //TODO deal with some error
        }
        return result;
    }

    public String getNickName(String user) {
        VCard card = new VCard();
        try {
            card.load(mConnect,user);
        } catch(XMPPException e) {
            e.printStackTrace();
        }
        return card.getNickName();
    }

    public String getMale(String user) {
        VCard card = new VCard();
        try {
            card.load(mConnect,user);
        } catch(XMPPException e) {
            e.printStackTrace();
        }
        return card.getFirstName();
    }

    public String getAge(String user) {
        VCard card = new VCard();
        try {
            card.load(mConnect,user);
        } catch(XMPPException e) {
            e.printStackTrace();
        }
        return card.getLastName();
    }

    public String getHeaderImagePaht(String user) {
        VCard card = new VCard();
        try {
            card.load(mConnect,user);
        } catch(XMPPException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(card.getAvatar());

        byte buf[] = new byte[1024];
        String dirPath = Environment.getExternalStorageDirectory() + "/.IMTONG/Vcard/Head/";
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String fileName =  dirPath + user + ".png";
        File download = new File(fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(download);
            do {
                int numread = bais.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);
            bais.close();
            fos.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    void getBasicFriendInfo(String user,String fileName,String male,String age) {

    }
}
