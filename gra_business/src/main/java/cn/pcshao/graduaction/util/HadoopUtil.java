package cn.pcshao.graduaction.util;

import cn.pcshao.grant.common.consts.DtoCodeConsts;
import cn.pcshao.grant.common.exception.CustomException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 当前hadoop连接
 *  持有当前配置
 *  持有当前连接地址
 *  持有当前dfs文件系统
 *
 * 在释放连接时最好调用close方法
 *
 * 静态方法clearDFS()会影响filesystem的连接，测试需要注意
 * @author pcshao.cn
 * @date 2019-03-11
 */
public class HadoopUtil {

    private Configuration conf;
    private String hadoopURI;
    private URI uri;
    private FileSystem fs = null;

    public HadoopUtil(String hadoopURI, Configuration conf){
        this.hadoopURI = hadoopURI;
        this.conf = conf;
    }

    public void copyToLocalFile(Path path, String localPath) {
        checkFsConnect();
        try {
            fs.copyToLocalFile(path, new Path(localPath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(DtoCodeConsts.HADOOP_HDFS_DST_PATH_FAIL, DtoCodeConsts.HADOOP_HDFS_DST_PATH_FAIL_MSG);
        }
    }

    public void copyFromLocalFile(String localPath, String destPath){
        checkFsConnect();
        try {
            fs.copyFromLocalFile(new Path(localPath), new Path(destPath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException(DtoCodeConsts.HADOOP_HDFS_DST_PATH_FAIL, DtoCodeConsts.HADOOP_HDFS_DST_PATH_FAIL_MSG);
        }
    }

    public List<Path> getPathFromDFS(String destPath, boolean recursive) {
        checkFsConnect();
        List<Path> pathList = new ArrayList<>();
        try {
            RemoteIterator<LocatedFileStatus> files = fs.listFiles(new Path(destPath), recursive); //递归false
            while (files.hasNext()){
                LocatedFileStatus next = files.next();
                Path path = next.getPath();
                pathList.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathList;
    }

    /**
     * 获取FileSystem连接
     * @return
     */
    public FileSystem getFs() {
        if(null == fs) {
            try {
                uri = connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                throw new CustomException(DtoCodeConsts.HADOOP_CONNECT_FAIL, DtoCodeConsts.HADOOP_CONNECT_FAIL_MSG);
            }
            try {
                fs = FileSystem.get(uri, conf);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException(DtoCodeConsts.HADOOP_HDFS_CONNECT_FAIL, DtoCodeConsts.HADOOP_HDFS_CONNECT_FAIL_MSG);
            }
        }
        return fs;
    }

    private URI connect() throws URISyntaxException {
        return new URI(hadoopURI);
    }

    public static void clearHdfs(String hadoopURI, String hdfsLocatePath) throws IOException, URISyntaxException {
        Configuration conf = new Configuration();
        HadoopUtil hadoopUtil = new HadoopUtil(hadoopURI, conf);
        FileSystem fs = null;
        fs = hadoopUtil.getFs();
        fs.deleteOnExit(new Path(hdfsLocatePath));
        fs.close();
    }

    /**
     * 检查Hadoop FileSystem连接
     */
    private void checkFsConnect(){
        if(null == fs)
            getFs();
    }

    /**
     * 在释放Hadoop连接时最好调用此方法
     * @throws IOException
     */
    public void closeFs() throws IOException {
        fs.close();
    }
}
