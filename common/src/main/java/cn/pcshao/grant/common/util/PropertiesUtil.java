package cn.pcshao.grant.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * 配置文件工具类
 *  单例
 * @author pcshao.cn
 * @date 2018/12/13
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    /*private static PropertiesUtil propertiesUtils = null;

    public PropertiesUtil getInstance(){
        if(null == propertiesUtils){
            logger.debug("首次加载PropertiesUtils！");
            propertiesUtils = new PropertiesUtil();
        }
        return propertiesUtils;
    }*/

    public static String getMailConfig(String key){
        if(null == loadPropCh("mail-config.properties"))
            return "";
        return loadPropCh("mail-config.properties").getProperty(key);
    }

    public static String getAlbumConfig(String key){
        if(null == loadProp("album.properties"))
            return "";
        return loadProp("album.properties").getProperty(key);
    }

    public static String getBusinessConfig(String key){
        if(null == loadProp("business.properties"))
            return "";
        return loadProp("business.properties").getProperty(key);
    }

    /**
     * 根据配置文件名返回配置文件路径
     * @param prop
     * @return
     */
    public static Properties loadProp(String prop){
        Properties properties = null;
        try {
            properties = PropertiesLoaderUtils.loadAllProperties(prop);
        } catch (IOException e) {
            logger.debug("配置文件不存在或路径错误！");
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 根据配置文件名返回配置文件路径
     *  中文支持
     * @param prop
     * @return
     */
    public static Properties loadPropCh(String prop){
        Properties properties = new Properties();
        InputStream is = null;
        InputStreamReader isReader = null;
        URL resource = null;
        try {
            //类加载器上下文
            ClassLoader classLoader = PropertiesUtil.class.getClassLoader();
            //取到resource
            resource = classLoader.getResource(prop);
            //取流和打包
            is = new FileInputStream(resource.getPath());
            isReader = new InputStreamReader(is, "utf-8");
            properties.load(isReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null != isReader){
                try {
                    isReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}
