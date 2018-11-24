package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantUser;
import java.util.List;

public interface GrantUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(GrantUser record);

    GrantUser selectByPrimaryKey(Long userId);

    List<GrantUser> selectAll();

    int updateByPrimaryKey(GrantUser record);
}