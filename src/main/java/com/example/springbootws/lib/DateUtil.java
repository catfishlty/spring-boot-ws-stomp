package com.example.springbootws.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.time.DateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 日期工具
 * @author Catfish
 * @version V1.0 2018/8/4 1:20
 * @email catfish_lty@qq.com
 **/
@Slf4j
public class DateUtil extends DateUtils {
    public final static String DATE = "yyyy-MM-dd";
    public final static String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public final static String DATETIME_CN = "yyyy年MM月dd日 HH:mm:ss";
    public final static String DATETIME_SHORT = "yyyy-MM-dd HH:mm";
    public final static String DATETIME_SHORT_CN = "yyyy年MM月dd日 HH:mm";
    public final static String TIME = "HH:mm:ss";
    public final static String TIME_SHORT = "HH:mm";
    public final static String DATE_MONTH = "yyyy-MM";
    private final static ThreadLocal<SimpleDateFormat> DATE_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE));
    private final static ThreadLocal<SimpleDateFormat> DATETIME_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATETIME));
    private final static ThreadLocal<SimpleDateFormat> DATETIME_CN_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATETIME_CN));
    private final static ThreadLocal<SimpleDateFormat> DATETIME_SHORT_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATETIME_SHORT));
    private final static ThreadLocal<SimpleDateFormat> DATETIME_SHORT_CN_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATETIME_SHORT_CN));
    private final static ThreadLocal<SimpleDateFormat> TIME_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(TIME));
    private final static ThreadLocal<SimpleDateFormat> TIME_SHORT_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(TIME_SHORT));
    private final static ThreadLocal<SimpleDateFormat> DATE_MONTH_FORMATTER = ThreadLocal.withInitial(() -> new SimpleDateFormat(DATE_MONTH));

    /**
     * 从时间字符串中获取时间戳
     * @param datetime 时间字符串
     * @param template 时间字符串模板
     * @return 时间戳
     * @throws ParseException 解析异常
     */
    public static long getTimeMillsFromString(String datetime, String template) {
        return getDateFromString(datetime, template).getTime();
    }

    public static Date getDateFromString(String datetime, String template) {
        ThreadLocal<SimpleDateFormat> formatter = getDateFormatter(template);
        try {
            return formatter.get().parse(datetime);
        } catch (ParseException e) {
            log.error("时间转换错误", e);
            return new Date();
        }
    }

    public static String getStringFromDate(Date date, String template) {
        ThreadLocal<SimpleDateFormat> formatter = getDateFormatter(template);
        return formatter.get().format(date);
    }

    private static ThreadLocal<SimpleDateFormat> getDateFormatter(String tpl) {
        switch (tpl) {
            case DATETIME:
                return DATETIME_FORMATTER;
            case DATETIME_CN:
                return DATETIME_CN_FORMATTER;
            case DATETIME_SHORT:
                return DATETIME_SHORT_FORMATTER;
            case DATETIME_SHORT_CN:
                return DATETIME_SHORT_CN_FORMATTER;
            case DATE:
                return DATE_FORMATTER;
            case DATE_MONTH:
                return DATE_MONTH_FORMATTER;
            case TIME:
                return TIME_FORMATTER;
            case TIME_SHORT:
                return TIME_SHORT_FORMATTER;
            default:
                log.error("[{}]模板不支持", tpl);
                return DATETIME_FORMATTER;
        }
    }

    public static String getStringFromString(String source, String tplFrom, String tplTo) {
        return getStringFromDate(getDateFromString(source, tplFrom), tplTo);
    }

    public static String getStringFromTimeMills(long timeMills, String template) {
        Date date = new Date();
        date.setTime(timeMills);
        return getStringFromDate(date, template);
    }

    public static String getStringFromCalendar(Calendar calendar, String template) {
        return getStringFromDate(calendar.getTime(), template);
    }

    public static String getCNDatetimeShortFromTimeMills(long timeMills) {
        Date date = new Date();
        date.setTime(timeMills);
        return getStringFromDate(date, DATETIME_SHORT_CN);
    }

    public static String getCNDatetimeFromTimeMills(long timeMills) {
        Date date = new Date();
        date.setTime(timeMills);
        return getStringFromDate(date, DATETIME_CN);
    }

    /**
     * 获得当前日期时间字符串
     * @return 日期时间字符串
     */
    public static String getNowDatetime() {
        return DateUtil.getStringFromTimeMills(getNowTimeMills(), DateUtil.DATETIME);
    }

    /**
     * 获得当前日期字符串
     * @return 日期字符串
     */
    public static String getNowDate() {
        return DateUtil.getStringFromTimeMills(getNowTimeMills(), DateUtil.DATE);
    }

    /**
     * 获得当前日期字符串
     * @return 日期字符串
     */
    public static String getNowDateMonth() {
        return DateUtil.getStringFromTimeMills(getNowTimeMills(), DateUtil.DATE_MONTH);
    }

    /**
     * 获取当前时间戳
     * @return 当前时间戳
     */
    public static long getNowTimeMills() {
        return System.currentTimeMillis();
    }

    /**
     * 获取指定年的第一时刻
     * @param year 年
     * @return 日历对象
     */
    public static Calendar getFirstTimeOfDate(Integer year) {
        return getFirstTimeOfDate(year, 1);
    }

    /**
     * 获取指定年月的第一时刻
     * @param year  年
     * @param month 月
     * @return 日历对象
     */
    public static Calendar getFirstTimeOfDate(Integer year, Integer month) {
        return getFirstTimeOfDate(year, month, 1);
    }

    /**
     * 获取指定年月日的第一时刻
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 日历对象
     */
    public static Calendar getFirstTimeOfDate(Integer year, Integer month, Integer day) {
        Calendar calendar = getDayStartTime(Calendar.getInstance());
        if (Objects.nonNull(year)) {
            calendar.set(Calendar.YEAR, year);
        }
        if (Objects.nonNull(month)) {
            calendar.set(Calendar.MONTH, month - 1);
        }
        if (Objects.nonNull(day)) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
        return calendar;
    }

    /**
     * 获取当日的第一时刻
     * @return 日期对象
     */
    public static Calendar getFirstTimeOfDay() {
        return getFirstTimeOfDate(null, null, null);
    }

    /**
     * 增加年
     * @param calendar 日期对象
     * @param year     年
     * @return 日期对象
     */
    public static Calendar addYear(Calendar calendar, int year) {
        Calendar c = (Calendar) calendar.clone();
        c.add(Calendar.YEAR, year);
        return c;
    }

    /**
     * 增加月
     * @param calendar 日期对象
     * @param month    月
     * @return 日期对象
     */
    public static Calendar addMonth(Calendar calendar, int month) {
        Calendar c = (Calendar) calendar.clone();
        c.add(Calendar.MONTH, month);
        return c;
    }

    /**
     * 增加天
     * @param calendar 日期对象
     * @param day      日
     * @return 日期对象
     */
    public static Calendar addDay(Calendar calendar, int day) {
        Calendar c = (Calendar) calendar.clone();
        c.add(Calendar.DAY_OF_MONTH, day);
        return c;
    }

    /**
     * 增加小时
     * @param calendar 日期对象
     * @param hour     小时
     * @return 日期对象
     */
    public static Calendar addHour(Calendar calendar, int hour) {
        Calendar c = (Calendar) calendar.clone();
        c.add(Calendar.HOUR_OF_DAY, hour);
        return c;
    }

    /**
     * 增加分钟
     * @param calendar 日期对象
     * @param minute   分钟
     * @return 日期对象
     */
    public static Calendar addMinute(Calendar calendar, int minute) {
        Calendar c = (Calendar) calendar.clone();
        c.add(Calendar.MINUTE, minute);
        return c;
    }

    /**
     * 增加秒
     * @param calendar 日期对象
     * @param second   秒
     * @return 日期对象
     */
    public static Calendar addSecond(Calendar calendar, int second) {
        Calendar c = (Calendar) calendar.clone();
        c.add(Calendar.SECOND, second);
        return c;
    }

    /**
     * 增加毫秒
     * @param calendar    日期对象
     * @param millisecond 毫秒
     * @return 日期对象
     */
    public static Calendar addMillisecond(Calendar calendar, int millisecond) {
        Calendar c = (Calendar) calendar.clone();
        c.add(Calendar.MILLISECOND, millisecond);
        return c;
    }

    private static Calendar getDayStartTime(Calendar calendar) {
        Calendar c = (Calendar) calendar.clone();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        return c;
    }

    public static Calendar tripToHour(Calendar calendar) {
        Calendar c = (Calendar) calendar.clone();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        return c;
    }

    /**
     * 根据时间戳获取日期对象
     * @param timeMillis 时间戳
     */
    public static Calendar getFromTimeMills(long timeMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        return calendar;
    }

    public static Calendar getFromString(String source, String template) throws ParseException {
        return getFromDate(getDateFromString(source, template));
    }

    /**
     * 根据Date获取日期对象
     * @param date 日期
     */
    public static Calendar getFromDate(Date date) {
        return getFromTimeMills(date.getTime());
    }

    public static Integer getTimeMills(Calendar calendar) {
        return calendar.get(Calendar.MILLISECOND);
    }

    public static Integer getSecond(Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    public static Integer getMinute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    public static Integer getHour(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static Integer getDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Integer getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Integer getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }


    public static Date getZeroDate() {
        Date date = new Date();
        date.setTime(0);
        return date;
    }

    public static Calendar getNowDate(Calendar calendar) {
        Calendar c = (Calendar) calendar.clone();
        Calendar now = Calendar.getInstance();
        c.set(Calendar.YEAR, now.get(Calendar.YEAR));
        c.set(Calendar.MONTH, now.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
        return c;
    }

    public static int betweenDay(String st, String ed, String tpl) throws ParseException {

        return betweenHour(st, ed, tpl) / 24;
    }

    public static int betweenDay(long st, long ed) {
        return betweenHour(st, ed) / 24;
    }

    public static int betweenHour(String st, String ed, String tpl) throws ParseException {
        return betweenMinute(st, ed, tpl) / 60;
    }

    public static int betweenHour(long st, long ed) {
        return betweenMinute(st, ed) / 60;
    }

    public static int betweenMinute(String st, String ed, String tpl) throws ParseException {
        return betweenSecond(st, ed, tpl) / 60;
    }

    public static int betweenMinute(long st, long ed) {
        return betweenSecond(st, ed) / 60;
    }

    public static int betweenSecond(String st, String ed, String tpl) throws ParseException {
        return betweenTimeMills(st, ed, tpl) / 1000;
    }

    public static int betweenSecond(long st, long ed) {
        return betweenTimeMills(st, ed) / 1000;
    }

    public static int betweenTimeMills(String st, String ed, String tpl) throws ParseException {

        return (int) Math.abs(getTimeMillsFromString(st, tpl) - getTimeMillsFromString(ed, tpl));
    }

    public static int betweenTimeMills(long st, long ed) {
        return (int) Math.abs(st - ed);
    }

    public static boolean validate(String tpl, String... args) {
        SimpleDateFormat sdf = new SimpleDateFormat(tpl);
        try {
            for (String arg : args) {
                sdf.parse(arg);
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static Calendar setDate(Calendar cc, int year, int month, int day) {
        Calendar c = (Calendar) cc.clone();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        return c;
    }

    public static Calendar setDate(Calendar cc, Calendar date) {
        return setDate(cc, DateUtil.getYear(date), DateUtil.getMonth(date), DateUtil.getDay(date));
    }

    public static Calendar setDate(Calendar cc, String date) throws ParseException {
        Calendar c = DateUtil.getFromString(date, DateUtil.DATE);
        return setDate(cc, c);
    }

    public static Calendar setTime(Calendar cc, int hour, int minute, int second) {
        Calendar c = (Calendar) cc.clone();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        return c;
    }

    public static Calendar setTime(Calendar cc, Calendar time) {
        return setTime(cc, DateUtil.getHour(time), DateUtil.getMinute(time), DateUtil.getSecond(time));
    }

    public static Calendar setTime(Calendar cc, String time) throws ParseException {
        Calendar c = DateUtil.getFromString(time, DateUtil.TIME);
        return setTime(cc, c);
    }

    public static int getWeekDay(Calendar cc) {
        return cc.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDateFromDatetime(String datetime) throws ParseException {
        return getStringFromString(datetime, DATETIME, DATE);
    }

    public static boolean check(String date, String tpl) {
        try {
            getFromString(date, tpl);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static Calendar combineCalAndTimeShort(Calendar current, String timeShort) throws ParseException {
        Calendar cal = (Calendar) current.clone();
        Calendar time = DateUtil.getFromString(timeShort, DateUtil.TIME_SHORT);
        cal.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, time.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, time.get(Calendar.MILLISECOND));
        return cal;
    }

    public static boolean isExpire(Long expire) {
        return expire < getNowTimeMills();
    }
}
