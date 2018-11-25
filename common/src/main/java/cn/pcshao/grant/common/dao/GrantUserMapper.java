package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantUser;
import cn.pcshao.grant.common.entity.GrantUserExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface GrantUserMapper extends BaseDao<GrantUser, Long> {
    int countByExample(GrantUserExample example);

    int deleteByExample(GrantUserExample example);

    int insert(GrantUser record);

    int insertSelective(GrantUser record);

    List<GrantUser> selectByExample(GrantUserExample example);

    int updateByExampleSelective(@Param("record") GrantUser record, @Param("example") GrantUserExample example);

    int updateByExample(@Param("record") GrantUser record, @Param("example") GrantUserExample example);
}