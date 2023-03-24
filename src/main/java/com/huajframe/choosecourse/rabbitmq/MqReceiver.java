package com.huajframe.choosecourse.rabbitmq;

import com.huajframe.choosecourse.config.RabbitMQConfig;
import com.huajframe.choosecourse.service.IRecordService;
import com.huajframe.choosecourse.util.JsonUtil;
import com.huajframe.choosecourse.vo.ChooseCourseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息接收者
 *
 * @author Hua JFarmer
 */
@Service
@Slf4j
public class MqReceiver {
    @Autowired
    private IRecordService recordService;

    @RabbitListener(queues = RabbitMQConfig.CHOOSE_QUEUE)
    public void receive01(String msg) {
        log.info("收到选课消息：" + msg);
        ChooseCourseMessage message = JsonUtil.jsonStr2Object(msg, ChooseCourseMessage.class);
        if(message != null){
            //开始执行选课
            recordService.choose(message.getStudent(), message.getClassesId());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.DLX_QUEUE)
    public void receive02(String msg) {
        log.info("收到选课记录信息：recordId = " + msg);
        if(msg != null){
            //执行取消选课
            Long recordId = Long.valueOf(msg);
            recordService.timeoutNotCheck(recordId);
        }
    }
}
