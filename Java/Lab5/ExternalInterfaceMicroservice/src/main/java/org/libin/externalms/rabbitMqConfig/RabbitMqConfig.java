package org.libin.externalms.rabbitMqConfig;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${rabbitmq.cat.json.queue.name}")
    private String catJsonQueue;

    @Value("${rabbitmq.json.routing.ms.cat.key}")
    private String catRoutingJsonKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.owner.json.queue.name}")
    private String ownerJsonQueue;

    @Value("${rabbitmq.json.routing.ms.owner.key}")
    private String ownerRoutingJsonKey;


    @Bean
    public TopicExchange exchange() {return  new TopicExchange(exchange);}

    @Bean
    public Queue ownerMSJsonQueue() {return  new Queue(ownerJsonQueue);}

    @Bean
    public Binding ownerJsonBinding() {
        return BindingBuilder.bind(ownerMSJsonQueue()).to(exchange()).with(ownerRoutingJsonKey);
    }

    @Bean
    public  Queue catMSJsonQueue() {return  new Queue(catJsonQueue);}

    @Bean
    public  Binding catJsonBinding(){
        return  BindingBuilder.bind(catMSJsonQueue()).to(exchange()).with(catRoutingJsonKey);
    }

    @Bean
    public MessageConverter converter() {return  new Jackson2JsonMessageConverter();}

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
