package org.jackform.innocent.activity;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import org.jackform.innocent.R;
import org.jackform.innocent.widget.BaseActivity;

public class ChatActivity extends BaseActivity {
	private final static String TAG = "CHAT";
	/*
	private String mFriend;
	private Chat mChat;
	private ChatManager chatManager;
*/

	@Override
	protected void onPause() {
		super.onPause();
//		chatManager.removeChatListener(mChatManagerListener);
//		mChat.removeMessageListener(mMessageListener);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

//		setContentView(R.layout.activity_chat);
//		mFriend = getIntent().getStringExtra("FRIEND_INFO");
//		chatManager = XmppUtils.getConnection().getChatManager();
//		mChat = chatManager.createChat(mFriend, null);
//		chatManager.addChatListener(mChatManagerListener);
	}

	/*
	ChatManagerListener mChatManagerListener = new ChatManagerListener() {

		@Override
		public void chatCreated(Chat chat, boolean able) {
			chat.addMessageListener(mMessageListener);
		}
	};

	MessageListener mMessageListener = new MessageListener() {

		@Override
		public void processMessage(Chat arg0, Message arg1) {
			Log.v(TAG, "==> Received: " + arg1.getFrom() + ":" + arg1.getBody());
		}
	};
	*/
	/*
	 * 连接服务器后注册用户
	 * 
	 * new Thread() { public void run() { ConnectionConfiguration connConfig =
	 * new ConnectionConfiguration(hostIP);
	 * connConfig.setReconnectionAllowed(true);
	 * connConfig.setSecurityMode(SecurityMode.disabled); //
	 * SecurityMode.required/disabled
	 * connConfig.setSASLAuthenticationEnabled(false); // true/false
	 * connConfig.setCompressionEnabled(false); // 配置服务器 connection = new
	 * XMPPConnection(connConfig);
	 * 
	 * try { // 连接服务器 Log.v(TAG,"==> start login"); connection.connect();
	 * Log.v(TAG,"==> connected!");
	 * 
	 * Registration reg = new Registration(); reg.setType(IQ.Type.SET);
	 * reg.setTo(connection.getServiceName()); reg.setUsername("queenform");
	 * reg.setPassword("123"); reg.addAttribute("android",
	 * "geolo_createUser_android"); PacketFilter filter = new AndFilter(new
	 * PacketIDFilter(reg.getPacketID()),new PacketTypeFilter(IQ.class));
	 * PacketCollector collector = connection.createPacketCollector(filter);
	 * connection.sendPacket(reg); IQ result =
	 * (IQ)collector.nextResult(SmackConfiguration.getPacketReplyTimeout()); //
	 * Stop queuing results collector.cancel();//停止请求results（是否成功的结果） if (result
	 * == null) { Log.v("xmppMainRegiter","No response from server.");
	 * Toast.makeText(ChatActivity.this,"服务器没有返回结果", Toast.LENGTH_SHORT).show();
	 * } else if (result.getType() == IQ.Type.ERROR) {
	 * if(result.getError().toString().equalsIgnoreCase("conflict(409)")){
	 * Log.e("xmppMainRegiter", "IQ.Type.ERROR: "+result.getError().toString());
	 * Toast.makeText(ChatActivity.this, "这个账号已经存在", Toast.LENGTH_SHORT).show();
	 * } else { Log.e("xmppMainRegiter",
	 * "IQ.Type.ERROR: "+result.getError().toString());
	 * Toast.makeText(ChatActivity.this, "注册失败", Toast.LENGTH_SHORT).show(); } }
	 * else if (result.getType() == IQ.Type.RESULT){ // setToText(R.id.userid,
	 * registerUserName.getText().toString()); // setToText(R.id.password,
	 * registerPassword.getText().toString()); Toast.makeText(ChatActivity.this,
	 * "恭喜你注册成功", Toast.LENGTH_SHORT).show(); } } catch(XMPPException e) {
	 * e.printStackTrace(); } }
	 * 
	 * }.start();
	 */

	/*
	 * new Thread() { public void run() { ConnectionConfiguration connConfig =
	 * new ConnectionConfiguration(hostIP);
	 * connConfig.setReconnectionAllowed(true);
	 * connConfig.setSecurityMode(SecurityMode.disabled); //
	 * SecurityMode.required/disabled
	 * connConfig.setSASLAuthenticationEnabled(false); // true/false
	 * connConfig.setCompressionEnabled(false); // 配置服务器 connection = new
	 * XMPPConnection(connConfig);
	 * 
	 * try { // 连接服务器 Log.v(TAG,"==> start login"); connection.connect();
	 * Log.v(TAG,"==> connected!"); // 用户登录 connection.login("jackform",
	 * "awfityvi"); Log.v(TAG,"==> login completed!"); String path = "aaa"; //
	 * File file = new File(path); FileTransferManager transferManager = new
	 * FileTransferManager(connection); OutgoingFileTransfer
	 * outgoingFileTransfer =
	 * transferManager.createOutgoingFileTransfer("admin@jackform-k42jr"); //
	 * 发送文件 // outgoingFileTransfer.sendFile(file, file.getName()); // 接收文件监听
	 * transferManager.addFileTransferListener(new FileTransferListener() {
	 * 
	 * @Override public void fileTransferRequest(FileTransferRequest request) {
	 * try {
	 * 
	 * 
	 * File sdDir = null; boolean sdCardExist =
	 * Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	 * //判断sd卡是否存在 if (sdCardExist) { sdDir =
	 * Environment.getExternalStorageDirectory();//获取跟目录 }
	 * Log.v("sdcardDir",sdDir.toString()); // 接收文件 IncomingFileTransfer
	 * transfer = request.accept(); // 接收文件存放的位置 transfer.recieveFile(new
	 * File(sdDir.toString()+"/aaa.txt"));
	 * 
	 * 
	 * } catch (Exception e) { Log.e("RecFile Ex In!",e.getMessage()); } } });
	 * 
	 * } catch(XMPPException e) { e.printStackTrace(); } } }.start();
	 */

	/*
	 * 连接\登录\发送消息和接收消息
	 * 
	 * new Thread() { public void run() { ConnectionConfiguration connConfig =
	 * new ConnectionConfiguration(hostIP);
	 * connConfig.setReconnectionAllowed(true);
	 * connConfig.setSecurityMode(SecurityMode.disabled); //
	 * SecurityMode.required/disabled
	 * connConfig.setSASLAuthenticationEnabled(false); // true/false
	 * connConfig.setCompressionEnabled(false); // 配置服务器 connection = new
	 * XMPPConnection(connConfig);
	 * 
	 * try { // 连接服务器 Log.v(TAG,"==> start login"); connection.connect();
	 * Log.v(TAG,"==> connected!"); // 用户登录 connection.login("jackform",
	 * "awfityvi"); Log.v(TAG,"==> login completed!"); Roster roster =
	 * connection.getRoster(); Collection<RosterEntry> entries =
	 * roster.getEntries(); for (RosterEntry entry : entries) {
	 * Log.v(TAG,"[Name] "+entry.getName());
	 * Log.v(TAG,"[User] "+entry.getUser());
	 * Log.v(TAG,"[Group] "+entry.getGroups());
	 * Log.v(TAG,"[Status] "+entry.getStatus()); } // 向另一个用户发出聊天 String
	 * loginAddr = "admin@jackform-k42jr";
	 * Log.v(TAG,"==>[loginAddr]: "+loginAddr); ChatManager chatManager =
	 * connection.getChatManager(); Chat chat =
	 * chatManager.createChat(loginAddr, null); chatManager.addChatListener(new
	 * ChatManagerListener() {
	 * 
	 * @Override public void chatCreated(Chat chat, boolean able) {
	 * chat.addMessageListener(new MessageListener() {
	 * 
	 * @Override public void processMessage(Chat arg0, Message arg1) {
	 * Log.v(TAG,"==> Received: "+arg1.getFrom()+":"+arg1.getBody()); try {
	 * arg0.sendMessage("我已收到"); } catch (XMPPException e) {
	 * e.printStackTrace(); } } }); } });
	 * 
	 * // 发送聊天信息 chat.sendMessage("Hello!"); } catch(Exception e) {
	 * e.printStackTrace(); } } }.start();
	 */
}
