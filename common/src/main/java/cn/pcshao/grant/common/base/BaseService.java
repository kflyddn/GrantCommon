package cn.pcshao.grant.common.base;

/**
 * @author pcshao.cn
 * @date 2018-11-24
 * 服务基础接口
 *  封装CRUD
 *
 * Model: 操作实体类
 * PK: 对应的主键
 */
public interface BaseService<Model, PK> {

    /**
     * C
     * @param model
     * @return
     */
    int insert(Model model);

    /**
     * U
     * @param model
     * @return
     */
    int update(Model model);

    /**
     * D
     * @param id
     * @return
     */
    int delete(PK id);

    /**
     * R
     * @param id
     * @return
     */
    Model selectById(PK id);

}
