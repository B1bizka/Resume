package org.libin.externalms.producer;

import org.libin.dao.dto.owner.DtoOwner;
import org.libin.dao.dto.owner.DtoSaveOwner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OwnerProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.json.routing.ms.owner.key}")
    private String routingJsonKeyToOwnerMS;

    private final RabbitTemplate rabbitTemplate;

    public OwnerProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOwner(DtoSaveOwner ownerSaveModel){ rabbitTemplate.convertAndSend(exchange, routingJsonKeyToOwnerMS, ownerSaveModel);}
}
