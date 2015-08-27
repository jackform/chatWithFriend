package org.jackform.innocent.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.jackform.innocent.R;
import org.jackform.innocent.adapter.SearchUserListAdapter;
import org.jackform.innocent.data.FriendInfo;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.SearchFriendList;
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
        mEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String keyword = mEdtSearch.getText().toString();
                if(!TextUtils.isEmpty(keyword)) {
                    requestSearchUserList(keyword);
                }
                return false;
            }
        });
        mDataFetcher.addExecuteListener(this);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                }
                break;
            }
            default:
                break;
        }

    }
}
