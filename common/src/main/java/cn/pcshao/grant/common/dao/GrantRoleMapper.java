package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantRole;
import cn.pcshao.grant.common.entity.GrantRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantRoleMapper extends BaseDao<GrantRole, Short> {
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