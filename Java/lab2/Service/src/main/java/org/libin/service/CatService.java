package org.libin.service;

import lombok.Getter;
import org.libin.dao.DaoCat;
import org.libin.dao.models.Cat;

@Getter

public class CatService {
    private DaoCat catsDao = new DaoCat();

    public Cat findCat(long id){
        return catsDao.findCatById(id);
    }
    public void saveCat(Cat cat){
        catsDao.saveCat(cat);
    }
    public void updateCat(Cat cat){catsDao.updateCat(cat);}
    public void deleteCat(Cat cat){
        catsDao.deleteCat(cat);
    }
}
