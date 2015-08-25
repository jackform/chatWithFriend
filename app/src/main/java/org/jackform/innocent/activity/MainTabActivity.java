package org.jackform.innocent.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.widget.RadioGroup;

import org.jackform.innocent.R;
import org.jackform.innocent.fragment.FragmentFactory;
import org.jackform.innocent.fragment.FriendListFragment;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.widget.BaseActivity;

import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * Created by jackform on 15-6-8.
 */
public class MainTabActivity extends BaseActivity implements DataFetcher.ExecuteListener {

    private RadioGroup underLineTab;
    private FragmentManager	fragmentManager;
    private Fragment oldFragment = null;
    private int lastClickId;
    private Toolbar mToolBar;
    private String mAccount;
    private String mJabberID;
    private String mImageHeaderPath;
    private String mMale;
    private String mAge;

    public String getmAccount() {
        return mAccount;
    }

    public void setmAccount(String mAccount) {
        this.mAccount = mAccount;
    }

    public String getmJabberID() {
        return mJabberID;
    }

    public void setmJabberID(String mJabberID) {
        this.mJabberID = mJabberID;
    }

    public String getmImageHeaderPath() {
        return mImageHeaderPath;
    }

    public void setmImageHeaderPath(String mImageHeaderPath) {
        this.mImageHeaderPath = mImageHeaderPath;
    }

    public String getmMale() {
        return mMale;
    }

    public void setmMale(String mMale) {
        this.mMale = mMale;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
//        mDataFetcher.setResultListener(this);
        //设置actionBar
        mToolBar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
//        mToolBar.setNavigationIcon(R.mipmap.nav_btn_back);
        fragmentManager = getSupportFragmentManager();
        underLineTab = (RadioGroup)findViewById(R.id.tab_view);
        underLineTab.check(R.id.friendlist_tab);
        underLineTab.setOnCheckedChangeListener(onTabSwitchListener);
        //display first tab fragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = FragmentFactory.getInstanceByIndex(R.id.friendlist_tab);
//        transaction.replace(R.id.content_view,fragment,String.valueOf(R.id.friend_list));
        transaction.replace(R.id.content_view, fragment, null);
        transaction.commit();
        oldFragment = fragment;
        lastClickId = R.id.friendlist_tab;


        Log.v("hahaha","onCreate");


        mAccount = getIntent().getStringExtra("ACCOUNT");




//        showDialog();
        /*
        int code = 1;
        PendingIntent pendingIntent = PendingIntent.getActivity(this,11,new Intent(this,MainTabActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        HeadsUpManager manager = HeadsUpManager.getInstant(getApplication());
        HeadsUp.Builder builder = new HeadsUp.Builder(this);
        builder.setContentTitle("提醒").setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS).setSmallIcon(R.drawable.account_icon).setContentIntent(pendingIntent).setFullScreenIntent(pendingIntent,false).setContentText("你有新的消息");
        HeadsUp headsUp = builder.buildHeadUp();
        headsUp.setSticky(true);
        manager.notify(code++,headsUp);
        */
    }


    void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Your Title");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        MainTabActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    //tab切换时的，改变content_view的内容
    private OnCheckedChangeListener onTabSwitchListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup parent, int checkedId) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            if ( lastClickId < checkedId )
//                transaction.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
//            else
//                transaction.setCustomAnimations(R.anim.in_from_left,R.anim.out_to_right);
            lastClickId = checkedId;
            Fragment newFragment = fragmentManager.findFragmentByTag(String.valueOf(checkedId));
            if ( null == newFragment ) {
                newFragment = FragmentFactory.getInstanceByIndex(checkedId);
                transaction.hide(oldFragment);
                transaction.add(R.id.content_view, newFragment,String.valueOf(checkedId));
            } else {
                transaction.hide(oldFragment);
                transaction.show(newFragment);
            }
            oldFragment = newFragment;
            transaction.commit();

        }
    };

    @Override
    public int getCaller() {
        return this.hashCode();
    }

    @Override
    public void onExecuteResult(int responseID, Bundle requestTask) {
        switch(responseID) {
        }

    }
}
