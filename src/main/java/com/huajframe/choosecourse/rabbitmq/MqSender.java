package com.huajframe.choosecourse.rabbitmq;

import com.huajframe.choosecourse.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发布者
 *
 * @author Hua JFarmer
 */
@Service
@Slf4j
public class MqSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendChooseCourseMessage(String seckillMessage){
        rabbitTemplate.convertAndSend(RabbitMQConfig.CHOOSE_EXCHANGE, "choose.course.message", seckillMessage);
        log.info("选课信息以入队: " + seckillMessage);
    }

    public void sendRecordWaitTimeout(Long recordId){
        rabbitTemplate.convertAndSend(RabbitMQConfig.CHOOSE_EXCHANGE, "record.timeout", recordId);
        log.info("课程超时未签到已经入队：" + recordId);
    }
}
