package org.jackform.innocent.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jackform.innocent.R;
import org.jackform.innocent.activity.ChatActivity;
import org.jackform.innocent.activity.MainTabActivity;
import org.jackform.innocent.adapter.FriendListExpandableAdapter;

import android.os.Handler.Callback;

public class FriendListFragment extends BaseFragment {
	private FriendListExpandableAdapter friendListAdapter;
	private ExpandableListView friendListView;
	private Context mContext;

	private Callback mCallback = new Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			switch(msg.what) {
				case 0x1:
                    if(null == tv ) {
                        Log.v("hahaha","tv is null");
                    } else {
						Log.v("hahaha","append the message");
                        tv.append(" nine old fucker");
                    }
                    break;
				default:
					break;
			}
			return false;
		}
	};
	private Handler mHanler = new Handler(mCallback);



	public void transFromActivity(final String message) {
		Log.v("hahaha","obtain the message from TabActivity:"+message);
//		tv.append(message);
		Bundle bundle = getArguments();
		String a = bundle.getString("hahaha");
		Log.v("hahaha","obtain the message from TabActivity:"+a);


		/*
		((Activity)mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tv.append(message);
			}
		});
		*/

	}

	@Override
	public boolean isUse() {
		return true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		((MainTabActivity)activity).setHandler(mHanler);
		Log.v("hahaha","onAttach");
	}
	/*
	private List<RosterGroup> getGroups(Roster roster) {
		List<RosterGroup> groupsList = new ArrayList<RosterGroup>();
		Collection<RosterGroup> rosterGroup = roster.getGroups();
		for (RosterGroup r : rosterGroup) {
			groupsList.add(r);
		}
		return groupsList;
		gvo
	}
	*/


	private TextView tv;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.activity_main, null);
		tv = (TextView)view.findViewById(R.id.test);

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
						Object child = friendListAdapter.getChild(groupPosition, childPosition);
						Intent intent = new Intent(mContext, ChatActivity.class);
						Log.v("",child.toString());
						intent.putExtra("FRIEND_INFO",child.toString());
						startActivity(intent);
						return false;
					}
				});
//		obtainFriendList o = new obtainFriendList();
//		o.start();
		Log.v("hahaha","onCreateView");
		return view;
	}

	/*
	class obtainFriendList extends Thread {
		public void run() {
			Roster roster = XmppUtils.getConnection().getRoster();
			friendListAdapter.setGroupList(getGroups(roster));
			friendListAdapter.notifyDataSetChanged();
		}
	}
	*/
}
