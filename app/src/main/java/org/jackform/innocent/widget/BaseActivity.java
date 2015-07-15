package org.jackform.innocent.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.request.UnBindTaskRequest;
import org.jackform.innocent.utils.DataFetcher;

/**
 * @author jackform
 * @version 1.0
 *
 */
public abstract class BaseActivity extends AppCompatActivity {
//    private InputMethodManager mInputManager;
//    protected INetworkService.Stub mNetworkHandler;
//    protected abstract boolean needToConnectService();
    protected DataFetcher mDataFetcher;
    @Override
    protected void onCreate(Bundle savedInstantceState) {
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstantceState);
        mDataFetcher = new DataFetcher(this);
//        mInputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void unBindRemoteService() {
        UnBindTaskRequest request = new UnBindTaskRequest();
        mDataFetcher.executeRequest(RequestConstant.REQUEST_UNBIND,request);
        mDataFetcher.unbindService();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_main);
    }

    protected void toast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
