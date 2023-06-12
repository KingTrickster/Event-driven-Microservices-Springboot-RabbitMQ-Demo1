package com.trxjster.orderservice.publisher;

import com.trxjster.orderservice.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProducer.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.binding.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.binding.email.routing.key}")
    private String emailRoutingKey;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(OrderEvent orderEvent){
        LOGGER.info(String.format("Order event sent to RabbitMQ => %s", orderEvent));

        //send na order event to order queue
        rabbitTemplate.convertAndSend(exchange, routingKey, orderEvent);

        //send an order event to email queue
        rabbitTemplate.convertAndSend(exchange, emailRoutingKey, orderEvent);
    }
}
