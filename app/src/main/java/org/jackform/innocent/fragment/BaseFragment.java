package org.jackform.innocent.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;

public abstract class BaseFragment extends Fragment {
	public abstract boolean isUse();
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		DebugLog.v("onHiddeChanged:"+hidden);
	}

}
