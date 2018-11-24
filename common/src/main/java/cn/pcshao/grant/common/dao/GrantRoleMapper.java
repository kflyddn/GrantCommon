package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantRole;
import java.util.List;

public interface GrantRoleMapper {
    int deleteByPrimaryKey(Short roleId);

    int insert(GrantRole record);

    GrantRole selectByPrimaryKey(Short roleId);

    List<GrantRole> selectAll();

    int updateByPrimaryKey(GrantRole record);
}