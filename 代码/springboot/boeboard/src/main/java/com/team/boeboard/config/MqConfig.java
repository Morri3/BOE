package com.team.boeboard.config;

import com.team.boeboard.utils.Constants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//rabbitmq配置类
@Configuration
public class MqConfig {
    //使用基础模式，创建队列
    @Bean
    public Queue queue() {
        return new Queue(Constants.QUE_SCHEME);
    }//发送计划

    @Bean
    public Queue queue0() {
        return new Queue(Constants.QUE_SCHEMEVIDEO);
    }//发送计划

    @Bean
    public Queue queue2() {
        return new Queue(Constants.QUE_SCREENSHOT);
    }//屏幕截屏

    @Bean
    public Queue queue3() {
        return new Queue(Constants.QUE_TIMING);
    }//定时开关机

    @Bean
    public Queue queue4() {
        return new Queue(Constants.QUE_RESTART);
    }//自动重启

    @Bean
    public Queue queue5() {
        return new Queue(Constants.QUE_VOLUME);
    }//音量控制

    @Bean
    public Queue queue6() {
        return new Queue(Constants.QUE_BRIGHTNESS);
    }//亮度控制

    @Bean
    public Queue queue7() {
        return new Queue(Constants.QUE_UPGRADE);
    }//系统升级

    @Bean
    public Queue queue8() {
        return new Queue(Constants.QUE_CLEARCACHE);
    }//清除缓存

    @Bean
    public Queue queue9() {
        return new Queue(Constants.QUE_ANNOUNCE);
    }//公告
}
