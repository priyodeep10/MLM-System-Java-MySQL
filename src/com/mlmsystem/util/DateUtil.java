package com.mlmsystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    
    public static String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    public static String getCurrentDateTime() {
        return formatDate(new Date());
    }
}