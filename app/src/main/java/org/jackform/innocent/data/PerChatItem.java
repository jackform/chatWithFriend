package org.jackform.innocent.data;

/**
 * Created by jackform on 15-8-24.
 */
public class PerChatItem {
    private boolean mIsMe;
    private String mChatContent;
    private String mDate;

    public PerChatItem(boolean isMe,String chatContent) {
       mIsMe = isMe;
        mChatContent = chatContent;
    }

    public boolean isMe() {
        return mIsMe;
    }

    public void setIsMe(boolean mIsMe) {
        this.mIsMe = mIsMe;
    }

    public String getChatContent() {
        return mChatContent;
    }

    public void setChatContent(String mChatContent) {
        this.mChatContent = mChatContent;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }
}
