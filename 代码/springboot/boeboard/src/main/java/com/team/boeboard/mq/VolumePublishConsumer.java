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
//@RabbitListener(queues = {Constants.QUE_VOLUME})
//public class VolumePublishConsumer {
//    private final Logger logger = LoggerFactory.getLogger(VolumePublishConsumer.class);
//
//    @RabbitHandler
//    public void handleMsg(MqMessage msg) {
//        logger.warn("发送音量控制的消息给android：[{}]", msg.stringfy());
//    }
//}
