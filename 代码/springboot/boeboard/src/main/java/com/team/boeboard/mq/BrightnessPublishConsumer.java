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
//@RabbitListener(queues = {Constants.QUE_BRIGHTNESS})
//public class BrightnessPublishConsumer {
//    private final Logger logger = LoggerFactory.getLogger(BrightnessPublishConsumer.class);
//
//    @RabbitHandler
//    public void handleMsg(MqMessage msg) {
//        logger.warn("发送亮度控制的消息给android：[{}]", msg.stringfy());
//    }
//}
