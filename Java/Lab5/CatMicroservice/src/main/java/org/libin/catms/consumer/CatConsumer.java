package org.libin.catms.consumer;

import lombok.AllArgsConstructor;
import org.libin.catms.handler.PersistHandler;
import org.libin.catms.service.CatService;
import org.libin.dao.dto.cat.DtoCat;
import org.libin.dao.dto.cat.DtoSaveCat;
import org.libin.dao.dto.owner.DtoSaveOwner;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CatConsumer {
    private final CatService catService;
    @RabbitListener(queues = "${rabbitmq.json.queue.name}")
    public void consume(DtoSaveCat saveCat){
        new PersistHandler(catService).handle(saveCat);
    }

}
