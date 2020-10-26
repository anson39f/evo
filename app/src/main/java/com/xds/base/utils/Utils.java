package com.xds.base.utils;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.orhanobut.logger.Logger;
import com.xds.project.BaseApplication;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 */

public class Utils {

    public static void closeAll(Closeable a, Closeable b) throws IOException {
        Throwable thrown = null;
        try {
            a.close();
        } catch (Throwable e) {
            thrown = e;
        }
        try {
            b.close();
        } catch (Throwable e) {
            if (thrown == null)
                thrown = e;
        }
        if (thrown == null)
            return;
        if (thrown instanceof IOException)
            throw (IOException) thrown;
        if (thrown instanceof RuntimeException)
            throw (RuntimeException) thrown;
        if (thrown instanceof Error)
            throw (Error) thrown;
        throw new AssertionError(thrown);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    public static int parseInt(String value, int def) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return def;
        }
    }

    public static long parseLong(String value, long def) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            return def;
        }
    }

    public static float parseFloat(String value, float def) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return def;
        }
    }

    public static double parseDouble(String value, double def) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 获取字符长度
     *
     * @param s
     * @return
     */
    public static int getStringLength(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;

        }
        return length;

    }

    /**
     * @param s
     * @return
     */
    public static int getStringIndex(String s, int length) {
        int count = 0;
        int index = s.length();
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {
                count++;
            } else {
                count += 2;
            }
            if (count > length) {
                index = i;
                break;
            }
        }
        return index >= 0 ? index : 0;

    }

    public static String getString(String s, int length) {
        return getString(s, length, true);
    }

    public static String getString(String s, int length, boolean ellipsis) {
        if (TextUtils.isEmpty(s))
            return "";
        int l = 0;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255) {
                l++;
            } else {
                l += 2;
            }
            if (l > length && ellipsis) {
                sb.append("...");
                break;
            } else {
                sb.append(s.charAt(i));
            }

        }
        return sb.toString();
    }

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //初始化keystore
            //            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //            clientKeyStore.load(mContext.getAssets().open("**.jks"), "123456".toCharArray());
            //
            //            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            //            keyManagerFactory.init(clientKeyStore, "123456".toCharArray());

            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            //            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getIdCard(String idCard, int index) {
        if (TextUtils.isEmpty(idCard)) {
            return null;
        }
        try {
            StringBuffer sb = new StringBuffer();
            if (index < 18) {
                int count = 18 - index;
                for (int i = 0; i < count; i++) {
                    sb.append("*");
                }
            }
            return String.format("%s%s", idCard.substring(0, index), sb.toString());
        } catch (Exception e) {
            return idCard;
        }

    }

    public static String getIdCard(String idCard) {
        return getIdCard(idCard, 14);
    }

    public static String getIdCard2(String idCard) {
        if (TextUtils.isEmpty(idCard)) {
            return idCard;
        }
        return idCard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1*****$2");
    }

    public static Pair<Boolean, List<Float>> isRange(String reference, String value) {
        if (TextUtils.isEmpty(reference) || TextUtils.isEmpty(value)) {
            return Pair.create(false, null);
        }
        List<Float> values = new ArrayList<>();
        try {
            Pattern pattern = Pattern.compile("^?[0-9]+\\.?[0-9]*");
            Matcher matcher = pattern.matcher(reference);

            while (matcher.find()) {
                values.add(Float.valueOf(matcher.group()));
            }
        } catch (Exception e) {
            Logger.e(e, "isRange");
        }


        return Pair.create(values.size() == 2 && FormatUtils.isNumeric(value), values);
    }


    /**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @param color  高亮颜色
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target, int color) {
        if (BlankUtil.isBlank(text)) {
            return null;
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        if (BlankUtil.isBlank(target) || color == 0) {
            return spannable;
        }

        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(color);// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static SpannableStringBuilder highlight(Spanned text, String target, int color) {
        if (BlankUtil.isBlank(text)) {
            return null;
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        if (BlankUtil.isBlank(target) || color == 0) {
            return spannable;
        }

        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
            span = new ForegroundColorSpan(color);// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static String stringformat(String format, String... values) {
        if (values == null) {
            return "";
        }
        List<String> list = new ArrayList<>();
        for (String value : values) {
            list.add(TextUtils.isEmpty(value) ? "" : value);
        }
        return String.format(format, list.toArray());
    }

    /**
     * 拨打电话
     *
     * @param phone
     */
    public static void call(String phone) {
        try {

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            BaseApplication.getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getSexName(String sex) {
        String sexName = "";
        if ("1".equals(sex)) {
            sexName = "男";
        } else if ("2".equals(sex)) {
            sexName = "女";
        } else if (TextUtils.isEmpty(sex)) {
            sexName = "未知";
        } else {
            sexName = sex;
        }
        return sexName;
    }

    /**
     * 判断是否为image
     *
     * @param filePath
     * @return
     */
    public static boolean isImageFile(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        if (options.outWidth == -1) {
            return false;
        }
        return true;
    }
}
