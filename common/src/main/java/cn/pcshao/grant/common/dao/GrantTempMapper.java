package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantTemp;
import cn.pcshao.grant.common.entity.GrantTempExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantTempMapper extends BaseDao<GrantTemp, Integer> {
    int countByExample(GrantTempExample example);

    int deleteByExample(GrantTempExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GrantTemp record);

    int insertSelective(GrantTemp record);

    List<GrantTemp> selectByExample(GrantTempExample example);

    GrantTemp selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GrantTemp record, @Param("example") GrantTempExample example);

    int updateByExample(@Param("record") GrantTemp record, @Param("example") GrantTempExample example);

    int updateByPrimaryKeySelective(GrantTemp record);

    int updateByPrimaryKey(GrantTemp record);
}