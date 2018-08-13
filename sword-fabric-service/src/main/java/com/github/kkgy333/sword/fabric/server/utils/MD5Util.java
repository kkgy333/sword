package com.github.kkgy333.sword.fabric.server.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
public class MD5Util {

    /**
     * 将字符串md5散列值24位
     *
     * @param content
     *            散列值内容
     * @return 散列值结果
     */
    public static String md516(String content) {
        String str = md5(content);
        return str.substring(8, 24);
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @return 密文
     */
    public static String md5(String text) {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text);
        System.out.println("MD5:encodeStr=" + encodeStr);
        return encodeStr;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param md5  密文
     * @return true/false
     */
    public static boolean verify(String text, String md5) {
        //根据传入的密钥进行验证
        String md5Text = md5(text);
        return md5Text.equalsIgnoreCase(md5);
    }

}
