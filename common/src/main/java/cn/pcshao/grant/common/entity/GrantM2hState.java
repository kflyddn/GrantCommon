package cn.pcshao.grant.common.entity;

import java.io.Serializable;
import java.util.Date;

public class GrantM2hState implements Serializable {
    private Long id;

    private Long huserId;

    private Byte state;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public GrantM2hState(Long huserId, Byte state, Date createTime) {
        this.huserId = huserId;
        this.state = state;
        this.createTime = createTime;
    }

    public GrantM2hState() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHuserId() {
        return huserId;
    }

    public void setHuserId(Long huserId) {
        this.huserId = huserId;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}