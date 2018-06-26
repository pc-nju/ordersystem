package com.imooc.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
/**
 * @author **
 * @date 2018/6/9 10:05
 */
public final class Md5Util {
    private static final char DIGITS[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * 获取文件的MD5码
     *
     * @param absPath
     *            文件路径
     * @return 文件的MD5码
     */
    public static String getFileMD5(String absPath) {
        try {
            File file = new File(absPath);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            FileChannel filechannel = fis.getChannel();
            MappedByteBuffer mbb = filechannel
                    .map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            mdTemp.update(mbb);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = DIGITS[byte0 >>> 4 & 0xf];
                str[k++] = DIGITS[byte0 & 0xf];
            }
            fis.close();
            return new String(str);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取指定字符串的MD5码
     *
     * @param s 字符串
     * @return 字符串的MD5码
     */
    public static String getMD5(String s) {
        return getMD5WithSalt(s, null);
    }

    /**
     * 根据盐值获取指定字符串的MD5码
     *
     * @param s 字符串
     * @return 字符串的MD5码
     */
    public static String getMD5WithSalt(String s, String salt) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(addSalt(s, salt).getBytes());
            byte[] md = digest.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 为加密对象撒盐
     * @param object 待加密对象
     * @param salt 盐（秘钥）
     * @return 撒盐后的结果
     */
    private static String addSalt(String object, String salt) {
        if(object == null){
            object = "";
        }
        if(salt == null || "".equals(salt)){
            return object;
        } else {
            return object + "{"+ salt +"}";
        }
    }
}
