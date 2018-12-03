package cn.pcshao.graduaction.bo;

import cn.pcshao.grant.common.entity.GrantPermission;
import cn.pcshao.grant.common.entity.GrantRole;
import cn.pcshao.grant.common.entity.GrantUser;

/**
 * 系统管理页面的业务对象
 * @author pcshao.cn
 * @date 2018-12-03
 *  分页查询
 *  复杂条件查询
 */
public class SysPageBo {

    private Integer pageNum;
    private Integer pageSize;

    private GrantUser grantUser;
    private GrantRole grantRole;
    private GrantPermission grantPermission;

    public boolean checkSelf(){
        if(null != pageNum || null != pageSize)
            return true;
        return false;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public GrantUser getGrantUser() {
        return grantUser;
    }

    public void setGrantUser(GrantUser grantUser) {
        this.grantUser = grantUser;
    }

    public GrantRole getGrantRole() {
        return grantRole;
    }

    public void setGrantRole(GrantRole grantRole) {
        this.grantRole = grantRole;
    }

    public GrantPermission getGrantPermission() {
        return grantPermission;
    }

    public void setGrantPermission(GrantPermission grantPermission) {
        this.grantPermission = grantPermission;
    }
}
