package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantRolePermission;
import java.util.List;

public interface GrantRolePermissionMapper {
    int deleteByPrimaryKey(Long rolePermissionId);

    int insert(GrantRolePermission record);

    GrantRolePermission selectByPrimaryKey(Long rolePermissionId);

    List<GrantRolePermission> selectAll();

    int updateByPrimaryKey(GrantRolePermission record);
}