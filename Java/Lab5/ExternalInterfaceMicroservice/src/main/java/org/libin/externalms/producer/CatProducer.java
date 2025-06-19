package org.libin.externalms.producer;

import org.libin.dao.dto.cat.DtoSaveCat;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CatProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.json.routing.ms.cat.key}")
    private String routingJsonKeyToCatMS;

    private final RabbitTemplate rabbitTemplate;

    public CatProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendCat(DtoSaveCat catSaveModel){rabbitTemplate.convertAndSend( exchange, routingJsonKeyToCatMS, catSaveModel);}
}
