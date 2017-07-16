package com.safety.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.net.ssl.HttpsURLConnection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanwenbin on 16/9/19.
 */
public final class Utils {
    public final static BigDecimal HUNDRED = new BigDecimal(100);

    public static Timestamp getNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getEndOfDay(Timestamp day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getBeginOfDay(Timestamp day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getBeginOfHour(Timestamp time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new Timestamp(cal.getTime().getTime());
    }

    public static Timestamp getSomeDayAfter(Timestamp now, int i) {
        return new Timestamp(now.getTime() + i * 24 * 60 * 60 * 1000l);
    }

    public static Timestamp getSomeDayAgo(Timestamp now, int i) {
        return new Timestamp(now.getTime() - i * 24 * 60 * 60 * 1000l);
    }

    public static BigDecimal fromFenToYuan(final Long fen) {
        if (null == fen) return BigDecimal.ZERO;
        return new BigDecimal(fen).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal fromFenToYuan(final Double fen) {
        if (null == fen) return BigDecimal.ZERO;
        return new BigDecimal(fen).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public static Long fromYuanToFen(final BigDecimal yuan) {
        if (null == yuan) return 0l;
        return yuan.multiply(HUNDRED).longValue();
    }

    /**
     * 判断网址是否有效<br>
     *
     * @param
     * @return
     */
    public static boolean isReachable(String urlString) {

        boolean reachable = false;
        HttpURLConnection httpconn = null;
        HttpsURLConnection httpsconn = null;
        int code = 0;
        try {
            URL url = new URL(urlString);
            if (url.getProtocol().equals("https")) {
                httpsconn = (HttpsURLConnection) url.openConnection();
                code = httpsconn.getResponseCode();
            } else {
                httpconn = (HttpURLConnection) url.openConnection();
                code = httpconn.getResponseCode();
            }
            if (code != 404 && code < 500) {
                reachable = true;
            }
        } catch (Exception e) {
            reachable = false;
        }
        return reachable;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static boolean isStringArrayList(String stringArrayList) {
        Pattern p = Pattern.compile("\\[\\d+(,\\d+)*\\]");
        Matcher m = p.matcher(stringArrayList == null ? "" : stringArrayList);
        return m.matches();
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().toUpperCase().replace("-", "");
    }

    public static int getDateSpace(Timestamp fromTime, Timestamp toTime) {

        Calendar calst = Calendar.getInstance();;
        Calendar caled = Calendar.getInstance();

        calst.setTime(fromTime);
        caled.setTime(toTime);

        // 设置时间��
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        // 得到两个日期相差的天�
        int days = ((int) (caled.getTime().getTime() / 1000)
                - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;

        return days + 1;
    }
    
    public static int getMonthSpace(Timestamp fromTime, Timestamp toTime) {
        
        int result = 0;

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(fromTime);
        c2.setTime(toTime);

        result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return (result == 0 ? 1 : Math.abs(result)) + 1;

    }
    
    public static int getWeekSpace(Timestamp fromTime, Timestamp toTime) {  
        
        Calendar cal = Calendar.getInstance();  
        cal.setTime(fromTime);  
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);  
        dayOfWeek = dayOfWeek - 1;  
        if (dayOfWeek == 0) dayOfWeek = 7;  
  
        int dayOffset = getDateSpace(fromTime, toTime);  
  
        int weekOffset = dayOffset / 7;  
        int a;  
        if (dayOffset > 0) {  
            a = (dayOffset % 7 + dayOfWeek > 7) ? 1 : 0;  
        } else {  
            a = (dayOfWeek + dayOffset % 7 < 1) ? -1 : 0;  
        }  
        weekOffset = weekOffset + a;  
        return weekOffset + 1;  
    }  

    
    public static void main(String[] args) {
        Timestamp from = new Timestamp(1495814400000L);//27
        Timestamp to = new Timestamp(1496937600000L);//9
        System.out.println(getDateSpace(from, from));
        System.out.println(getMonthSpace(from, to));
        System.out.println(getWeekSpace(from, to));
    }
}
