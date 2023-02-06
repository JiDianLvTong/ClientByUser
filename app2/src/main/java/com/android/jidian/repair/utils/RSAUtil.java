package com.android.jidian.repair.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {
    private static final String PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuzzG5YdwHZp6q/8Q24Oq\n" +
                    "c1lErANmOwGIgWWcdCevx+qjb8J+642N5SylFj/DBuFTcdE2YrJyc/ivlBkFr8LF\n" +
                    "AvkDB62rY7y+h+sV5WYsIQyluuMgapveprYOJHf3XzdgNC0xNmU7i7cW731D7Nm1\n" +
                    "uNFZgtb74pLLjlGCP1lBL031iqbeX41ui90P0a3nTWIIEsjsPqhG7ACgA83WMPre\n" +
                    "wOF9U3VSTcKrK8jgTb1qs1O0J2HlYhEFD1Vr4SvzPWH7HG+qigm6K0JBMZP+5mTv\n" +
                    "HcoX9tsNO8bVm3ZMsTRZq2W9s+9/FMOgx0PSGmdBCUz63yMmiwCDzeC1yEWKqUPA\n" +
                    "9wIDAQAB";

    //字符串转RSA公钥
    public static PublicKey convertToRSAPublicKey() throws Exception {
        byte[] keyBytes = base642Byte(PUBLIC_KEY);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    //RSA公钥加密
    public static byte[] publicEncrpty(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //编码前设定编码方式及公钥
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //存入编码数据并返回编码结果
        return cipher.doFinal(content);
    }

    //字节数组转Base64编码
    public static byte[] byte2Base64(byte[] bytes) {
        return Base64.encode(bytes, Base64.NO_WRAP);
    }

    //base64字符串转byte[]
    public static byte[] base642Byte(String base64Str) {
        return Base64.decode(base64Str, Base64.NO_WRAP);
    }
}
