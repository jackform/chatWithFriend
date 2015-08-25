package org.jackform.innocent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import org.jackform.customwidget.CircleImageView;
import org.jackform.innocent.R;
import org.jackform.innocent.activity.MainTabActivity;
import org.jackform.innocent.data.APPConstant;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.PersonalInfoRequest;
import org.jackform.innocent.data.request.UpdatePersonalInfoRequest;
import org.jackform.innocent.data.result.PersonalInfoResult;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.widget.AccountEditText;
import org.jackform.innocent.widget.AgeEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cropper.CropImageView;

/**
 * Created by jackform on 15-8-24.
 */
public class PersonalInfoFragment extends BaseFragment  implements DataFetcher.ExecuteListener {
    private Context mContext;
    private String mJabberID;
    private String mMale;
    private String mAge;
    private String mHeaderImagePath;
    private String mAccountName;
    private DataFetcher mDataFetcher;

    private CircleImageView mHeaderImage;
    private AccountEditText mEtAccount;
    private AgeEditText mEtAge;
    private RadioButton mBtnMale;
    private RadioButton mBtnFemale;
    private Button mAlbumChoose;
    private Button mCameraChoose;
    private Button mBtnModityPersonalInfo;

    private View vPopWindow = null;
    private PopupWindow popWindow = null;
    public CropImageView cropImg;
    public int isPersonalInfoModified = 0;
    private Bitmap cropedBitmap = null;

    public static final int CHOOSE_PICTURE = 1;
    @Override
    public boolean isUse() {
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        Log.v("hahaha", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        DebugLog.v("onDetach");
    }

    View.OnClickListener onBtnModifiedClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            enbaleModify();
            mBtnModityPersonalInfo.setText("完 成");
            mBtnModityPersonalInfo.setOnClickListener(onBtnSubmitClicked);
            mAlbumChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCropImagePopWindow(v);
                }
            });
        }
    };

    void enbaleModify() {
        mCameraChoose.setEnabled(true);
        mAlbumChoose.setEnabled(true);
        mEtAge.setEnabled(true);
        mEtAge.setShowClearButton(true);
        mBtnMale.setEnabled(true);
        mBtnFemale.setEnabled(true);
    }

    View.OnClickListener onBtnSubmitClicked = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String currentMale = mBtnMale.isChecked() ? "男" : "女";
            String currentAge = mEtAge.getText().toString();

            requestModifyPersonalInfo(isPersonalInfoModified, currentMale, currentAge);
                    /*
                    Bitmap bitmap = BitmapFactory.decodeFile(mHeaderImagePath);
                    requestModifyPersonalInfo(bitmap,mAge,mMale);
                    */
        }
    };

    public void requestModifyPersonalInfo(final int isHeaderModified,final String age,final String male) {
        new Thread() {
            @Override
            public void run() {
                UpdatePersonalInfoRequest request = new UpdatePersonalInfoRequest(isHeaderModified,age,male);
                mDataFetcher.executeRequest(PersonalInfoFragment.this,RequestConstant.REQUEST_UPDATE_PERSONAL_INFO,request);
            }
        }.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_person_info, null);
        mHeaderImage = (CircleImageView)view.findViewById(R.id.head_image);
        mEtAccount = (AccountEditText)view.findViewById(R.id.et_register_account);
        mEtAge = (AgeEditText)view.findViewById(R.id.et_age);
        mBtnMale = (RadioButton)view.findViewById(R.id.male);
        mBtnFemale = (RadioButton)view.findViewById(R.id.female);
        mAlbumChoose = (Button)view.findViewById(R.id.album_choose);
        mCameraChoose = (Button)view.findViewById(R.id.camera_choose);
        mBtnModityPersonalInfo = (Button)view.findViewById(R.id.btn_modity_personal_info);
        mBtnModityPersonalInfo.setOnClickListener(onBtnModifiedClicked);

        mDataFetcher = DataFetcher.getInstance(((Activity) mContext).getApplication());
        mDataFetcher.addExecuteListener(this);

        mAccountName = ((MainTabActivity)mContext).getmAccount();
        if(TextUtils.isEmpty(((MainTabActivity) mContext).getmJabberID())) {
            requestPersonalInfo();
        } else {
            mJabberID = ((MainTabActivity)mContext).getmJabberID();
            mAge = ((MainTabActivity)mContext).getmAge();
            mHeaderImagePath = ((MainTabActivity)mContext).getmImageHeaderPath();
            mMale = ((MainTabActivity) mContext).getmMale();
        }
        return view;
    }

    protected void showCropImagePopWindow(View parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vPopWindow = inflater.inflate(R.layout.layout_crop_image, null, false);
        popWindow = new PopupWindow(vPopWindow, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        ColorDrawable color = new ColorDrawable(Color.TRANSPARENT);
        popWindow.setBackgroundDrawable(color);
        popWindow.setFocusable(true); // 设置PopupWindow可获得焦点
        popWindow.setTouchable(true); // //设置PopupWindow可触摸

        cropImg = (CropImageView) vPopWindow.findViewById(R.id.imgView);
        cropImg.setAspectRatio(30, 40);
        cropImg.setFixedAspectRatio(true);

        Button select = (Button) vPopWindow.findViewById(R.id.select);
        Button crop = (Button) vPopWindow.findViewById(R.id.crop);
        Button cancel = (Button) vPopWindow.findViewById(R.id.cancel);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CHOOSE_PICTURE);
            }
        });
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropedBitmap = cropImg.getCroppedImage();
                ((MainTabActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cropImg != null && cropedBitmap != null) {
                            mHeaderImage.setImageBitmap(cropedBitmap);
                            saveBitmapToMySelf(cropedBitmap);
                            isPersonalInfoModified = 1;
                            popWindow.dismiss();
                        }
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popWindow.dismiss();
            }
        });
        // popWindow.showAsDropDown(parent,-25,10);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    void saveBitmapToMySelf(Bitmap bitmap) {
        String dirPath = Environment.getExternalStorageDirectory() + "/.IMTONG/Vcard/Head/";
        String fileName =  dirPath + APPConstant.KEY_MYSELF_IMAGE_HEADER + ".png";
        File f = new File(fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestPersonalInfo() {
        PersonalInfoRequest request = new PersonalInfoRequest();
        mDataFetcher.executeRequest(PersonalInfoFragment.this, RequestConstant.REQUEST_GET_PERSONAL_INFO, request);
    }

    @Override
    public int getCaller() {
        PersonalInfoRequest request;
        return this.hashCode();
    }

    @Override
    public void onExecuteResult(int responseID, Bundle requestTask) {
        DebugLog.v("enter the personal info fragment execute result");
        switch(responseID) {
            case ResponseConstant.GET_PERSONAL_INFO_ID: {
                if(requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.SUCCESS_CODE)) {
                    PersonalInfoResult result =  requestTask.getParcelable(ResponseConstant.PARAMS);
                    final String jabberID = result.getmJabberID();
                    final String male = result.getmMale();
                    final String age = result.getmAge();
                    final String imagePath = result.getmImageHeaderPath();
                    DebugLog.v("result:" + "\njabberID:" + jabberID + " male:" + male + " age:" + age + " imagePath:" + imagePath);
                    final Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    ((MainTabActivity)mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mEtAccount.setText(mAccountName);
                            mEtAge.setText(age);
                            if (male != null && male.equals("男")) {
                                mBtnMale.setChecked(true);
                            } else if (male != null && male.equals("女")) {
                                mBtnFemale.setChecked(true);
                            }
                            mHeaderImage.setImageBitmap(bitmap);
                            diableModify();
                            mBtnModityPersonalInfo.setOnClickListener(onBtnModifiedClicked);
                        }
                    });
                } else {
                }
            }
            break;

            case ResponseConstant.UPDATE_PERSONAL_INFO_ID: {
                if (requestTask.getString(ResponseConstant.CODE).equals(ResponseConstant.SUCCESS_CODE)) {
                    ((MainTabActivity)mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            diableModify();
                            mBtnModityPersonalInfo.setText("修 改");
                            mBtnModityPersonalInfo.setOnClickListener(onBtnModifiedClicked);
                            isPersonalInfoModified = 0;
                        }
                    });
                }
            }
            break;

            default:
        }
    }

    void diableModify() {
        mEtAccount.setEnabled(false);
        mEtAccount.setShowClearButton(false);
        mEtAge.setEnabled(false);
        mEtAge.setShowClearButton(false);
        mBtnMale.setEnabled(false);
        mBtnFemale.setEnabled(false);
        mAlbumChoose.setEnabled(false);
        mCameraChoose.setEnabled(false);
    }
}
