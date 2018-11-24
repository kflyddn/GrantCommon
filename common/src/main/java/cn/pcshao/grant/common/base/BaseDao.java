package cn.pcshao.grant.common.base;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * 基础Dao接口
 *  Mybatis整合时Mapper接口继承此BaseDao接口
 *  通过mybatis映射后期方便CRUD间接映射，牛批！
 */
public interface BaseDao<Model, PK> {

    /**
     * 插入对象
     * @param model 对象
     */
    int insertSelective(Model model);

    /**
     * 更新对象
     * @param model 对象
     */
    int updateByPrimaryKeySelective(Model model);

    /**
     * 通过主键, 删除对象
     * @param id 主键
     */
    int deleteByPrimaryKey(PK id);

    /**
     * 通过主键, 查询对象
     * @param id 主键
     * @return
     */
    Model selectByPrimaryKey(PK id);

}
