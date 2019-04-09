package cn.pcshao.graduaction.task;

import cn.pcshao.graduaction.util.HadoopUtil;
import cn.pcshao.grant.common.entity.GrantHuser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019-02-28
 */
public class Mysql2HdfsTaskTest {

    @Test
    public void TestObj2file(){
        GrantHuser huser = new GrantHuser();
        huser.setHuserId(1001l);
        huser.setEmail("119123@qq.com");
        huser.setUserId(8888l);
        huser.setName("测试姓名");
        huser.setTelephone("18012341111");
        List<GrantHuser> husers = new ArrayList<>();
        husers.add(huser);
        huser.setHuserId(1002l);
        huser.setName("测试姓名2");
        husers.add(huser);
        new Mysql2HdfsTask().obj2file(husers, "E:\\tempFile");
    }

    @Test
    public void read4hdfs(){
        Configuration conf = new Configuration();
        HadoopUtil hadoopUtil = new HadoopUtil("hdfs://192.168.2.100:9000", conf);
        FileSystem fs = hadoopUtil.getFs();
        List<Path> pathList = new ArrayList<>();
        try {
            RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path("/input/test/"), false);
            while (files.hasNext()){
                LocatedFileStatus next = files.next();
                Path path = next.getPath();
                pathList.add(path);
            }
            //
            for(Path path : pathList) {
                fs.copyToLocalFile(path, new Path("E:\\Hado\\localtest\\RECOVER"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void TestFile2obj(){
        List<GrantHuser> husers = new Mysql2HdfsTask().file2obj("E:\\Hado\\localtest\\RECOVER\\1554773955123DUIYR");
        husers.toString();
    }

}
