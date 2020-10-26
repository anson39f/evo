package com.xds.project.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 */
public class TimeUtil {

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }


    public static String getTime(long time, String format) {
        if (time == 0) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time));
    }

    /**
     * 显示 15分10秒
     *
     * @param time
     * @return
     */
    public static String getMinAndSecond(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
        }
        return timeStr;
    }

    /**
     * 00:00:00
     *
     * @param time
     * @return
     */
    public static String getHourMinSecond(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0) {
            return "00:00:00";
        } else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr ="00:" +unitFormat(minute) + ":" + unitFormat(second) + "";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String getChatTime(long timesamp) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp = Integer.parseInt(sdf.format(today)) - Integer.parseInt(sdf.format(otherDay));
        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(timesamp);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(timesamp);
                break;
            case 2:
                result = "前天 " + getHourAndMin(timesamp);
                break;
            default:
                // result = temp + "天前 ";
                result = getTime(timesamp);
                break;
        }
        return result;
    }


    /**
     * 格式化消息中心的时间
     * 当天显示：X分钟/小时前
     * 当年显示：MM/DD
     * 非当年显示：YY/MM/DD
     *
     * @return
     */
    public static String getAnnouncementData(long timesamp) {
        String result = "";
        SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf_dd = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_HH = new SimpleDateFormat("HH");
        SimpleDateFormat sdf_mm = new SimpleDateFormat("mm");

        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp_year = Integer.parseInt(sdf_yyyy.format(today)) - Integer.parseInt(sdf_yyyy.format(otherDay));
        int temp_day = Integer.parseInt(sdf_dd.format(today)) - Integer.parseInt(sdf_dd.format(otherDay));
        int temp_hour = Integer.parseInt(sdf_HH.format(today)) - Integer.parseInt(sdf_HH.format(otherDay));
        int temp_min = Integer.parseInt(sdf_mm.format(today)) - Integer.parseInt(sdf_mm.format(otherDay));

        if (temp_year == 0) {  //当年
            if (temp_day == 0) {  //当天
                // X分钟/小时前
                if (temp_hour == 0) {
                    result = temp_min + "分钟前";
                } else if (temp_min == 0) {
                    result = temp_hour + "小时前";
                } else {
                    result = temp_hour + "小时" + temp_min + "分钟前";
                }
            } else { //非当天
                // MM/DD
                SimpleDateFormat sdf_MM = new SimpleDateFormat("MM/dd");
                result = sdf_MM.format(otherDay);
            }
        } else { //非当年
            SimpleDateFormat sdf_YYYY = new SimpleDateFormat("yyyy/MM/dd");
            result = sdf_YYYY.format(otherDay);
        }
        return result;
    }

    /**
     * 传入生日返回年
     * 生日格式：2015-07-09 00:00:00
     */
    public static int getYear(String birthday) {
        try {
            String year = birthday.substring(0, 4);
            return Integer.parseInt(year);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 传入生日返回月
     * 生日格式：2015-07-09 00:00:00
     */
    public static int getMonth(String birthday) {
        try {
            String s_month = birthday.substring(5, 7);
            if (s_month.substring(0, 2).equals("0"))
                return Integer.parseInt(s_month.substring(1, 2));
            else
                return Integer.parseInt(s_month);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 传入生日返回日
     * 生日格式：2015-07-09 00:00:00
     */
    public static int getDay(String birthday) {
        try {
            String s_day = birthday.substring(8, 10);
            if (s_day.substring(0, 2).equals("0"))
                return Integer.parseInt(s_day.substring(2, 3));
            else
                return Integer.parseInt(s_day);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 生日格式：2015-07-09 00:00:00
     */
    public static int getDate(String birthday) {
        try {
            String s_day = birthday.substring(0, 10);
            if (s_day.substring(0, 2).equals("0"))
                return Integer.parseInt(s_day.substring(2, 3));
            else
                return Integer.parseInt(s_day);
        } catch (Exception e) {
            return 0;
        }
    }


    /**
     * 传入生日返回时
     * 生日格式：2015-07-09 00:00:00
     */
    public static int getHour(String birthday) {
        try {
            String s_hour = birthday.substring(11, 13);
            if (s_hour.substring(0, 2).equals("0"))
                return Integer.parseInt(s_hour.substring(2, 3));
            else
                return Integer.parseInt(s_hour);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 传入生日返回分
     * 生日格式：2015-07-09 00:00:00
     */
    public static int getMinute(String birthday) {
        try {
            String s_minute = birthday.substring(14, 16);
            if (s_minute.substring(0, 2).equals("0"))
                return Integer.parseInt(s_minute.substring(2, 3));
            else
                return Integer.parseInt(s_minute);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 传入生日返回秒
     * 生日格式：2015-07-09 00:00:00
     */
    public static int getSecond(String birthday) {
        try {
            String s_second = birthday.substring(17, 19);
            if (s_second.substring(0, 2).equals("0"))
                return Integer.parseInt(s_second.substring(2, 3));
            else
                return Integer.parseInt(s_second);
        } catch (Exception e) {
            return 0;
        }
    }

    // 将字符串转为时间戳
    public static String getTime(String user_time) {
        String re_time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String str = String.valueOf(sdf.parse(user_time).getTime());
            re_time = str;
        } catch (Exception e) {
            return "0";
        }
        return re_time;
    }

    // 将字符串
    public static Date getDate(String user_time, String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(user_time);
        } catch (Exception e) {
            return new Date();
        }
    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date t = null;
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            t = sdf.parse(cc_time);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            return sdf2.format(t);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    // 将时间戳转为字符串
    public static String getStrTime(String cc_time, String oldformat, String format) {
        if (TextUtils.isEmpty(oldformat)) {
            oldformat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(oldformat);
        Date t = null;
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            t = sdf.parse(cc_time);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            return sdf2.format(t);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    // 将时间戳转为字符串
    public static String getStrTime(long time, String format) {
        Date t = null;
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            t = new Date(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            return sdf2.format(t);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    // 将时间戳转为字符串
    public static String getStrTime(Date time, String format) {
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            return sdf2.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getAge(String birthday) {
        return getAge(birthday, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getAge(String birthday, String format) {
        String age = "";
        if (TextUtils.isEmpty(birthday)) {
            return "";
        }
        Date nowTime = new Date();  //当前时间戳
        Date birTime = getDate(birthday, format); //出身时间戳

        SimpleDateFormat yearF = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthF = new SimpleDateFormat("MM");
        SimpleDateFormat dayF = new SimpleDateFormat("dd");

        //获取现在年月日时分
        int nowYear = Integer.valueOf(yearF.format(nowTime));
        int nowMonth = Integer.valueOf(monthF.format(nowTime));
        int nowDay = Integer.valueOf(dayF.format(nowTime));


        //获取出身年月日时分
        int birYear = Integer.valueOf(yearF.format(birTime));
        int birMonth = Integer.valueOf(monthF.format(birTime));
        int birDay = Integer.valueOf(dayF.format(birTime));


        //时间差
        int differYear = nowYear - birYear;
        int differMonth = nowMonth - birMonth;
        int differDay = nowDay - birDay;


        int tempYear = Math.abs(differYear);
        int tempMonth = Math.abs(differMonth);
        int tempDay = Math.abs(differDay);


//        L.e("tempYear=" + tempYear + " tempMonth=" + tempMonth + " tempDay=" + tempDay);

        if (tempYear >= 1) {  //年龄>14?
            age = differYear + "岁"; //格式：X岁
        } else {
            //判断是否小于1岁
            if (tempYear < 1) {
                //年龄小于1岁，判断年龄是否大于28天
                if (tempMonth >= 1 || (tempMonth < 1 && tempDay >= 28)) {
                    age = tempMonth + "月";
                } else {
                    if (tempDay > 0) {
                        age = differDay + 1 + "天";
                    } else {
                        age = 1 + "天";
                    }
                }
            } else {
                //年龄大于1岁，格式：X岁X月
                age = differYear + "岁" + differMonth + "月";
            }
        }
        return age;
    }

    /**
     * 获取消息列表时间
     * 显示：X分钟-小时前
     * 显示：MM-DD
     * 显示：YY-MM-DD
     *
     * @return
     */
    public static String getMessageData(long timesamp) {
        String result = "";
        SimpleDateFormat sdf_yyyy = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf_dd = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_HH = new SimpleDateFormat("HH");
        SimpleDateFormat sdf_mm = new SimpleDateFormat("mm");

        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(timesamp);
        int temp_year = Integer.parseInt(sdf_yyyy.format(today)) - Integer.parseInt(sdf_yyyy.format(otherDay));
        int temp_day = Integer.parseInt(sdf_dd.format(today)) - Integer.parseInt(sdf_dd.format(otherDay));
        int temp_hour = Integer.parseInt(sdf_HH.format(today)) - Integer.parseInt(sdf_HH.format(otherDay));
        int temp_min = Integer.parseInt(sdf_mm.format(today)) - Integer.parseInt(sdf_mm.format(otherDay));

        if (temp_year == 0) {  //当年
            if (temp_day == 0) {  //当天
                result = sdf_HH.format(otherDay) + ":" + sdf_mm.format(otherDay);
            } else if (temp_day == 1) {
                result = "昨天 " + sdf_HH.format(otherDay) + ":" + sdf_mm.format(otherDay);
            } else { //非当天
                SimpleDateFormat sdf_MM = new SimpleDateFormat("yyyy-MM-dd");
                result = sdf_MM.format(otherDay);
            }
        }
        return result;
    }

    /**
     * 比较时间
     *
     * @param DATE1
     * @param DATE2
     * @return 1：dt1 在dt2前
     */
    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取偏差的时间
     * offset 正数为往后一天，负数为往前一天
     *
     * @param offset
     * @return
     */
    public static Date getAfterOrBeforeDay(int offset) {
        Date date = new Date();//取时间
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(c.DATE, offset);
        return c.getTime();
    }

    /**
     * 获取偏差的时间
     * offset 正数为往后一月，负数为往前一月
     *
     * @param offset
     * @return
     */
    public static Date getAfterOrBeforeMonth(int offset) {
        Date date = new Date();//取时间
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(c.MONTH, offset);
        return c.getTime();
    }

    /**
     * 获取偏差的时间
     * offset 正数为往后一年，负数为往前一年
     *
     * @param offset
     * @return
     */
    public static Date getAfterOrBeforeYear(int offset) {
        Date date = new Date();//取时间
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(c.YEAR, offset);
        return c.getTime();
    }
}
