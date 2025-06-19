package org.libin.ownerms.handler;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.owner.DtoOwner;
import org.libin.dao.dto.owner.DtoSaveOwner;
import org.libin.dao.dto.util.DtoUtil;
import org.libin.ownerms.service.OwnerService;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PersistHandler {
    private final OwnerService ownerService;

    public void handle(DtoSaveOwner saveOwner) {
        ownerService.save(DtoUtil.castSaveToOwner(saveOwner));
    }
}
