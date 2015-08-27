package org.jackform.innocent.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import org.jackform.innocent.R;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.request.SearchUserListRequest;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.widget.BaseActivity;
import org.jackform.innocent.widget.SearchEdiText;

/**
 * Created by jackform on 15-8-26.
 */
public class SearchFriendActivity extends BaseActivity implements DataFetcher.ExecuteListener{

    private SearchEdiText mEdtSearch;


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

    }
}
