package cn.pcshao.pic.ao;

/**
 * @author pcshao.cn
 * @date 2018/12/4
 */
public class ResultFtp {

    private String ftpPath;
    private boolean flag;
    private double filesize;
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

    public double getFilesize() {
        return filesize;
    }

    public void setFilesize(double filesize) {
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
