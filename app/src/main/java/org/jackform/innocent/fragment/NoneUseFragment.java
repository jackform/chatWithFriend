package org.jackform.innocent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jackform.innocent.R;

public class NoneUseFragment extends BaseFragment {

	@Override
	public boolean isUse() {
		return false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_test, null);  
        TextView textView = (TextView) view.findViewById(R.id.testText);
        textView.setText("blablabla");
		return view;
	}
}
