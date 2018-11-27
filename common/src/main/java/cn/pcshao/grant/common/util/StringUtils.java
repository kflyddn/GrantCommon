package cn.pcshao.grant.common.util;

/**
 * String工具类
 * @author pcshao
 * @date 2018-11-27
 * 常用字符串工具类
 */
public class StringUtils {

    public static boolean isEmpty(String src){
        return src==null || src.trim().equals("") || src.length()==0 ? true :false;
    }

    public static boolean isNotEmpty(String src){
        return !isEmpty(src);
    }
}
