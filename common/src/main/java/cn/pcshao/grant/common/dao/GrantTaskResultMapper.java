package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantTaskResult;
import cn.pcshao.grant.common.entity.GrantTaskResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantTaskResultMapper {
    int countByExample(GrantTaskResultExample example);

    int deleteByExample(GrantTaskResultExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GrantTaskResult record);

    int insertSelective(GrantTaskResult record);

    List<GrantTaskResult> selectByExample(GrantTaskResultExample example);

    GrantTaskResult selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GrantTaskResult record, @Param("example") GrantTaskResultExample example);

    int updateByExample(@Param("record") GrantTaskResult record, @Param("example") GrantTaskResultExample example);

    int updateByPrimaryKeySelective(GrantTaskResult record);

    int updateByPrimaryKey(GrantTaskResult record);
}