package cn.pcshao.hs.ggzy.bo;

import cn.pcshao.grant.common.base.BasePageBo;
import cn.pcshao.hs.ggzy.entity.Requestpubjour;

import java.util.List;

/**
 * ggzy辅助操作对象
 * 1.使用时务必实例化对应的结果集列表，方能在反射机制中寻址到对应实体类
 * @author pcshao.cn
 * @date 2020-02-22
 */
public class OperateBo extends BasePageBo {

    private String sql;
    private List<Requestpubjour> requestpubjourList;

    public OperateBo(String sql) {
        this.sql = sql;
    }

    public List<Requestpubjour> getRequestpubjourList() {
        return requestpubjourList;
    }

    public void setRequestpubjourList(List<Requestpubjour> requestpubjourList) {
        this.requestpubjourList = requestpubjourList;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
