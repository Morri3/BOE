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
//@RabbitListener(queues = {Constants.QUE_TIMING})
//public class TimingPublishConsumer {
//    private final Logger logger = LoggerFactory.getLogger(TimingPublishConsumer.class);
//
//    @RabbitHandler
//    public void handleMsg(MqMessage msg) {
//        logger.warn("发送定时开关机的消息给android：[{}]", msg.stringfy());
//    }
//}
