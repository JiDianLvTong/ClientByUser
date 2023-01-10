package com.android.jidian.repair.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * Modified by fight on 2019/12/25
 */
public class Md5 {

    private static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * $time = 时间戳 #如 1576467001
     * aptk = md5($time 连接 '#mixiangx.com#' + $time) 连接 '-' 连接 $time;
     *
     * @return aptk字符串
     * 合并时请注意保留：这里是更改后的aptk获取方法
     */
    public static String getAptk() {
        final String magicString = "#jidianlvtong.com#";
        long epochSeconds;//$time
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            epochSeconds = Instant.now().getEpochSecond();
        } else {
            epochSeconds = System.currentTimeMillis() / 1000;
        }
        String afterMd5Process = stringToMD5(epochSeconds + magicString + epochSeconds);
        return afterMd5Process + "-" + epochSeconds;
    }

//    public String getDateToken() {
//        final String token_key = "K7JEUN6BH116M93A32A7H8R8BD9I505B";
//        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String date = sDateFormat.format(new java.util.Date());
//        String dateMd5 = stringToMD5(date);
//        String all = dateMd5 + token_key;
//        String allMd5 = stringToMD5(all);
//        return allMd5;
//    }

}
