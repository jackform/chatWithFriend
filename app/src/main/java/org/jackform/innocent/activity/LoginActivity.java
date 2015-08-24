package org.jackform.innocent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jackform.customwidget.RippleView;
import org.jackform.innocent.R;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.LoginTaskRequest;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.widget.BaseActivity;

public class LoginActivity extends BaseActivity implements DataFetcher.ExecuteListener {

    private EditText mEdtAccount;
    private EditText mEdtPassword;
    private TextView mRegister;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataFetcher.removeExecuteListener(this.getCaller());
    }

    @Override
    public int getCaller() {
        return this.hashCode();
    }

    @Override
    public void onExecuteResult(int responseID, Bundle result) {
        //TODO if result isn't sucess
        switch(responseID) {
            case ResponseConstant.LOGIN_ID:
                Intent intent = new Intent(LoginActivity.this,MainTabActivity.class);
                intent.putExtra("ACCOUNT","jackform");
                startActivity(intent);
                unBindRemoteService();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mDataFetcher.getResultListener(this);
        mDataFetcher.addExecuteListener(this);

        mEdtAccount = (EditText)findViewById(R.id.et_account);
        mEdtPassword = (EditText)findViewById(R.id.et_password);
        mRegister = (TextView)findViewById(R.id.register);
        RippleView mBtnSubmit = (RippleView)findViewById(R.id.btn_login);
        mBtnSubmit.setOnRippleCompleteListener(
                new RippleView.OnRippleCompleteListener() {
                   @Override
                   public void onComplete(RippleView rippleView) {
                       String account = mEdtAccount.getText().toString();
                       String password = mEdtPassword.getText().toString();
                       if(!TextUtils.isEmpty(account) &&
                               !TextUtils.isEmpty(password)) {
                           LoginTaskRequest params = new LoginTaskRequest(account,password);
                           mDataFetcher.executeRequest(LoginActivity.this,RequestConstant.REQUEST_LOGIN,params);

                       } else {
                           toast("账号密码不能为空");
                       }
                   }
               });

        mRegister.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mRegister.setTextColor(Color.GREEN);
                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }
                });
    }
}

/*
                        final String IP_ADRESS = "172.18.66.189";
                        final int PORT = 5222;
//                        SASLAuthentication.registerSASLMechanism(SASLAnonymous.class);//"DIGEST-MD5");//,org.jivesoftware.smack.sasl.SASLAnonymous.class);

                        XMPPTCPConnectionConfiguration conf = XMPPTCPConnectionConfiguration
                                .builder()
                                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                                .allowEmptyOrNullUsernames()
                                .setServiceName(IP_ADRESS)
                                .setHost(IP_ADRESS)
                                .setPort(PORT)
                                .setDebuggerEnabled(true)
                                .setSendPresence(false)
                                .setCompressionEnabled(false).build();

                        XMPPTCPConnection connection = new XMPPTCPConnection(conf);
                       // SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
                        //SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                        connection.addConnectionListener(new ConnectionListener() {
                            @Override
                            public void connected(XMPPConnection connection) {
                                AccountManager accountManager = AccountManager.getInstance(connection);
                                Map<String,String> map = new HashMap<String,String>();
                                try {
                                    accountManager.createAccount("778", "778",map);
                                } catch (SmackException.NoResponseException e) {
                                    e.printStackTrace();
                                } catch (XMPPException.XMPPErrorException e) {
                                    e.printStackTrace();
                                } catch (SmackException.NotConnectedException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void authenticated(XMPPConnection connection, boolean resumed) {

                            }

                            @Override
                            public void connectionClosed() {

                            }

                            @Override
                            public void connectionClosedOnError(Exception e) {

                            }

                            @Override
                            public void reconnectionSuccessful() {

                            }

                            @Override
                            public void reconnectingIn(int seconds) {

                            }

                            @Override
                            public void reconnectionFailed(Exception e) {

                            }
                        });
                        try {
                            connection.connect();

                            connection.disconnect();
//                            connection.login("jackform", "awfityvi");
                        } catch (XMPPException e) {
                            e.printStackTrace();
                        } catch (SmackException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Roster roster = Roster.getInstanceFor(connection);
                        if (!roster.isLoaded()) {
                            try {
                                roster.reloadAndWait();
                            } catch (SmackException.NotLoggedInException e) {
                                e.printStackTrace();
                            } catch (SmackException.NotConnectedException e) {
                                e.printStackTrace();
                            }
                        }

                        Collection<RosterEntry> entries = roster.getEntries();
                        RosterEntry last_entry = null;
                        for (RosterEntry entry : entries) {
                            last_entry = entry;
                        }
                        Log.v("hahaha", last_entry.getUser());

                        ChatManager chatManager = ChatManager.getInstanceFor(connection);
                        Chat newChat = chatManager.createChat(last_entry.getUser(), null);
                        chatManager.addChatListener(new ChatManagerListener() {
                            @Override
                            public void chatCreated(Chat chat, boolean createdLocally) {
                                chat.addMessageListener(new ChatMessageListener() {
                                    @Override
                                    public void processMessage(Chat chat, Message message) {
                                        //             chat.sendMessage(message);
                                        if (chat != null && message.getBody() != null) {
                                            Log.v("hahaha", message.getBody().toString());
                                        }
                                    }
                                });
                            }
                        });

                        Message message = new Message("I am jackform");
                        message.setBody("you are bitch!!!");
                        try {
                            newChat.sendMessage(message);
                        }  catch( SmackException.NotConnectedException e )  {
                                e.printStackTrace();
                        }
                        */


