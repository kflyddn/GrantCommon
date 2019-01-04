package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.HUserService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019/1/3
 */
@Service("hUserServiceImpl")
public class HUserServiceImpl extends BaseServiceImpl<GrantHuser, Long> implements HUserService {

    @Resource
    private GrantHuserMapper grantHuserMapper;

    @Override
    public BaseDao getDao() {
        return grantHuserMapper;
    }

    @Override
    public GrantHuser selectById(Long id) {
        return super.selectById(id);
    }

    @Override
    public List<GrantHuser> getHUsersByUserId(Long userId) {
        GrantHuserExample example = new GrantHuserExample();
        GrantHuserExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<GrantHuser> grantHUserList = grantHuserMapper.selectByExample(example);
        return grantHUserList;
    }

    @Override
    public int addHUserFile(GrantHuser huser) {
        return 0;
    }

    @Override
    public int insertBatch(List<GrantHuser> huserList) {
        return 0;
    }

    @Override
    public int editHUserFile(GrantHuser huser) {
        return 0;
    }
}
