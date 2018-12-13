package cn.pcshao.graduaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}
