package cn.pcshao.pic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {"cn.pcshao.pic", "cn.pcshao.grant.common"})
public class AlbumApplication {

    public static void main(String[] args) throws IOException {
        //更改properties配置文件名称,避免依赖冲突
        Properties properties = new Properties();
        InputStream in = AlbumApplication.class.getClassLoader().getResourceAsStream("album.properties");
        properties.load(in);
        SpringApplication app = new SpringApplication(AlbumApplication.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }

}
