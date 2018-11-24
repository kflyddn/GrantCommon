package cn.pcshao.grant.common.base;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * 基础Service类
 *  将BaseDao拿过来注入
 *  提供CRUD
 */
public abstract class BaseServiceImpl<Model, PK> implements BaseService<Model, PK> {

    /**
     * 在之后的ServiceImpl各种子类中实现getDao返回Mapper等等
     *  一定需要通过子类实现
     * @return
     */
    public abstract BaseDao<Model, PK> getDao();

    /**
     * 实现部分父类抽象方法
     */
    public int insert(Model model) {
        return getDao().insertSelective(model);
    }

    public int update(Model model) {
        return getDao().updateByPrimaryKeySelective(model);
    }

    public int delete(PK id) {
        return getDao().deleteByPrimaryKey(id);
    }

    public Model selectById(PK id) {
        return getDao().selectByPrimaryKey(id);
    }
}
