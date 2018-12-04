package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.AlbumType;
import cn.pcshao.grant.common.entity.AlbumTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AlbumTypeMapper {
    int countByExample(AlbumTypeExample example);

    int deleteByExample(AlbumTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AlbumType record);

    int insertSelective(AlbumType record);

    List<AlbumType> selectByExample(AlbumTypeExample example);

    AlbumType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AlbumType record, @Param("example") AlbumTypeExample example);

    int updateByExample(@Param("record") AlbumType record, @Param("example") AlbumTypeExample example);

    int updateByPrimaryKeySelective(AlbumType record);

    int updateByPrimaryKey(AlbumType record);
}