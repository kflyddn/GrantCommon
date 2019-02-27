package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantM2hState;
import cn.pcshao.grant.common.entity.GrantM2hStateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GrantM2hStateMapper extends BaseDao<GrantM2hState, Long> {
    int countByExample(GrantM2hStateExample example);

    int deleteByExample(GrantM2hStateExample example);

    int deleteByPrimaryKey(Long id);

    int insert(GrantM2hState record);

    int insertSelective(GrantM2hState record);

    List<GrantM2hState> selectByExample(GrantM2hStateExample example);

    GrantM2hState selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") GrantM2hState record, @Param("example") GrantM2hStateExample example);

    int updateByExample(@Param("record") GrantM2hState record, @Param("example") GrantM2hStateExample example);

    int updateByPrimaryKeySelective(GrantM2hState record);

    int updateByPrimaryKey(GrantM2hState record);

    Long getMaxHUserId();

    int insertBatch(@Param("husers") List<GrantHuser> husers);
}