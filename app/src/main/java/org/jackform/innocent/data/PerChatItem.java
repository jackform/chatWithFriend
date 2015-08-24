package org.jackform.innocent.data;

/**
 * Created by jackform on 15-8-24.
 */
public class PerChatItem {
    private boolean mIsMe;
    private String mChatContent;
    private String mDate;
    private boolean mIsSending = false;
    private boolean mIsSendingCompleted = false;
    private boolean mIsSendingFailure = false;

    public PerChatItem(boolean isMe,String chatContent,String chatTime) {
       mIsMe = isMe;
        mChatContent = chatContent;
        mDate = chatTime;
    }

    public void setIsSending() {
        mIsSending = true;
    }

    public void setSendMessageCompleted() {
        mIsSending = false;
        mIsSendingCompleted = true;
        mIsSendingFailure = false;
    }

    public boolean isSendingFailure() {
        return !mIsSendingFailure && mIsSendingFailure && !mIsSendingCompleted;
    }

    public void setmIsSendingFailure() {
        mIsSending = false;
        mIsSendingCompleted = false;
        mIsSendingFailure = true;
    }

    public boolean isSendingCompleted() {
        return !mIsSending && mIsSendingCompleted && !mIsSendingFailure;
    }

    public boolean isSending() {
        return mIsSending;
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
