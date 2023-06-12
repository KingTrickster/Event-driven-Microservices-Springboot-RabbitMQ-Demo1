package com.trxjster.orderservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.order.name}")
    private String orderQueue;

    @Value("${rabbitmq.exchange.name}")
    private String orderExchange;

    @Value("${rabbitmq.binding.routing.key}")
    private String routingKey;

    // Spring bean for queue - order queue
    @Bean
    public Queue orderQueue(){
        return new Queue(orderQueue);
    }

    //Spring bean for exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(orderExchange);
    }

    //Spring bean for binding between exchange and queue using routing queue
    @Bean
    public Binding binding(){
        return BindingBuilder
                .bind(orderQueue())
                .to(exchange())
                .with(routingKey);
    }

    //Configure message converter
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    //Configure RabbitTemplate
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
