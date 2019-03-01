package cn.pcshao.graduaction.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.grant.common.entity.GrantTemp;

import java.util.List;

/**
 * 初始为了提供上传文件进度记录
 *  可以换成redis的
 * @author pcshao.cn
 * @date 2019-03-01
 */
public interface GrantTempService extends BaseService<GrantTemp, Integer> {

    /**
     * 通过操作名获取相关属性
     * @param name
     * @return
     */
    List<GrantTemp> selectByName(String name);

}
