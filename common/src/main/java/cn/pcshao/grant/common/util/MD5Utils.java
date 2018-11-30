package cn.pcshao.grant.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 * @author pcshao
 * @date 2018-11-30
 */
public class MD5Utils {

    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String transMD5Code(String src){
        String temp = new String(src);
        try {
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] a  = md.digest(src.getBytes());
            temp = byteToString(a);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String transMD5Code(String src, int num){
        String ret = new String(src);
        while(num-->0) {
            ret = transMD5Code(ret);
        };
        return ret;
    }

    private static String byteToString(byte[] digest) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            sb.append(byteToArrayString(digest[i]));
        }
        return sb.toString();
    }
    /**
     * 输入的byte有负数值的
     * @param b
     * @return 在16位字符数组里找位置取值
     */
    private static String byteToArrayString(byte b) {
        int bb = b;
        if(b<0)
            bb = b + 256;
        int c1 = bb/16;
        int c2 = bb%16;
        return strDigits[c1]+strDigits[c2];
    }

}
