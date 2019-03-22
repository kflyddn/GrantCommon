package cn.pcshao.graduaction;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication(scanBasePackages = {"cn.pcshao.graduaction","cn.pcshao.grant.common", "cn.pcshao.pic", "cn.pcshao.grant.websocket"})
@MapperScan("cn.pcshao.grant.common.dao")
//@EnableDiscoveryClient
@EnableScheduling
//@EnableAsync //线程池异步，已经在commonApplication注解
//@EnableWebSocket  //开启WebSocket，已经在websocketApplication注解
public class BusinessApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }

    public static void main(String[] args) throws IOException {
        //更改properties配置文件名称,避免依赖冲突
        Properties properties = new Properties();
        InputStream in = BusinessApplication.class.getClassLoader().getResourceAsStream("business.properties");
        properties.load(in);
        SpringApplication app = new SpringApplication(BusinessApplication.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }

    /**
     * 定时任务注解会和websocket冲突
     * 在注册一个合适的Scheduler失败时，直接创建了一个NoOpScheduler对象
     * @Scheduled注解会调用这个没有任何意义的Scheduler,那么手动创建一个taskScheduler是不是就行了
     * @return
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        return taskScheduler;
    }

}
