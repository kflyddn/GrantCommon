package cn.pcshao.graduaction.task;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author pcshao.cn
 * @date 2019/1/14
 */
public class HadoopTest {

    public void exec() throws URISyntaxException, IOException {
        URI uri = new URI("hdfs://hadoop2:9000");
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(uri, conf);
        //读取流
        FSDataInputStream open = fs.open(new Path("/testFile"));
        //输出流
        FileOutputStream out = new FileOutputStream("D://PCSHAO/Hadoop/testFile");
        IOUtils.copyBytes(open, out, 4096, true); //IOUtils的第三个参数值4096是很多大师级人物在写文件读取时常用的值（4k），第四个参数true的意思是文件写完后返回true。
    }
}
