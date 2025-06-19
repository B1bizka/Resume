package org.libin.ownerms.consumer;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.owner.DtoOwner;
import org.libin.dao.dto.owner.DtoSaveOwner;
import org.libin.ownerms.handler.PersistHandler;
import org.libin.ownerms.service.OwnerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OwnerConsumer {
    private final OwnerService ownerService;

    @RabbitListener(queues = "${rabbitmq.json.queue.name}")
    public void consume(DtoSaveOwner saveOwner){
        new PersistHandler(ownerService).handle(saveOwner);
    }
}
