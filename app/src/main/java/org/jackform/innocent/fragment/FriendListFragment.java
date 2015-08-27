package org.jackform.innocent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import org.jackform.innocent.activity.MainTabActivity;
import org.jackform.innocent.data.APPConstant;
import org.jackform.innocent.data.DataEngine;
import org.jackform.innocent.data.FriendInfo;
import org.jackform.innocent.R;
import org.jackform.innocent.activity.ChatActivity;
import org.jackform.innocent.adapter.FriendListExpandableAdapter;
import org.jackform.innocent.data.FriendList;
import org.jackform.innocent.data.RequestConstant;
import org.jackform.innocent.data.ResponseConstant;
import org.jackform.innocent.data.request.GetFriendListRequest;
import org.jackform.innocent.data.result.GetFriendListResult;
import org.jackform.innocent.utils.DataFetcher;
import org.jackform.innocent.utils.DebugLog;
import org.jackform.innocent.widget.BaseActivity;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FriendListFragment extends BaseFragment implements DataFetcher.ExecuteListener {
	private FriendListExpandableAdapter friendListAdapter;
	private ExpandableListView friendListView;
	private Context mContext;
	private DataFetcher mDataFetcher;

	@Override
	public boolean isUse() {
		return true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		getActivity().setTitle("好友列表");
		((BaseActivity)getActivity()).setRightIconVisible();
		Log.v("hahaha", "onAttach");
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden) {
			getActivity().setTitle("好友列表");
			((BaseActivity)getActivity()).setRightIconVisible();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(DataEngine.getInstance().isFriendListUpdated()) {
			requestFriendList();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		DebugLog.v("onDetach");
        mDataFetcher.removeExecuteListener(this.getCaller());
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_main, null);
		friendListAdapter = new FriendListExpandableAdapter(mContext);
		friendListView = (ExpandableListView) view
				.findViewById(R.id.friend_list);
		friendListView.setAdapter(friendListAdapter);
		friendListView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
												View v, int groupPosition, int childPosition,
												long id) {
						FriendInfo child = (FriendInfo)friendListAdapter.getChild(groupPosition, childPosition);
						Intent intent = new Intent(mContext, ChatActivity.class);
						DebugLog.v(child.getmJabberID());
						DebugLog.v(child.getmUserName());
						intent.putExtra("FRIEND_INFO", child.getmJabberID());
						intent.putExtra("FRIEND_NAME", child.getmUserName());
						intent.putExtra(APPConstant.KEY_HEADER_IMAGE_PATH,child.getmHeaderImagePath());
						startActivity(intent);
						return false;
					}
				});
		mDataFetcher = DataFetcher.getInstance(((Activity) mContext).getApplication());
		mDataFetcher.addExecuteListener(this);
		requestFriendList();
//		mDataFetcher.addExecuteListener(r);
		return view;
	}

	private List<RosterGroup> getGroups(Roster roster) {
		List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		for (RosterGroup r : rosterGroup) {
			groupsList.add(r);
		}
		DebugLog.v("group size: " + groupsList.size());
		return groupsList;
	}

	void requestFriendList() {
		GetFriendListRequest request = new GetFriendListRequest();
		mDataFetcher.executeRequest(FriendListFragment.this, RequestConstant.REQUEST_GET_FRIEND_LIST, request);
		/*
		new Thread() {
			@Override
			public void run() { if (!XmppMethod.getInstance().isConnect()) XmppMethod.getInstance().connect();
				XmppMethod.getInstance().login("jackform", "awfityvi");
				Roster roster = XmppMethod.getInstance().getRoster();
				friendListAdapter.setGroupList(getGroups(roster));
				((Activity) mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						friendListAdapter.notifyDataSetChanged();
					}
				});
			}
		}.start();
		*/
	}

	@Override
	public int getCaller() {
		return this.hashCode();
	}

	@Override
	public void onExecuteResult(int responseID, Bundle requestTask) {
		//TODO some error deal with
		switch(responseID) {
    		case ResponseConstant.GET_FRIEND_LIST_ID:
				GetFriendListResult getFriendListResult = requestTask.getParcelable(ResponseConstant.PARAMS);
				String jsonStr = getFriendListResult.getmJsonFriendList();
				Gson gson = new Gson();
				DebugLog.v("the return friend list json is " + jsonStr);
				final FriendList friendList = gson.fromJson(jsonStr,FriendList.class);
				friendListAdapter.setData(friendList);
				((Activity)mContext).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						friendListAdapter.notifyDataSetChanged();
					}
				});

			break;
		}

	}
}

