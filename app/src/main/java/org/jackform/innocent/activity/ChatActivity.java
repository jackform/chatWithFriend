package org.jackform.innocent.activity;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.jackform.innocent.R;
import org.jackform.innocent.adapter.ChatContentListAdapter;
import org.jackform.innocent.data.APPConstant;
import org.jackform.innocent.data.PerChatItem;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.PersonalInfoRequest;
import org.jackform.innocent.data.request.SendChatMessageRequest;
import org.jackform.innocent.data.request.SendFileRequest;
import org.jackform.innocent.data.result.PersonalInfoResult;
import org.jackform.innocent.data.result.ReceiveChatMessageResult;
import org.jackform.innocent.data.result.SendChatMessageResult;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.utils.TimeUtils;
import org.jackform.innocent.widget.BaseActivity;
import org.jackform.innocent.xmpp.XmppMethod;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ChatActivity extends BaseActivity implements DataFetcher.ExecuteListener{
	private final static String TAG = "CHAT";
	private String mFriendJabberID;
	private String mFriendName;
	private Toolbar mStringToolBar;
	private ListView mChatContentList;
	private ChatContentListAdapter mAdapter;
	private ArrayList<PerChatItem> mChatContentDatas;
	private String  mCurrentSendMessage;
	private Toolbar mToolBar;
	private Button mBtnSend;
	private EditText mEdtChatContent;

	private Bitmap friendHeaderBitmap;
	private Bitmap myselfHeaderBitmap;
	private String friendHeaderPath;

	/*
	private String mFriend;
	private Chat ToolbarmChat;
	private ChatManager chatManager;
    */

	@Override
	protected void onPause() {
		super.onPause();
		if(this.isFinishing()) {
			DebugLog.v("onPause,finishing");
			mDataFetcher.removeExecuteListener(this.getCaller());
		} else {
			DebugLog.v("onPause,but not finishing");
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		mToolBar = (Toolbar)findViewById(R.id.tool_bar);
		setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.mipmap.nav_btn_back);
		mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mFriendJabberID = getIntent().getStringExtra("FRIEND_INFO");
		mFriendName = getIntent().getStringExtra("FRIEND_NAME");
		friendHeaderPath = getIntent().getStringExtra(APPConstant.KEY_PRIEND_HEADER_IMAGE);

		DebugLog.v(mFriendJabberID);
		DebugLog.v("username is" + mFriendName);
		mToolBar.setSubtitle("您正在与" + mFriendName + "聊天中");

		mChatContentList = (ListView)findViewById(R.id.main_chat_form);
		mChatContentDatas = new ArrayList<>();
		mAdapter = new ChatContentListAdapter(this,mChatContentDatas);
		mChatContentList.setAdapter(mAdapter);

		mDataFetcher.addExecuteListener(this);

		mEdtChatContent = (EditText)findViewById(R.id.chat_content);


		mBtnSend = (Button)findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sendChatContent = mEdtChatContent.getText().toString();
				if(TextUtils.isEmpty(sendChatContent)) {
					toast("发送消息不能为空");
					return;
				}
                sendChatContenetRequest(sendChatContent);
			}
		});

		requestPersonalInfo();
//		setContentView(R.layout.activity_chat);
//		mFriend = getIntent().getStringExtra("FRIEND_INFO");
//		chatManager = XmppUtils.getConnection().getChatManager();
//		mChat = chatManager.createChat(mFriend, null);
//		chatManager.addChatListener(mChatManagerListener);
	}

	void requestPersonalInfo() {
		PersonalInfoRequest request = new PersonalInfoRequest();
		mDataFetcher.executeRequest(ChatActivity.this,RequestConstant.REQUEST_GET_PERSONAL_INFO,request);
	}

	void sendChatContenetRequest(String content) {
		mCurrentSendMessage = content;
		String currentTime = TimeUtils.getCurrentTime();
		PerChatItem perChatItem = new PerChatItem(true,content,currentTime);
		//set sending progress
		perChatItem.setIsSending();
		mChatContentDatas.add(perChatItem);
		mAdapter.notifyDataSetChanged();
		mChatContentList.setSelection(mChatContentDatas.size() - 1);
		mEdtChatContent.setText("");
		//send request
		SendChatMessageRequest sendChatMessageRequest = new SendChatMessageRequest(mFriendJabberID, content, currentTime);
		mDataFetcher.executeRequest(this, RequestConstant.REQUEST_SEND_CHAT_MESSAGE,sendChatMessageRequest);

	}

	@Override
	public int getCaller() {
		return this.hashCode();
	}





	@Override
	public void onExecuteResult(int responseID, Bundle requestTask) {
		DebugLog.v("enter the chat activity onExecuteResult");
		switch(responseID) {
			case ResponseConstant.SEND_CHAT_MESSAGE_ID:
				if(requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.SUCCESS_CODE)) {
					SendChatMessageResult sendChatMessageResult = requestTask.getParcelable(ResponseConstant.PARAMS);
					String jabberID = sendChatMessageResult.getJabberID();
					String message = sendChatMessageResult.getSendMessage();
					String sendTime = sendChatMessageResult.getmSendTime();
					DebugLog.v("jabberID:"+jabberID);
					DebugLog.v("message:"+message);
					DebugLog.v("sendTime:"+sendTime);
					if(jabberID.startsWith(mFriendJabberID)) {

						for(PerChatItem perChatItem : mChatContentDatas) {
							if(perChatItem.getDate().equals(sendTime)) {
								perChatItem.setSendMessageCompleted();
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										mAdapter.notifyDataSetChanged();
										mChatContentList.setSelection(mChatContentDatas.size() - 1);
										mEdtChatContent.setText("");
									}
								});
								break;
							}
						}


					} else {
						//TODO if jabber doesn't equal current friendJabberID
					}
				}
				break;
			case ResponseConstant.RECEIVE_CHAT_MESSAGE_ID:
				ReceiveChatMessageResult receiveChatMessageResult = requestTask.getParcelable(ResponseConstant.PARAMS);
				String from = receiveChatMessageResult.getmUserJabberID();
				DebugLog.v(from);
				if(from.startsWith(mFriendJabberID)) {
					mFriendJabberID = from;
					DebugLog.v(receiveChatMessageResult.getmReceiveMessage());
					mChatContentDatas.add(new PerChatItem(false, receiveChatMessageResult.getmReceiveMessage(),TimeUtils.getCurrentTime()));
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mAdapter.notifyDataSetChanged();
							mChatContentList.setSelection(mChatContentDatas.size() - 1);
						}
					});

					/* send file
					String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.txt";
					SendFileRequest sendFileRequest = new SendFileRequest(mFriendJabberID,filePath);
					mDataFetcher.executeRequest(this,RequestConstant.REQUEST_SEND_FILE,sendFileRequest);
					*/
				}
				break;
			case ResponseConstant.GET_PERSONAL_INFO_ID: {
				PersonalInfoResult result = requestTask.getParcelable(ResponseConstant.PARAMS);
				String myselfHeaderImagePath = result.getmImageHeaderPath();
				myselfHeaderBitmap = BitmapFactory.decodeFile(myselfHeaderImagePath);
				friendHeaderBitmap = BitmapFactory.decodeFile(friendHeaderPath);
				mAdapter.setBitmap(myselfHeaderBitmap,friendHeaderBitmap);
				break;
			}
			default:
				break;
		}

	}

	public void createFile() {
		DebugLog.v(""+getFilesDir());
		DebugLog.v(""+Environment.getDownloadCacheDirectory().getAbsolutePath());
		DebugLog.v(""+Environment.getDataDirectory().getAbsolutePath());
		DebugLog.v(""+Environment.getExternalStorageDirectory().getAbsolutePath());
		DebugLog.v(""+Environment.getExternalStoragePublicDirectory("pub_test").getAbsolutePath());
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = new File(path,"test.txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void receiveFile() {
		new Thread() {
			public void run() {

				final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
				XMPPConnection connection = XmppMethod.getInstance().getConnection();
				try {
					connection.connect();
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				XmppMethod.getInstance().login("jackform", "awfityvi");
				FileTransferManager manager = new FileTransferManager(XmppMethod.getInstance().getConnection());
				OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(mFriendJabberID);
				try {
					transfer.sendFile(new File(path),"data backup");
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				manager.addFileTransferListener(new FileTransferListener() {
					@Override
					public void fileTransferRequest(FileTransferRequest fileTransferRequest) {
						DebugLog.v("has file incoming");
						IncomingFileTransfer transfer = fileTransferRequest.accept();
						try {
							transfer.recieveFile(new File(path, "a.txt"));
						} catch (XMPPException e) {
							e.printStackTrace();
						}

					}
				});
			}

		}.start();

	}
	private void sendFile(String user,XMPPConnection conn,File file) throws XMPPException {
		FileTransferManager manager = new FileTransferManager(conn);
		OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(user);
//		File afile = new File(Environmentonment.getExternalStoragePublicDirectory());
		transfer.sendFile(file, "data backup");
		manager.addFileTransferListener(new FileTransferListener() {
			@Override
			public void fileTransferRequest(FileTransferRequest request) {
				try {
					DebugLog.v("has file coming!");
					IncomingFileTransfer transfer = request.accept();
					transfer.recieveFile(new File("complete_work.txt"));
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		});

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
