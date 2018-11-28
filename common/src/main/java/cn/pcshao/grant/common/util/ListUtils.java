package cn.pcshao.grant.common.util;

import java.util.List;

/**
 * List工具类
 * @author pcshao
 * @date 2018-11-28
 * 常用数组工具类
 */
public class ListUtils {

    public static boolean isEmptyList(List list){
        if(null == list || 0 == list.size()){
            return true;
        }
        return false;
    }

    public static boolean isNotEmptyList(List list){
        return !isEmptyList(list);
    }

}
