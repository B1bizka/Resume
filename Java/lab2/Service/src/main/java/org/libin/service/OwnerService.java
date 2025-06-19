package org.libin.service;

import lombok.Getter;
import org.libin.dao.DaoOwner;
import org.libin.dao.models.Owner;

@Getter

public class OwnerService {
    private DaoOwner ownerDao;

    public OwnerService(DaoOwner ownerDao) {
        this.ownerDao = ownerDao;
    }
    public OwnerService(){
        this.ownerDao = new DaoOwner();
    }
    public Owner findOwner(long id){
        return ownerDao.findOwnerById(id);
    }
    public void saveOwner(Owner owner){
        ownerDao.saveOwner(owner);
    }
    public void updateOwner(Owner owner){
        ownerDao.updateOwner(owner);
    }
    public void deleteOwner(Owner owner){
        ownerDao.deleteOwner(owner);
    }

}