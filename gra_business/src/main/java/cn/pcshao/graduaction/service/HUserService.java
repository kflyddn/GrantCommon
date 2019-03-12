package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.entity.GrantHuser;

import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019/1/3
 */
public interface HUserService extends BaseService<GrantHuser, Long> {

    /**
     * 根据userId获取hUser
     * 一人一档
     * @return
     */
    List<GrantHuser> getHUsersByUserId(Long userId);

    /**
     * 批量插入
     * @param huserList
     * @return
     */
    int insertBatch(List<GrantHuser> huserList);

    /**
     * 编辑用户档案
     *  一次机会限定，之后需要申请
     * @param huser
     * @return
     */
    int editHUserFile(GrantHuser huser);

    /**
     * 转换
     * @param excels
     * @return
     */
    List<GrantHuser> getUsersFromList(List<List> excels);

    /**
     * 获取mysql同步hdfs的进度
     *  同步状态表中同步成功、hID大于等于源数据表的最小ID的数据总数 / 源数据表中的数据总数
     * @return
     */
    Float getSynchronizedProcess();

    void resetDB();
}
