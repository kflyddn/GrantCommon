package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.GrantTempService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantTempMapper;
import cn.pcshao.grant.common.entity.GrantTemp;
import cn.pcshao.grant.common.entity.GrantTempExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author pcshao.cn
 * @date 2019-03-01
 */
@Service("gTempService")
public class GrantTempServiceImpl extends BaseServiceImpl<GrantTemp, Integer> implements GrantTempService {

    @Resource
    private GrantTempMapper grantTempMapper;

    @Override
    public BaseDao<GrantTemp, Integer> getDao() {
        return grantTempMapper;
    }

    @Override
    public List<GrantTemp> selectByName(String name) {
        GrantTempExample tempExample = new GrantTempExample();
        GrantTempExample.Criteria criteria = tempExample.createCriteria();
        criteria.andOperNameEqualTo(name);
        List<GrantTemp> grantTemps = grantTempMapper.selectByExample(tempExample);
        return grantTemps;
    }
}
