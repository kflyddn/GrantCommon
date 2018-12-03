package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantRole;
import cn.pcshao.grant.common.entity.GrantRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantRoleMapper extends BaseDao<GrantRole, Short> {
    /**
     * 根据权限ID查角色列表
     * @param permissionId
     * @return
     */
    List<GrantRole> selectRolesByPermissionId(Long permissionId);

    /**
     * 根据用户ID查角色列表
     * @param userId
     * @return
     */
    List<GrantRole> selectRolesByUserId(Long userId);

    int countByExample(GrantRoleExample example);

    int deleteByExample(GrantRoleExample example);

    int deleteByPrimaryKey(Short roleId);

    int insert(GrantRole record);

    int insertSelective(GrantRole record);

    List<GrantRole> selectByExample(GrantRoleExample example);

    GrantRole selectByPrimaryKey(Short roleId);

    int updateByExampleSelective(@Param("record") GrantRole record, @Param("example") GrantRoleExample example);

    int updateByExample(@Param("record") GrantRole record, @Param("example") GrantRoleExample example);

    int updateByPrimaryKeySelective(GrantRole record);

    int updateByPrimaryKey(GrantRole record);
}