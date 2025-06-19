package org.libin.dao.dto.util;

import org.libin.dao.dto.cat.DtoCat;
import org.libin.dao.dto.cat.DtoListCat;
import org.libin.dao.dto.cat.DtoSaveCat;
import org.libin.dao.dto.owner.DtoListOwner;
import org.libin.dao.dto.owner.DtoOwner;
import org.libin.dao.dto.owner.DtoSaveOwner;
import org.libin.dao.dto.user.DtoUser;
import org.libin.dao.models.Cat;
import org.libin.dao.models.Owner;
import org.libin.dao.models.Role;
import org.libin.dao.models.User;

import java.util.ArrayList;
import java.util.List;

public class DtoUtil {
    // to dto lists
    public static DtoListCat castToDtoCatList(List<Cat> cats){
        List<DtoCat> dtoCats = new ArrayList<>();
        for(Cat cat : cats){dtoCats.add(new DtoCat(cat));}
        return new DtoListCat(dtoCats);
    }
    public static DtoListOwner castToDtoOwnerList(List<Owner> owners){
        List<DtoOwner> dtoOwners = new ArrayList<>();
        for (Owner owner : owners) { dtoOwners.add(new DtoOwner(owner));}
        return  new DtoListOwner(dtoOwners);
    }

    // entity to dto and vise versa
    public static DtoOwner castToDtoOwner(Owner owner){
        return new DtoOwner(owner);
    }
    public static DtoCat castToDtoCat(Cat cat){
        return new DtoCat(cat);
    }


    public static Cat castToCat (DtoCat dtoCat){
        return new Cat(dtoCat.getName(), dtoCat.getBreed(),
                dtoCat.getColor() , dtoCat.getBirthday());
    }

    public static Owner castToOwner (DtoOwner owner){
        return new Owner(owner.getName(), owner.getBirthday() );
    }


    // save models
    public static Cat castSaveToCat(DtoSaveCat dtoSaveCat){
        return new Cat(dtoSaveCat.getName(),dtoSaveCat.getBreed(),dtoSaveCat.getColor(),
                dtoSaveCat.getBirthday(), dtoSaveCat.getOwner());
    }

    public static Owner castSaveToOwner (DtoSaveOwner dtoSaveOwner){
        return new Owner(dtoSaveOwner.getName(), dtoSaveOwner.getBirthday() );
    }






    // user
    public static DtoUser castToDtoUser(User user){
        return new DtoUser(user);
    }

    public static User castToUser(DtoUser dtoUser){
        User user = new  User();
        user.setUsername(dtoUser.getUsername());
        user.setPassword(dtoUser.getPassword());
        user.setUserRole(dtoUser.getRole());
        user.setOwner(dtoUser.getOwnerId());
        return user;

    }

}
