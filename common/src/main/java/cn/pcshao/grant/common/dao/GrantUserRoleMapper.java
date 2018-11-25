package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantUserRole;
import cn.pcshao.grant.common.entity.GrantUserRoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantUserRoleMapper {
    int countByExample(GrantUserRoleExample example);

    int deleteByExample(GrantUserRoleExample example);

    int insert(GrantUserRole record);

    int insertSelective(GrantUserRole record);

    List<GrantUserRole> selectByExample(GrantUserRoleExample example);

    int updateByExampleSelective(@Param("record") GrantUserRole record, @Param("example") GrantUserRoleExample example);

    int updateByExample(@Param("record") GrantUserRole record, @Param("example") GrantUserRoleExample example);
}