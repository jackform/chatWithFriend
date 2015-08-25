package org.jackform.innocent.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jackform.innocent.R;
import org.jackform.innocent.activity.MainTabActivity;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;

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

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_person_info, null);
        if(TextUtils.isEmpty(((MainTabActivity) mContext).getmJabberID())) {
            requestPersonalInfo();
        } else {
            mJabberID = ((MainTabActivity)mContext).getmJabberID();
            mAccountName = ((MainTabActivity)mContext).getmAccount();
            mAge = ((MainTabActivity)mContext).getmAge();
            mHeaderImagePath = ((MainTabActivity)mContext).getmImageHeaderPath();
            mMale = ((MainTabActivity) mContext).getmMale();
        }
        return view;
    }

    private void requestPersonalInfo() {

    }

    @Override
    public int getCaller() {
        return 0;
    }

    @Override
    public void onExecuteResult(int responseID, Bundle requestTask) {

    }
}
