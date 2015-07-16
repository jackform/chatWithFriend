package org.jackform.innocent.utils;

import android.util.Log;

/**
 * Created by jackform on 15-7-15.
 */
public class DebugLog {
    static boolean isDebug = true;
    static String TAG = "hahaha";
    public static void v(String message)
    {
        if(isDebug)
            Log.v(TAG,message);
    }

}
