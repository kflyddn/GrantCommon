package cn.pcshao.grant.common.util;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author pcshao.cn
 * @date 2019/1/8
 */
public class JSONUtils {

    Logger logger = LoggerFactory.getLogger(JSONUtils.class);

    /**
     * 从json HASH表达式中获取一个map，改map支持嵌套功能
     * @param jsonString
     * @return
     */
    public static Map<String,Object> getMapFromJson(String jsonString) {
        Map<String, Object> valueMap = null;
        if(StringUtils.isNotEmpty(jsonString)) {
            JSONObject jsonObject = JSONObject.fromObject(jsonString);
            Iterator<?> keyIter = jsonObject.keys();
            String key;
            Object value;
            valueMap = new HashMap<>();
            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
        }
        return valueMap;
    }

    /**
     * map转json
     * @param paramMap
     * @return
     */
    public static String getJsonFromMap(Map<String, Object> paramMap){
        String value = null;
        if(null != paramMap) {
            JSONObject jsonObject = JSONObject.fromObject(paramMap);
            if(null != jsonObject){
                value = jsonObject.toString();
            }
        }
        return value;
    }
}
