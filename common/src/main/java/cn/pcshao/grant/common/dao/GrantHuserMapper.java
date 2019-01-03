package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantHuserMapper extends BaseDao<GrantHuser, Long> {
    int countByExample(GrantHuserExample example);

    int deleteByExample(GrantHuserExample example);

    int deleteByPrimaryKey(Long huserId);

    int insert(GrantHuser record);

    int insertSelective(GrantHuser record);

    List<GrantHuser> selectByExample(GrantHuserExample example);

    GrantHuser selectByPrimaryKey(Long huserId);

    int updateByExampleSelective(@Param("record") GrantHuser record, @Param("example") GrantHuserExample example);

    int updateByExample(@Param("record") GrantHuser record, @Param("example") GrantHuserExample example);

    int updateByPrimaryKeySelective(GrantHuser record);

    int updateByPrimaryKey(GrantHuser record);
}