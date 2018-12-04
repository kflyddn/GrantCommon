package cn.pcshao.grant.common.dao;

import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.bo.AlbumSource;
import cn.pcshao.grant.common.entity.AlbumPicPublic;
import cn.pcshao.grant.common.entity.AlbumPicPublicExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AlbumPicPublicMapper extends BaseDao<AlbumSource, Integer> {
    int countByExample(AlbumPicPublicExample example);

    int deleteByExample(AlbumPicPublicExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AlbumPicPublic record);

    int insertSelective(AlbumPicPublic record);

    List<AlbumPicPublic> selectByExample(AlbumPicPublicExample example);

    AlbumPicPublic selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AlbumPicPublic record, @Param("example") AlbumPicPublicExample example);

    int updateByExample(@Param("record") AlbumPicPublic record, @Param("example") AlbumPicPublicExample example);

    int updateByPrimaryKeySelective(AlbumPicPublic record);

    int updateByPrimaryKey(AlbumPicPublic record);
}