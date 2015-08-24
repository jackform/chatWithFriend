package org.jackform.innocent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jackform.innocent.R;
import org.jackform.innocent.data.PerChatItem;

import java.util.ArrayList;

/**
 * Created by jackform on 15-8-24.
 */
public class ChatContentListAdapter extends BaseAdapter {
    private ArrayList<PerChatItem> mData;
    private Context mContext;
    private View mMeChatItemView;
    private View mOtherChatItemView;

    public ChatContentListAdapter(Context context,ArrayList data) {
        mContext = context;
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
        boolean isMe = mData.get(position).isMe();
        int chatItemId = isMe ? R.layout.chat_listitem_me : R.layout.chat_listitem_other;
        TextView chatContent;
        ImageView headImage;
        convertView = LayoutInflater.from(mContext).inflate(chatItemId,null);
        if(isMe) {
            headImage = (ImageView)convertView.findViewById(R.id.chatlist_image_me);
            chatContent = (TextView)convertView.findViewById(R.id.chatlist_text_me);
        } else {
            headImage = (ImageView)convertView.findViewById(R.id.chatlist_image_other);
            chatContent = (TextView)convertView.findViewById(R.id.chatlist_text_other);
        }
        chatContent.setText(mData.get(position).getChatContent());
        return convertView;
    }
}