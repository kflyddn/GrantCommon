package cn.pcshao.grant.common.entity;

import java.io.Serializable;
import java.util.Date;

public class GrantTaskResult implements Serializable {
    private Integer id;

    private Integer taskId;

    private Date createTime;

    private String f1;

    private String f2;

    private String f3;

    private String f4;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1 == null ? null : f1.trim();
    }

    public String getF2() {
        return f2;
    }

    public void setF2(String f2) {
        this.f2 = f2 == null ? null : f2.trim();
    }

    public String getF3() {
        return f3;
    }

    public void setF3(String f3) {
        this.f3 = f3 == null ? null : f3.trim();
    }

    public String getF4() {
        return f4;
    }

    public void setF4(String f4) {
        this.f4 = f4 == null ? null : f4.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", taskId=").append(taskId);
        sb.append(", createTime=").append(createTime);
        sb.append(", f1=").append(f1);
        sb.append(", f2=").append(f2);
        sb.append(", f3=").append(f3);
        sb.append(", f4=").append(f4);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}