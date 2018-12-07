package cn.pcshao.pic.ao;

/**
 * @author pcshao.cn
 * @date 2018/12/4
 */
public class ResultFtp {

    private String ftpPath;
    private String localPath;

    private boolean flag;
    private float filesize;
    private Object data;

    @Override
    public String toString() {
        return "ResultFtp{" +
                "ftpPath='" + ftpPath + '\'' +
                ", flag=" + flag +
                ", filesize=" + filesize +
                ", data=" + data +
                '}';
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public float getFilesize() {
        return filesize;
    }

    public void setFilesize(float filesize) {
        this.filesize = filesize;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
