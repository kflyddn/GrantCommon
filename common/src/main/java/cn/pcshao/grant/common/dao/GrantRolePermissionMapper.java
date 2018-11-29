package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantRolePermission;
import cn.pcshao.grant.common.entity.GrantRolePermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantRolePermissionMapper {
    int countByExample(GrantRolePermissionExample example);

    int deleteByExample(GrantRolePermissionExample example);

    int deleteByPrimaryKey(Long rolePermissionId);

    int insert(GrantRolePermission record);

    int insertSelective(GrantRolePermission record);

    List<GrantRolePermission> selectByExample(GrantRolePermissionExample example);

    GrantRolePermission selectByPrimaryKey(Long rolePermissionId);

    int updateByExampleSelective(@Param("record") GrantRolePermission record, @Param("example") GrantRolePermissionExample example);

    int updateByExample(@Param("record") GrantRolePermission record, @Param("example") GrantRolePermissionExample example);

    int updateByPrimaryKeySelective(GrantRolePermission record);

    int updateByPrimaryKey(GrantRolePermission record);
}