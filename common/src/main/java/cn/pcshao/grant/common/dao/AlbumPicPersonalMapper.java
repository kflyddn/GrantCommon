package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.entity.AlbumPicPersonal;
import cn.pcshao.grant.common.entity.AlbumPicPersonalExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AlbumPicPersonalMapper {
    int countByExample(AlbumPicPersonalExample example);

    int deleteByExample(AlbumPicPersonalExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AlbumPicPersonal record);

    int insertSelective(AlbumPicPersonal record);

    List<AlbumPicPersonal> selectByExample(AlbumPicPersonalExample example);

    AlbumPicPersonal selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AlbumPicPersonal record, @Param("example") AlbumPicPersonalExample example);

    int updateByExample(@Param("record") AlbumPicPersonal record, @Param("example") AlbumPicPersonalExample example);

    int updateByPrimaryKeySelective(AlbumPicPersonal record);

    int updateByPrimaryKey(AlbumPicPersonal record);
}