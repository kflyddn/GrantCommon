package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantRolePermission;
import cn.pcshao.grant.common.entity.GrantRolePermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantRolePermissionMapper {
    int countByExample(GrantRolePermissionExample example);

    int deleteByExample(GrantRolePermissionExample example);

    int insert(GrantRolePermission record);

    int insertSelective(GrantRolePermission record);

    List<GrantRolePermission> selectByExample(GrantRolePermissionExample example);

    int updateByExampleSelective(@Param("record") GrantRolePermission record, @Param("example") GrantRolePermissionExample example);

    int updateByExample(@Param("record") GrantRolePermission record, @Param("example") GrantRolePermissionExample example);
}