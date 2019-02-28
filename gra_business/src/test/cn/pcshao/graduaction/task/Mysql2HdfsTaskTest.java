package cn.pcshao.graduaction.task;

import cn.pcshao.grant.common.entity.GrantHuser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2019-02-28
 */
public class Mysql2HdfsTaskTest {

    @Test
    public void TestObj2file(){
        GrantHuser huser = new GrantHuser();
        huser.setHuserId(1001l);
        huser.setEmail("119123@qq.com");
        huser.setUserId(8888l);
        huser.setName("测试姓名");
        huser.setTelephone("18012341111");
        List<GrantHuser> husers = new ArrayList<>();
        husers.add(huser);
        huser.setHuserId(1002l);
        huser.setName("测试姓名2");
        husers.add(huser);
        new Mysql2HdfsTask().obj2file(husers, "E:\\tempFile");
    }
}
