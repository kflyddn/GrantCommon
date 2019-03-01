package cn.pcshao.graduaction.service.impl;

import cn.pcshao.graduaction.service.HUserService;
import cn.pcshao.grant.common.base.BaseDao;
import cn.pcshao.grant.common.base.BaseServiceImpl;
import cn.pcshao.grant.common.dao.GrantHuserMapper;
import cn.pcshao.grant.common.dao.GrantM2hStateMapper;
import cn.pcshao.grant.common.entity.GrantHuser;
import cn.pcshao.grant.common.entity.GrantHuserExample;
import cn.pcshao.grant.common.entity.GrantM2hStateExample;
import cn.pcshao.grant.common.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019/1/3
 */
@Service("hUserServiceImpl")
public class HUserServiceImpl extends BaseServiceImpl<GrantHuser, Long> implements HUserService {

    @Resource
    private GrantHuserMapper grantHuserMapper;

    @Resource
    private GrantM2hStateMapper grantM2hStateMapper;

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
    public int insertBatch(List<GrantHuser> hUserList) {
        return grantHuserMapper.insertBatch(hUserList);
    }

    @Override
    public int editHUserFile(GrantHuser huser) {
        return 0;
    }

    @Override
    public List<GrantHuser> getUsersFromList(List<List> list) {
        List<GrantHuser> users = new ArrayList<>();
        int colLength = list.get(0).size();
        int[] indexLocation = new int[colLength+1];
        for (int i = 0; i < list.size(); i++) {
            List row = list.get(i);
            GrantHuser user = new GrantHuser();
            for (int j = 0; j < row.size(); j++) {
                if(indexLocation[0] != colLength){
                    if(PropertiesUtil.getBusinessConfig("importHUsersTemplate.idCard").equals(row.get(j).toString())){
                        indexLocation[1]=j;
                        indexLocation[0]++;
                    }else if(PropertiesUtil.getBusinessConfig("importHUsersTemplate.name").equals(row.get(j).toString())){
                        indexLocation[2]=j;
                        indexLocation[0]++;
                    }else if(PropertiesUtil.getBusinessConfig("importHUsersTemplate.sex").equals(row.get(j).toString())){
                        indexLocation[3]=j;
                        indexLocation[0]++;
                    }else if(PropertiesUtil.getBusinessConfig("importHUsersTemplate.email").equals(row.get(j).toString())){
                        indexLocation[4]=j;
                        indexLocation[0]++;
                    }else if(PropertiesUtil.getBusinessConfig("importHUsersTemplate.telephone").equals(row.get(j).toString())){
                        indexLocation[5]=j;
                        indexLocation[0]++;
                    }
                    //如果增加字段往后加，自动记录下标标记
                }
            }
            user.setIdCard(row.get(indexLocation[1]).toString());
            user.setName(row.get(indexLocation[2]).toString());
            user.setSex(row.get(indexLocation[3]).toString().equals("1")?true:false);
            user.setEmail(row.get(indexLocation[4]).toString());
            user.setTelephone(row.get(indexLocation[5]).toString());
            users.add(user);
        }
        users.remove(0); //移除表头
        return users;
    }

    @Override
    public Float getSynchronizedProcess() {
        Long minHUserId = grantHuserMapper.getMinHUserId();
        GrantM2hStateExample example = new GrantM2hStateExample();
        GrantM2hStateExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo("1");
        criteria.andHuserIdGreaterThanOrEqualTo(minHUserId);
        int sucessNum = grantM2hStateMapper.countByExample(example);
        GrantHuserExample huserExample = new GrantHuserExample();
        int allNum = 1;
        allNum = grantHuserMapper.countByExample(huserExample);
        return allNum==1? 0f : (float)sucessNum/allNum*100;
    }

    @Override
    public Float getUpdateProcess() {
        return null;
    }
}
