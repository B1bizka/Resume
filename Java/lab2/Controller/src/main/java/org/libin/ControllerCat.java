package org.libin;

import org.libin.dao.models.Cat;
import org.libin.service.CatService;

public class ControllerCat {
    public ControllerCat(CatService catsServices){
        catsService= catsServices;
    }
    private CatService catsService;
    public Cat find(int id){
        return catsService.findCat(id);
    }
    public void update(Cat cat){
        catsService.updateCat(cat);
    }
    public void delete(Cat cat){
        catsService.deleteCat(cat);
    }
    public void save(Cat cat){
        catsService.saveCat(cat);
    }

}
