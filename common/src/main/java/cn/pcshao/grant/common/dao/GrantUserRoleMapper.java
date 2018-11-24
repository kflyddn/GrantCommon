package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantUserRole;
import java.util.List;

public interface GrantUserRoleMapper {
    int deleteByPrimaryKey(Long userRoleId);

    int insert(GrantUserRole record);

    GrantUserRole selectByPrimaryKey(Long userRoleId);

    List<GrantUserRole> selectAll();

    int updateByPrimaryKey(GrantUserRole record);
}