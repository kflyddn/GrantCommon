package cn.pcshao.grant.common.util;

import java.io.File;

/**
 * @author pcshao.cn
 * @date 2019-04-09
 */
public class FileUtils {

    public static boolean checkFileExist(String filePath){
        return new File(filePath).exists();
    }

    public static void deleteDir(String directory){
        File file = new File(directory);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f : files){
                f.delete();
            }
        }
    }
}
