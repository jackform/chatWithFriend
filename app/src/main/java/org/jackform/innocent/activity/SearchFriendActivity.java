package org.jackform.innocent.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jackform.innocent.R;
import org.jackform.innocent.adapter.SearchUserListAdapter;
import org.jackform.innocent.data.DataEngine;
import org.jackform.innocent.data.FriendInfo;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.SearchFriendList;
import org.jackform.innocent.data.request.AddFriendRequest;
import org.jackform.innocent.data.request.SearchUserListRequest;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.widget.BaseActivity;
import org.jackform.innocent.widget.SearchEdiText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackform on 15-8-26.
 */
public class SearchFriendActivity extends BaseActivity implements DataFetcher.ExecuteListener{

    private SearchEdiText mEdtSearch;
    private ListView mSearchedUserList;
    private SearchUserListAdapter mAdapter;


    @Override
    protected void onPause() {
        super.onPause();
        mDataFetcher.removeExecuteListener(this.getCaller());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        setTitle("查找用户");
        setLeftIconVisible();
        mEdtSearch = (SearchEdiText)findViewById(R.id.edit_search);
        mSearchedUserList = (ListView)findViewById(R.id.search_user_list);
        mAdapter = new SearchUserListAdapter(this);
        mSearchedUserList.setAdapter(mAdapter);
        mSearchedUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendInfo friendInfo = (FriendInfo) parent.getAdapter().getItem(position);
                if(friendInfo != null) {
                    showAddFriendDialog(friendInfo.getmUserName(),friendInfo.getmJabberID());
                }
            }
        });

        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String keyword = mEdtSearch.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    showProgressDialog("搜索用户", "搜索中...");
                    requestSearchUserList(keyword);
                }
                return true;
            }
        });
        mDataFetcher.addExecuteListener(this);
    }

    public void showAddFriendDialog(final String username,final String userJabberID) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("添加好友");
        alertDialogBuilder
                .setMessage("是否要添加"+username+"为好友?")
                .setCancelable(false)
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestAddFriend(username);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void requestAddFriend(String friendUsername) {
        AddFriendRequest request = new AddFriendRequest(friendUsername);
        mDataFetcher.executeRequest(this, RequestConstant.REQUEST_ADD_FRIEND,request);
    }

    void requestSearchUserList(String keyword) {
        SearchUserListRequest request = new SearchUserListRequest(keyword);
        mDataFetcher.executeRequest(this, RequestConstant.REQUEST_SEARCH_USER_LIST,request);
    }

    @Override
    public int getCaller() {
        return this.hashCode();
    }

    @Override
    public void onExecuteResult(int responseID, Bundle requestTask) {
        switch(responseID) {
            case ResponseConstant.SEARCHUSERS_ID: {
                if(requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.SUCCESS_CODE)) {
                    String jsonStr = requestTask.getString(ResponseConstant.PARAMS);
                    DebugLog.v("searchUser:"+jsonStr);
                    Gson gson = new Gson();
                    SearchFriendList searchFriendList = gson.fromJson(jsonStr,SearchFriendList.class);
                    List<FriendInfo> users = searchFriendList.getMfriendList();
                    mAdapter.setData(users);
                    dissmissprogressDialog();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                }
                break;
            }
            case ResponseConstant.ADD_FRIEND_ID: {
                if(requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.SUCCESS_CODE)) {
                    toast("添加成功!");
                    DataEngine.getInstance().setFriendListUpdated();
                } else if(requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.USER_ALREADY_ADD)){
                    toast("该用户已被添加为好友");
                } else if(requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.CANNOT_ADD_ME_AS_FRIEND)) {
                    toast("不能添加自己为好友");
                } else {
                    toast("后台出错,请稍候再试");
                }
                break;
            }
            case ResponseConstant.RECEIVE_FRIEND_REQEST_ID: {
                String friendUsername =  requestTask.getString(ResponseConstant.PARAMS);
                DebugLog.v("receive "+friendUsername+" 's friend request");
                requestAddFriend(friendUsername);
                break;
            }
            default:
                break;
        }

    }
}
