package org.jackform.innocent.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jackform on 15-8-24.
 */
public class TimeUtils {
    static public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        return format.format(curDate);
    }
}
