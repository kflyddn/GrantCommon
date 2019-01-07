package cn.pcshao.grant.common.bo;

import cn.pcshao.grant.common.base.BasePageBo;

/**
 * @author pcshao.cn
 * @date 2019/1/7
 */
public class TaskBo extends BasePageBo {

    private Byte state;

    private String type;

    @Override
    public String toString() {
        return "TaskBo{" +
                "state=" + state +
                ", type='" + type + '\'' +
                '}';
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
