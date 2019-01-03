package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.entity.GrantHuser;

import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019/1/3
 */
public interface HUserService extends BaseService<GrantHuser, Long> {

    /**
     * 根据userId获取hUser
     * 一人一档
     * @return
     */
    List<GrantHuser> getHUsersByUserId(Long userId);

}
