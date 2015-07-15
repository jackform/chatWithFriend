package org.jackform.innocent.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.jackform.innocent.R;

public class FriendListExpandableAdapter extends  BaseExpandableListAdapter {
	private Context mContext;
	private String[] armTypes = new String []{"aaa","bbb","ccc"};
	private String[][] arms = new String[][] {{"a1","a2"},{"b1","b2"},{"c1","c2"}};
	
//	private List<RosterGroup> mGroupList;
//	private List<List<RosterEntry>> mEntris;
	/*
	public void setGroupList(List<RosterGroup> groupList)
	{
		mGroupList = groupList;
		mEntris = new ArrayList<List<RosterEntry>>();
		for(RosterGroup rosterGroup : groupList) {
			List<RosterEntry> rosterEntries = new ArrayList<RosterEntry>();
			for(RosterEntry r :	rosterGroup.getEntries())
				rosterEntries.add(r);
			mEntris.add(rosterEntries);
		}
	}
	*/
	
	public FriendListExpandableAdapter(Context context) {
		mContext = context;
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return arms[groupPosition][childPosition];
//		return mEntris.get(groupPosition).get(childPosition).getUser();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		LayoutInflater inflate = LayoutInflater.from(mContext);
		convertView = inflate.inflate(R.layout.item_friendlist_child, null);
		TextView friendName = (TextView)convertView.findViewById(R.id.ct_name);
		friendName.setText(arms[groupPosition][childPosition]);
//		friendName.setText(mEntris.get(groupPosition).get(childPosition).getName());
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return arms[groupPosition].length;
//		return null == mEntris ? 0 : mEntris.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
//		return mEntris.get(groupPosition);
		return armTypes[groupPosition];
	}

	@Override
	public int getGroupCount() {
//		return null == mEntris ? 0 : mEntris.size();
		return armTypes.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		LayoutInflater inflate = LayoutInflater.from(mContext);
		convertView = inflate.inflate(R.layout.item_friendlist_group, null);
		TextView groupName = (TextView) convertView.findViewById(R.id.groupName);
//		groupName.setText(mGroupList.get(groupPosition).getName());
		groupName.setText(armTypes[groupPosition].toString());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
