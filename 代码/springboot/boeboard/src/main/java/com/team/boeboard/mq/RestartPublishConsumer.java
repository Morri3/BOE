//package com.team.boeboard.mq;
//
//import com.team.boeboard.utils.Constants;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RabbitListener(queues = {Constants.QUE_RESTART})
//public class RestartPublishConsumer {
//    private final Logger logger = LoggerFactory.getLogger(RestartPublishConsumer.class);
//
//    @RabbitHandler
//    public void handleMsg(MqMessage msg) {
//        logger.warn("发送自动重启的消息给android：[{}]", msg.stringfy());
//    }
//}
