package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;

public interface UserService<GrantUser, Long> extends BaseService<GrantUser, Long> {

    /**
     * 基础校验方法
     * @param username
     * @param password
     * @return
     */
    int doAuth(String username, String password);

}
