package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantTask;
import cn.pcshao.grant.common.entity.GrantTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantTaskMapper extends BaseDao<GrantTask, Integer> {
    int countByExample(GrantTaskExample example);

    int deleteByExample(GrantTaskExample example);

    int deleteByPrimaryKey(Integer taskId);

    int insert(GrantTask record);

    int insertSelective(GrantTask record);

    List<GrantTask> selectByExample(GrantTaskExample example);

    GrantTask selectByPrimaryKey(Integer taskId);

    int updateByExampleSelective(@Param("record") GrantTask record, @Param("example") GrantTaskExample example);

    int updateByExample(@Param("record") GrantTask record, @Param("example") GrantTaskExample example);

    int updateByPrimaryKeySelective(GrantTask record);

    int updateByPrimaryKey(GrantTask record);
}