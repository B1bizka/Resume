package org.libin.catms.handler;

import org.libin.catms.service.CatService;
import org.libin.dao.dto.cat.DtoCat;
import org.libin.dao.dto.cat.DtoSaveCat;
import org.libin.dao.dto.util.DtoUtil;
import org.springframework.stereotype.Component;

@Component
public class PersistHandler {
    private final CatService catService;
    public PersistHandler(CatService catService) {
        this.catService = catService;
    }
    public void handle(DtoSaveCat dtoSaveCat) {
        catService.save(DtoUtil.castSaveToCat(dtoSaveCat));

    }
}
