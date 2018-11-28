package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.entity.GrantUser;

public interface UserService extends BaseService<GrantUser, Long> {

    /**
     * 基础校验方法
     * @param username
     * @param password
     * @return
     */
    GrantUser doAuth(String username, String password);

}
