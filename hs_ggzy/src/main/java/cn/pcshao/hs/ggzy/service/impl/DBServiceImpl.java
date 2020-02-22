package cn.pcshao.hs.ggzy.service.impl;

import cn.pcshao.hs.ggzy.bo.OperateBo;
import cn.pcshao.hs.ggzy.entity.Requestpubjour;
import cn.pcshao.hs.ggzy.service.DBService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2020-02-22
 */
@Service("ggzyService")
@PropertySource({"classpath:ggzy.properties"})
public class DBServiceImpl implements DBService {

    @Value("${ggzy.db.driver}")
    private String driver;
    @Value("${ggzy.db.url}")
    private String url;
    @Value("${ggzy.db.username}")
    private String username;
    @Value("${ggzy.db.password}")
    private String password;
    @Value("${ggzy.db.prop1.key}")
    private String prop1_key;
    @Value("${ggzy.db.prop1.value}")
    private String prop1_value;
    @Value("${ggzy.db.prop2.key}")
    private String prop2_key;
    @Value("${ggzy.db.prop2.value}")
    private String prop2_value;

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet resultSet = null;

    {
        //testOracleConnect();
//        getOracleConnect();
//        OperateBo operateBo = new OperateBo(
//                "select * from hs_asset.requestpubjour where mod_no = 'M202002170644'"
//        );
//        List result = new ArrayList();
//        operateBo.setRequestpubjourList(result);
//        selectById(operateBo);
//        for (int i = 0; i < result.size(); i++) {
//            System.out.println(result.get(i).toString());
//        }
    }

    private void testOracleConnect() {
        String url1 = "jdbc:oracle:thin:@10.20.23.26:1521:CRDT_ZRT";
        String url = "jdbc:oracle:thin:@HS89";
        String username = "hs_asset";
        String password = "hundsun1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        System.setProperty("oracle.net.tns_admin", "C:\\app\\hspcadmin\\product\\11.2.0\\client_1\\network\\admin");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(url, username, password);
            ps = conn.prepareStatement("select * from hs_asset.requestpubjour where mod_no = 'M202002170644'");
            rs = ps.executeQuery();
            while (rs.next()){
                System.out.println("数据库查询："+ rs.getString("OP_REMARK"));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                rs.close();
                ps.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean getOracleConnect() {
        if (prop1_key.length() > 0)
            System.setProperty(prop1_key, prop1_value);
        if (prop2_key.length() > 0)
            System.setProperty(prop2_key, prop2_value);
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int insert(OperateBo operateBo) {
        return 0;
    }

    @Override
    public int update(OperateBo operateBo) {
        return 0;
    }

    @Override
    public int delete(OperateBo bo) {
        int ret = 0;
        checkConnection();
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(bo.getSql());
            ret = ps.execute()? 1:0;
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
            }
        }
        return ret;
    }

    @Override
    public OperateBo selectById(OperateBo bo) {
        checkConnection();
        try {
            ps = conn.prepareStatement(bo.getSql());
            resultSet = ps.executeQuery();
            transResultSet(resultSet, bo);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (ps != null)
                try {
                    ps.close();
                } catch (SQLException e) {
                }
        }
        return bo;
    }

    private void transResultSet(ResultSet resultSet, OperateBo bo) throws SQLException {
        String prex = "cn.pcshao.hs.ggzy.entity";
        Field[] fields = null;
        if (null != bo.getRequestpubjourList()){
            try {
                //获取对应类的成员集
                Class clazz = Class.forName(prex+ ".Requestpubjour");
                fields = clazz.getFields();  //取公共属性
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
            List<Requestpubjour> list = bo.getRequestpubjourList();
            while (resultSet.next()){
                Requestpubjour t = new Requestpubjour();
                refletTrans(t, fields, resultSet);
                list.add(t);
            }
            bo.setRequestpubjourList(list);
        }
        //其他类型依次添加
    }

    /**
     * SQL结果集转入对象
     * @param t 目标对象
     * @param fields 目标对象成员集
     * @param resultSet SQL结果集
     * @throws SQLException
     */
    private void refletTrans(Object t, Field[] fields, ResultSet resultSet) throws SQLException {
        for (Field f : fields ){
            //遍历类成员集塞入SQL结果集
            String name = f.getName();
            String value = resultSet.getString(name);
            try {
                f.set(t, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkConnection() {
        try {
            if (conn == null || !conn.isValid(2000)){
                getOracleConnect();
            }
        } catch (SQLException e) {
            System.out.println("数据库连接断开，正在重连...");
        }
    }
}
