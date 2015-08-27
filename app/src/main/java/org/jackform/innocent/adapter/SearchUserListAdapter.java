package org.jackform.innocent.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jackform.innocent.R;
import org.jackform.innocent.data.FriendInfo;

import java.util.List;

/**
 * Created by jackform on 15-8-27.
 */
public class SearchUserListAdapter extends BaseAdapter {

    private List<FriendInfo> mData;
    private Context mContext;

    public SearchUserListAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<FriendInfo> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friendlist_child, null);
        TextView friendName = (TextView)convertView.findViewById(R.id.ct_name);
        ImageView headImage = (ImageView)convertView.findViewById(R.id.ct_photo);
        TextView sign = (TextView)convertView.findViewById(R.id.ct_sign);
        friendName.setText(mData.get(position).getmUserName());
        sign.setText(mData.get(position).getmMale() + " " + mData.get(position).getmAge());
        Bitmap bitmap = BitmapFactory.decodeFile(mData.get(position).getmHeaderImagePath());
        headImage.setImageBitmap(bitmap);
        return convertView;
    }
}
