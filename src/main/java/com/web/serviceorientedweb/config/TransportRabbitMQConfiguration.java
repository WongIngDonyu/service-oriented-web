package com.web.serviceorientedweb.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransportRabbitMQConfiguration {
    @Bean
    public TopicExchange transportsExchange() {
        return new TopicExchange("transports-exchange");
    }

    @Bean
    public Queue transportsQueue() {
        return new Queue("transports-queue");
    }

    @Bean
    public Binding transportsBinding(Queue transportsQueue, TopicExchange transportsExchange) {
        return BindingBuilder.bind(transportsQueue).to(transportsExchange).with("transports-routing-key");
    }
}

