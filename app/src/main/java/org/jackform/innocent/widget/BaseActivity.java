package org.jackform.innocent.widget;

import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jackform.customwidget.RippleView;
import org.jackform.innocent.R;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.request.UnBindTaskRequest;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;

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
    private Toolbar mToolBar;
    private TextView mToolbarTitle;
    private RippleView mLeftIcon;
    private RippleView mRightIcon;
    @Override
    protected void onCreate(Bundle savedInstantceState) {
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstantceState);
        mDataFetcher = DataFetcher.getInstance(getApplication());
//        mInputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void unBindRemoteService() {
//        UnBindTaskRequest request = new UnBindTaskRequest();
//        mDataFetcher.executeRequest(RequestConstant.REQUEST_UNBIND,request);
//        mDataFetcher.unbindService();
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        mToolBar = (Toolbar)findViewById(R.id.toolbar);
        if(mToolBar != null) {
            setSupportActionBar(mToolBar);
            mToolbarTitle = (TextView)mToolBar.findViewById(R.id.toolbar_title);
            mLeftIcon = (RippleView)mToolBar.findViewById(R.id.left_icon);
            mRightIcon = (RippleView)mToolBar.findViewById(R.id.right_icon);
            if(mToolbarTitle != null) {
                //with this,the mToolbarTitle can be displayed on the toolbar
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            if(mLeftIcon != null ) {
                mLeftIcon.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        onBackPressed();
                    }
                });
            }

            if(mRightIcon != null) {
                mRightIcon.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        onTitleRightPressed();
                    }
                });
            }
        }
    }

    public void setRightIconVisible() {
        if(mRightIcon != null) {
            mRightIcon.setVisibility(View.VISIBLE);
        }
    }

    public void setRightIconInvisible() {
        if(mRightIcon != null) {
           mRightIcon.setVisibility(View.GONE);
        }
    }

    public void setLeftIconVisible() {
        if(mLeftIcon != null) {
            mLeftIcon.setVisibility(View.VISIBLE);
        }
    }

    public void setLeftIconInvisible()  {
        if(mLeftIcon != null) {
            mLeftIcon.setVisibility(View.GONE);
        }
    }

    public void onTitleRightPressed() {

    }

    //This method is called when Activity's onPostCreate() and setTitle() is called.
    //we rewrite it to make setTitle() method to change toolbar 's title
    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mToolBar != null && mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        }
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
