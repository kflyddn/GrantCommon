package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.entity.GrantUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantUserMapper extends BaseDao<GrantUser ,Long> {
    int countByExample(GrantUserExample example);

    int deleteByExample(GrantUserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(GrantUser record);

    int insertSelective(GrantUser record);

    List<GrantUser> selectByExample(GrantUserExample example);

    GrantUser selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") GrantUser record, @Param("example") GrantUserExample example);

    int updateByExample(@Param("record") GrantUser record, @Param("example") GrantUserExample example);

    int updateByPrimaryKeySelective(GrantUser record);

    int updateByPrimaryKey(GrantUser record);
}