package cn.pcshao.hs.ggzy.service;

import cn.pcshao.grant.common.base.BaseService;
import cn.pcshao.hs.ggzy.bo.OperateBo;

/**
 * Oracle数据库服务
 * @author pcshao.cn
 * @date 2020-02-22
 */
public interface DBService extends BaseService<OperateBo, OperateBo> {

    boolean getOracleConnect();

}
