package cn.pcshao.graduaction.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author pcshao.cn
 * @date 2019-03-11
 */
public class HadoopUtil {

    private String hadoopURI;
    private Configuration conf;
    private URI uri;
    private FileSystem fs;

    public HadoopUtil(String hadoopURI, Configuration conf){
        this.hadoopURI = hadoopURI;
        this.conf = conf;
    }

    public FileSystem getFs() throws IOException, URISyntaxException {
        uri = connect();
        fs = FileSystem.get(uri, conf);
        return fs;
    }

    public URI connect() throws URISyntaxException {
        return new URI(hadoopURI);
    }

    public void closeFs() throws IOException {
        fs.close();
    }

}
