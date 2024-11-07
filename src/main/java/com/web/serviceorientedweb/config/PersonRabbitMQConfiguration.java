package com.web.serviceorientedweb.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PersonRabbitMQConfiguration {
    @Bean
    public TopicExchange personsExchange() {
        return new TopicExchange("persons-exchange");
    }

    @Bean
    public Queue personsQueue() {
        return new Queue("persons-queue");
    }

    @Bean
    public Binding personBinding(Queue personsQueue, TopicExchange personsExchange) {
        return BindingBuilder.bind(personsQueue).to(personsExchange).with("persons-routing-key");
    }
}
