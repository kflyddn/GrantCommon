package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantPermission;
import java.util.List;

public interface GrantPermissionMapper {
    int deleteByPrimaryKey(Long permissionId);

    int insert(GrantPermission record);

    GrantPermission selectByPrimaryKey(Long permissionId);

    List<GrantPermission> selectAll();

    int updateByPrimaryKey(GrantPermission record);
}