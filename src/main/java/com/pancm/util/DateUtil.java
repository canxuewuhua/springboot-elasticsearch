package com.pancm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class DateUtil {

    public static final String FORMAT_PATTERN_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_PATTERN_DAY = "yyyy-MM-dd";
    public static final String FORMAT_PATTERN_MONTH = "yyyy-MM";
    public static final String FORMAT_PATTERN_TIME_TOGETHER = "yyyyMMddHHmmssSSS";
    private static String FORMAT_PATTERN_TIME_HH_MM_SS = "HH:mm:ss";
    private static final String SPLITTERS_LINE = "-";
    private static DateUtil dateUtil;

    @PostConstruct
    public void init() {
        dateUtil = this;
    }

    /**
     * 按格式模式格式化日期字符串，返回日期
     *
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date parseDate(String dateStr, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 把Long类型的时间戳格式转换成日期格式
     * 这里只能处理秒级别的时间戳
     */
    public static String transferLongToDate(String dateFormat, long secondsTimestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date = new Date();
        date.setTime(secondsTimestamp * 1000);
        return simpleDateFormat.format(date);
    }


    /**
     * 获取指定日期增加指定月间隔后的日期
     *
     * @param date
     * @param interval
     * @return
     */
    public static Date addMonth(Date date, int interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, interval);

        return cal.getTime();
    }

    /**
     * 获取指定日期增加指定日间隔后的日期
     *
     * @param date
     * @param interval
     * @return
     */
    public static Date addDays(Date date, int interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, interval);
        return cal.getTime();
    }

    /**
     * 使用指定的模式格式化日期
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String formatDate(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    public static String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_PATTERN_TIME);
        calendar.setTime(getCurrentTime());
        return simpleDateFormat.format(calendar.getTime());
    }


    public static Date getCurrentTime() {
        return new Date();
    }


    public static String getTimeHHMMSS() {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_PATTERN_TIME_HH_MM_SS);
        Date date = new Date();
        return format.format(date);
    }

    public static boolean valid(String start, String end) {
        boolean flag = false;
        try {
            if ((Integer.parseInt(start.replace(SPLITTERS_LINE, "")) <= Integer.parseInt(end.replace(SPLITTERS_LINE, "")))) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String timeToDate(long time) {
        SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat(FORMAT_PATTERN_TIME);
        return simpleDateFormat3.format(new Date(time));
    }


    public static String getTime() {
        Calendar oneCalendar = Calendar.getInstance();
        int year = oneCalendar.get(Calendar.YEAR);
        int month = oneCalendar.get(Calendar.MONTH);
        int day = oneCalendar.get(Calendar.DATE);
        int hours = oneCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = oneCalendar.get(Calendar.MINUTE);
        int second = oneCalendar.get(Calendar.SECOND);
        return year + "年" + month + "月" + day + "日" + hours + "时" + minute + "分" + second + "秒";
    }


    /**
     * 根据所给年、月、日，得到日期(java.sql.Date类型)，注意：没有时间，只有日期。
     * 年、月、日不合法，会抛IllegalArgumentException(不需要catch)
     *
     * @param yyyy 4位年
     * @param MM   月
     * @param dd   日
     * @return 日期
     */
    public static java.sql.Date getDate(int yyyy, int MM, int dd) {
        if (!verityDate(yyyy, MM, dd)) {
            throw new IllegalArgumentException("This is illegimate date!");
        }
        Calendar oneCalendar = Calendar.getInstance();
        oneCalendar.clear();
        oneCalendar.set(yyyy, MM - 1, dd);
        return new java.sql.Date(oneCalendar.getTime().getTime());
    }

    /**
     * 根据所给年、月、日，检验是否为合法日期。
     *
     * @param yyyy 4位年
     * @param MM   月
     * @param dd   日
     * @return
     */
    public static boolean verityDate(int yyyy, int MM, int dd) {
        boolean flag = false;

        if (MM >= 1 && MM <= 12 && dd >= 1 && dd <= 31) {
            if (MM == 4 || MM == 6 || MM == 9 || MM == 11) {
                if (dd <= 30) {
                    flag = true;
                }
            } else if (MM == 2) {
                if (yyyy % 100 != 0 && yyyy % 4 == 0 || yyyy % 400 == 0) {
                    if (dd <= 29) {
                        flag = true;
                    }
                } else if (dd <= 28) {
                    flag = true;
                }
            } else {
                flag = true;
            }
        }
        return flag;
    }

    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        //给日期格式设置默认值
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = FORMAT_PATTERN_TIME;
        }
        SimpleDateFormat df = new SimpleDateFormat(formatStr);

        return df.format(date);
    }

    public static Date strToDate(String dateStr, String formatStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }

        //给日期格式设置默认值
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = FORMAT_PATTERN_TIME;
        }
        SimpleDateFormat df = new SimpleDateFormat(formatStr);

        try {
            return df.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 功能描述：获得给定的两个日期之间相差的天数（日期不分前后）
     *
     * @param fromdate 日期 格式：Date
     * @param todate   日期 格式：Date
     * @return long
     * @修改日志：1.0
     */
    public static long getDaysBetween(Date fromdate, Date todate) {
        Calendar from = DateUtils.toCalendar(fromdate);
        Calendar to = DateUtils.toCalendar(todate);
        long millis = Math.abs(from.getTimeInMillis() - to.getTimeInMillis());
        return millis / (24 * 60 * 60 * 1000);
    }

    /**
     * 返回yyyyMM格式的String
     *
     * @param date : yyyy-MM-dd
     * @return yyyyMM
     */
    public static String getYearMonth(Date date, String formatStr) {
        //给日期格式设置默认值
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = FORMAT_PATTERN_MONTH;
        }
        SimpleDateFormat df = new SimpleDateFormat(formatStr);
        try {
            return df.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num  为增加的天数
     * @param date 日期
     * @return
     * @throws ParseException
     */
    public static String plusDay(String date, int num) {
        Date currdate = strToDate(date, FORMAT_PATTERN_DAY);
        Calendar cal = Calendar.getInstance();
        cal.setTime(currdate);
        cal.add(Calendar.DATE, num);// num为增加的天数，可以改变的
        currdate = cal.getTime();
        String enddate = dateToStr(currdate, FORMAT_PATTERN_DAY);
        return enddate;
    }

    /**
     * 方法描述： 比较两个时间的大小
     *
     * @param currDatetime1 yyyyMMdd hh:mm:ss
     * @param currDatetime2 yyyyMMdd hh:mm:ss
     * @return int 返回 -1 currDatetime1小于currDatetime2 0 相等1 currDatetime1大于currDatetime2
     */
    public static int compareTime(String currDatetime1, String currDatetime2) {
        int flag = 0;
        try {
            DateFormat df = new SimpleDateFormat(FORMAT_PATTERN_TIME);
            Date startDateTime = df.parse(currDatetime1);
            Date endDateTime = df.parse(currDatetime2);
            if (startDateTime.getTime() < endDateTime.getTime()) {
                flag = -1;
            } else if (startDateTime.getTime() > endDateTime.getTime()) {
                flag = 1;
            } else if (startDateTime.getTime() == endDateTime.getTime()) {
                flag = 0;
            }
        } catch (Exception e) {
            log.error("时间比较出现异常,异常信息:{},currDatetime1:{},currDatetime2:{}", e.getMessage(), currDatetime1, currDatetime2);
        }
        return flag;
    }

}