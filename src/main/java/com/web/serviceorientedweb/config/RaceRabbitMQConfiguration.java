package com.web.serviceorientedweb.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RaceRabbitMQConfiguration {
    @Bean
    public TopicExchange racesExchange() {
        return new TopicExchange("races-exchange");
    }

    @Bean
    public Queue racesQueue() {
        return new Queue("races-queue");
    }

    @Bean
    public Binding racesBinding(Queue racesQueue, TopicExchange racesExchange) {
        return BindingBuilder.bind(racesQueue).to(racesExchange).with("races-routing-key");
    }
}

