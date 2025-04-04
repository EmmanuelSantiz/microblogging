package com.test.microblogging.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class RabbitConfig {
    
    // Define una cola llamada "myQueue"
    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", true); // true significa que la cola es durable
    }

    // Define un intercambio tipo TopicExchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("myExchange");
    }

    // Bind de la cola a un exchange con una routing key
    @Bean
    public Binding binding(Queue myQueue, TopicExchange exchange) {
        return BindingBuilder.bind(myQueue).to(exchange).with("myRoutingKey");
    }
}
