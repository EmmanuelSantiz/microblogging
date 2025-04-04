package com.test.microblogging.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.test.microblogging.utils.Constantes;

@Configuration
public abstract class RabbitConfig {
    
    // Define una cola llamada "tweetsQueue o el valor en la constante"
    @Bean
    public Queue myQueue() {
        return new Queue(Constantes.RABBITMQ_QUEUE_NAME, true);
    }

    // Define un intercambio tipo TopicExchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(Constantes.RABBITMQ_EXCHANGE_NAME);
    }

    // Bind de la cola a un exchange con una routing key
    @Bean
    public Binding binding(Queue myQueue, TopicExchange exchange) {
        return BindingBuilder.bind(myQueue).to(exchange).with(Constantes.RABBITMQ_ROUTING_KEY);
    }
}
