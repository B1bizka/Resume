package org.libin;

import org.libin.dao.models.Owner;
import org.libin.service.OwnerService;

public class ControllerOwner {
    public ControllerOwner(OwnerService ownerServices){
        ownerService = ownerServices;
    }
    private OwnerService ownerService;
    public Owner find(int id){
        return ownerService.findOwner(id);
    }
    public void update(Owner owner){
        ownerService.updateOwner(owner);
    }
    public void delete(Owner owner){
        ownerService.deleteOwner(owner);
    }
    public void save(Owner owner){
        ownerService.saveOwner(owner);
    }
}

