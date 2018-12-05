package cn.pcshao.graduaction;

import com.github.pagehelper.PageHelper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {"cn.pcshao.graduaction","cn.pcshao.grant.common", "cn.pcshao.pic"})
@MapperScan("cn.pcshao.grant.common.dao")
@Configuration("cn.pcshao.graduaction.security.ShiroConfig")
public class BusinessApplication {

    public static void main(String[] args) throws IOException {
        //更改properties配置文件名称,避免依赖冲突
        Properties properties = new Properties();
        InputStream in = BusinessApplication.class.getClassLoader().getResourceAsStream("business.properties");
        properties.load(in);
        SpringApplication app = new SpringApplication(BusinessApplication.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }

    //配置mybatis的分页插件pageHelper
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum","true");   //offset limit == pageNum pageSize
        properties.setProperty("rowBoundsWithCount","true");    //配置pageInfo类或者查询返回总数
        properties.setProperty("reasonable","true");    //传入参数不合法时返回第一或者最后一页结果
        properties.setProperty("dialect","mysql");    //配置mysql数据库的方言
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
