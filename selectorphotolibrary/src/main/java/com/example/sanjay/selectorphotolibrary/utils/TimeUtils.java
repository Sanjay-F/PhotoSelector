package com.example.sanjay.selectorphotolibrary.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {


    public static final String TAG = TimeUtils.class.getSimpleName();

    public static String timeFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time) {
        return timeFormat(time, "yyyy-MM");
    }

    public static String formatPhotoDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01";
    }


    /**
     * @param time 这个是unix时间戳，因为系统返回的就是这个
     * @return 返回 time 与今天的关系是本周，本月还是某年某月
     */
    public static String getTimeStatus(long time) {


        long dataTime = time * 1000;
        if (isSameWeek(System.currentTimeMillis(), dataTime)) {
            return "本周";
        } else if (isSameMonth(System.currentTimeMillis(), dataTime)) {
            return "这个月";
        }
        return toStringOnlyYearAndMonth(dataTime);
    }

    public static boolean isSameWeek(long aTimeMills, long bTimeMills) {
        return getFirstSecondInWeek(aTimeMills) == getFirstSecondInWeek(bTimeMills);
    }

    public static boolean isSameMonth(long aTimeMills, long bTimeMills) {
        return getFirstSecondInMonth(aTimeMills) == getFirstSecondInMonth(bTimeMills);
    }

    /**
     * 根据一个表示的毫秒值获取该毫秒值所在的那一周的最早一秒
     *
     * @param timeMillis
     * @return
     */
    public static long getFirstSecondInWeek(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTimeInMillis(timeMillis);
        calendar.set(Calendar.DAY_OF_WEEK,
                calendar.getMinimum(Calendar.DAY_OF_WEEK));
        calendar.set(Calendar.HOUR_OF_DAY,
                calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar.getTimeInMillis();
    }

    /**
     * 根据一个表示时间的毫秒值获取该毫秒值所在的那一月的最早一秒
     *
     * @param startTimeMillis
     * @return
     */
    public static long getFirstSecondInMonth(long startTimeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTimeMillis);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,
                calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar.getTimeInMillis();
    }

    public static String toStringOnlyYearAndMonth(long milse) {
        Date date = new Date(milse);
        return toString(date, "yyyy-MM");
    }


    public static String toString(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(" 本周？ " + getTimeStatus(1450926871));
        System.out.println(" 这个月？" + getTimeStatus(1449252122));
        System.out.println(" 2015-09？ " + getTimeStatus(1441044122));
    }
}
