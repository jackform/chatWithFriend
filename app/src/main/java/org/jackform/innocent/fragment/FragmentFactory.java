package org.jackform.innocent.fragment;

import org.jackform.innocent.R;
import android.support.v4.app.Fragment;

public class FragmentFactory {
	public static Fragment getInstanceByIndex(int index)
	{
		Fragment fragment = null;
		switch(index) {
		case R.id.friendlist_tab:
			fragment = new FriendListFragment();
			break;
        case R.id.tab2:
			fragment = new DynamicLoadFragment();
			break;
        case R.id.tab3:
			fragment = new PersonalInfoFragment();
     		break;
		default:
			fragment = new NoneUseFragment();
			break;
		}
		return fragment;
	}
}
