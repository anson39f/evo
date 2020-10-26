package com.xds.base.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 常用格式判断工具类
 */
public class FormatUtils {


    /**
     * 判断只为汉字或者字母,汉字+字母
     *
     * @param name
     * @return
     */
    public static boolean isName(CharSequence name) {
        if (name == null || name.length() == 0 || "".equals(name)) {
            return false;
        }
        Pattern p = Pattern.compile("^[A-Za-z\\u4e00-\\u9fa5·s]{2,30}+$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 判断时间是否正确格式
     *
     * @param date
     * @param format
     * @return
     */
    public static boolean checkDate(String date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date d = null;
        try {
            d = df.parse(date);
        } catch (Exception e) {
            //如果不能转换,肯定是错误格式
            return false;
        }
        String s1 = df.format(d);
        // 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
        // "2006-02-31",转换为日期后再转换回字符串为"2006-03-03",说明格式虽然对,但日期
        // 逻辑上不对.
        return date.equals(s1);
    }

    /**
     * 判断邮件email是否正确格式
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (email == null || email.length() == 0 || "".equals(email)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


    /**
     * 判断手机号phone是否正确格式
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (phone == null || phone.length() == 0 || "".equals(phone)) {
            return false;
        }
        Pattern p = Pattern.compile("^1[0-9]{10}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    public static String isHidePhone(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }
        return idCard.replaceAll("(\\d{3})\\d{5}(\\w{3})", "$1*****$2");
    }



    /**
     * 只为数字和字母的密码
     *
     * @param password
     * @return
     */
    public static boolean isPassword(String password) {
        if (password == null || password.length() == 0 || "".equals(password)) {
            return false;
        }
        Pattern p = Pattern.compile("^[a-zA-Z]((?![a-zA-Z]+$)[0-9A-Za-z]{7,15})$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     1、号码的结构
     公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     八位数字出生日期码，三位数字顺序码和一位数字校验码。
     2、地址码(前六位数）
     表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
     3、出生日期码（第七位至十四位）
     表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
     4、顺序码（第十五位至十七位）
     表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。
     5、校验码（第十八位数）
     （1）十七位数字本体码加权求和公式
     S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
     Ai:表示第i位置上的身份证号码数字值
     Wi:表示第i位置上的加权因子
     Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     （2）计算模
     Y = mod(S, 11)
     （3）通过模得到对应的校验码
     Y: 0 1 2 3 4 5 6 7 8 9 10
     校验码: 1 0 X 9 8 7 6 5 4 3 2
     所以我们就可以大致写一个函数来校验是否正确了。
     * */

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr 身份证号
     * @return 有效：true        无效：false
     * @throws ParseException
     */
    public static boolean IDCardValidate(String IDStr) {
        String errorInfo = "";//记录错误信息
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        //================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "号码长度应该为15位或18位。";
            System.out.println(errorInfo);
            return false;
        }
        //================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            System.out.println(errorInfo);
            return false;
        }
        //================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);//年份
        String strMonth = Ai.substring(10, 12);//月份
        String strDay = Ai.substring(12, 14);//日份
        if (isDate(strYear + "/" + strMonth + "/" + strDay) == false) {
            errorInfo = "生日无效。strYear=" + strYear + " strMonth=" + strMonth + " strDay=" + strDay;
            System.out.println(errorInfo);
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "生日不在有效范围。";
                System.out.println(errorInfo);
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "月份无效";
            System.out.println(errorInfo);
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "日期无效";
            System.out.println(errorInfo);
            return false;
        }
        //================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "地区编码错误。";
            System.out.println(errorInfo);
            return false;
        }
        //================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18) {
            if (Ai.equalsIgnoreCase(IDStr) == false) {
                errorInfo = "身份证无效，最后一位字母错误";
                System.out.println(errorInfo);
                return false;
            }
        } else {
            System.out.println("所在地区:" + h.get(Ai.substring(0, 2).toString()));
            System.out.println("新身份证号:" + Ai);
            return true;
        }
        System.out.println("所在地区:" + h.get(Ai.substring(0, 2).toString()));
        return true;
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }


    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        // 该正则表达式可以匹配所有的数字 包括负数
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception e) {
            return false;//异常 说明包含非数字。
        }

        Matcher isNum = pattern.matcher(bigStr); // matcher是全匹配
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @return
     */
    public static boolean isDate(String strDate) {
        boolean convertSuccess = true;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            format.setLenient(false);
            format.parse(strDate);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }

    /**
     * 功能:在判定已经是正确的身份证号码之后,查找出身份证所在地区
     *
     * @param idCard 身份证号码
     * @return 所在地区
     */
    public static String GetArea(String idCard) {
        Hashtable<String, String> ht = GetAreaCode();
        String area = ht.get(idCard.substring(0, 2));
        return area;
    }

    /**
     * 功能:在判定已经是正确的身份证号码之后,查找出此人性别
     *
     * @param idCard 身份证号码
     * @return 男或者女
     */
    public static String GetSex(String idCard) {
        String sex = "";
        if (idCard.length() == 15)
            sex = idCard.substring(idCard.length() - 3, idCard.length());

        if (idCard.length() == 18 && IDCardValidate(idCard))
            sex = idCard.substring(idCard.length() - 2, idCard.length() - 1);
        if (isNumeric(sex)) {
            int sexNum = Integer.parseInt(sex) % 2;
            if (sexNum == 0) {
                return "女";
            }
        }
        return "男";
    }

    //根据身份证号输出年龄
    public static int getAge(String idCard) {
        int leh = idCard.length();
        String dates = "";
        if (leh == 18) {
            //            int se = Integer.valueOf(idCard.substring(leh - 1)) % 2;
            dates = idCard.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            int u = Integer.parseInt(year) - Integer.parseInt(dates);
            return u;
        } else {
            dates = idCard.substring(6, 8);
            return Integer.parseInt(dates);
        }

    }

    /**
     * 功能:在判定已经是正确的身份证号码之后,查找出此人出生日期
     *
     * @param idCard 身份证号码
     * @return 出生日期 XXXX MM-DD
     */

    public static String GetBirthday(String idCard) {
        String Ain = "";
        if (idCard.length() == 18) {
            Ain = idCard.substring(0, 17);
        } else if (idCard.length() == 15) {
            Ain = idCard.substring(0, 6) + "19" + idCard.substring(6, 15);
        }
        //================ 出生年月是否有效 ================
        String strYear = Ain.substring(6, 10);//年份
        String strMonth = Ain.substring(10, 12);//月份
        String strDay = Ain.substring(12, 14);//日期
        return strYear + "-" + strMonth + "-" + strDay;
    }

    /**
     * 密码格式：6到12位
     */
    public static boolean checkPwd(String pwd) {
        boolean flag;
        int length = pwd.length();
        if (length >= 6 && length <= 12) {
            flag = true;
            char[] pwdArray = pwd.toCharArray();
            for (int i = 0; i < pwdArray.length; i++) {
                if (isChinese(pwdArray[i])) {
                    flag = false;
                    break;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    //判断是否是汉字
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
