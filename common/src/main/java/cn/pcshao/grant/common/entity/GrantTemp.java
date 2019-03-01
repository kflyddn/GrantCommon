package cn.pcshao.grant.common.entity;

import java.io.Serializable;
import java.util.Date;

public class GrantTemp implements Serializable {
    private Integer id;

    private Integer operId;

    private String operName;

    private Date createTime;

    private String c1;

    private String c2;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperId() {
        return operId;
    }

    public void setOperId(Integer operId) {
        this.operId = operId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName == null ? null : operName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1 == null ? null : c1.trim();
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2 == null ? null : c2.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", operId=").append(operId);
        sb.append(", operName=").append(operName);
        sb.append(", createTime=").append(createTime);
        sb.append(", c1=").append(c1);
        sb.append(", c2=").append(c2);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}