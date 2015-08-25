package org.jackform.innocent.activity;

import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RadioGroup;

import org.jackform.innocent.R;
import org.jackform.innocent.fragment.FragmentFactory;
import org.jackform.innocent.fragment.FriendListFragment;
import org.jackform.innocent.fragment.PersonalInfoFragment;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.widget.BaseActivity;

import android.widget.RadioGroup.OnCheckedChangeListener;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    private Bitmap scaleBitmap(Bitmap photo) {
        int picOriginalWidth = photo.getWidth();
        int picOriginalHeight = photo.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        Matrix matrix = new Matrix();
        float scaleWidth = 2.0f / 3 * screenWidth / picOriginalWidth;
        float scaleHeight = 2.0f / 3 * screenHeight / picOriginalHeight;
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(photo, 0, 0, picOriginalWidth,
                picOriginalHeight, matrix, true);
        return newbmp;
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        DebugLog.v("open album result:"+resultCode);
        if (RESULT_OK != resultCode)
            return;
        switch (requestCode) {
            case PersonalInfoFragment.CHOOSE_PICTURE:
                if (data != null) {
                    Uri originalUri = data.getData();
                    Bitmap photo = null;
                    DebugLog.v(originalUri.toString());
                    DebugLog.v(originalUri.getPath());
                    String[] proj = { MediaStore.Images.Media.DATA };
                    CursorLoader loader = new CursorLoader(MainTabActivity.this,
                            originalUri, proj, null, null, null);
                    Cursor cursor = loader.loadInBackground();
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    DebugLog.v(cursor.getString(column_index));
                    ContentResolver resolver = getContentResolver();
                    try {
                        photo = MediaStore.Images.Media.getBitmap(resolver,
                                originalUri);
                        if (photo != null) {
                            Bitmap newbmp = scaleBitmap(photo);
                            // photo.recycle();
                            DebugLog.v("after scale");
                            if(null != oldFragment && oldFragment instanceof PersonalInfoFragment){
                                DebugLog.v("current is in the PersonalInfoFragment");
                                //TODO save bitmap as myself.png
                                ((PersonalInfoFragment)oldFragment).isPersonalInfoModified = 1;
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                break;
        }
    }
}
