package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.GrantLog;

public interface GrantLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GrantLog record);

    int insertSelective(GrantLog record);

    GrantLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GrantLog record);

    int updateByPrimaryKey(GrantLog record);
}