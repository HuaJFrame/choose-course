package com.huajframe.choosecourse.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * rabbitmq配置类
 *
 * @author Hua JFarmer
 */
@Configuration
public class RabbitMQConfig {

    public static final String CHOOSE_EXCHANGE = "choose_topic";
    public static final String CHOOSE_QUEUE = "choose_queue";
    public static final String CHOOSE_ROUTING_KEY = "choose.#";

    public static final String WAIT_QUEUE = "wait_queue";
    public static final String WAIT_ROUTING_KEY = "*.timeout";

    public static final String DLX_QUEUE = "dlx_queue";
    public static final String DLX_EXCHANGE = "dlx_exchange";
    public static final String DLX_ROUTING_KEY = "record_timeout";

    /**
     * 选课队列，存储学生信息和课程id
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue(CHOOSE_QUEUE);
    }

    /**
     * 选课交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(CHOOSE_EXCHANGE);
    }

    /**
     * 绑定选课队列和选课交换机
     * @param queue
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(CHOOSE_ROUTING_KEY);
    }


    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(DLX_QUEUE, true, false, false);
    }

    /**
     * 死信交换机
     * @return
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE, true, false);
    }

    /**
     * 绑定死信队列和死信交换机
     * @return
     */
    @Bean
    public Binding dlxBinding(DirectExchange dlxExchange, Queue dlxQueue) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(DLX_ROUTING_KEY);
    }

    /**
     * 普通消息队列,没有消费者，过期直接放到死信队列
     * @return
     */
    @Bean
    Queue waitQueue() {
        Map<String, Object> args = new HashMap<>(3);
        //设置消息过期时间，测试时间30s
        args.put("x-message-ttl", 1000 * 30);
        //设置死信交换机
        args.put("x-dead-letter-exchange", DLX_EXCHANGE);
        //设置死信 routing_key
        args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);
        return new Queue(WAIT_QUEUE, true, false, false, args);
    }

    /**
     * 绑定选课交换机和普通消息队列
     * @param topicExchange
     * @param waitQueue
     * @return
     */
    @Bean
    public Binding waitQueueBinding(TopicExchange topicExchange, Queue waitQueue){
        return BindingBuilder.bind(waitQueue).to(topicExchange).with(WAIT_ROUTING_KEY);
    }
}
