package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantPermission;
import cn.pcshao.grant.common.entity.GrantPermissionExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantPermissionMapper {
    int countByExample(GrantPermissionExample example);

    int deleteByExample(GrantPermissionExample example);

    int insert(GrantPermission record);

    int insertSelective(GrantPermission record);

    List<GrantPermission> selectByExample(GrantPermissionExample example);

    int updateByExampleSelective(@Param("record") GrantPermission record, @Param("example") GrantPermissionExample example);

    int updateByExample(@Param("record") GrantPermission record, @Param("example") GrantPermissionExample example);
}