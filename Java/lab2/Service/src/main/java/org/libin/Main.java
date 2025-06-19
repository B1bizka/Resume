package org.libin;

import org.libin.dao.models.Cat;
import org.libin.dao.models.Colour;
import org.libin.dao.models.Owner;
import org.libin.service.CatService;
import org.libin.service.OwnerService;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        OwnerService ownerService = new OwnerService();
        CatService catsService = new CatService();
        LocalDate bd1 = LocalDate.of(1990, 5, 20);
        LocalDate bd2 = LocalDate.of(2010, 8, 24);
        LocalDate bd3 = LocalDate.of(2020, 3, 18);

        Owner owner = new Owner("Bibizka", bd1);
        Cat cat = new Cat("TTecuK","4epNaua", Colour.WHITE,bd2);
        Cat cat1 = new Cat("CTyJIoCKpe6","6pogdArA",Colour.RED,bd2);

        ownerService.saveOwner(owner);
        catsService.saveCat(cat);
        catsService.saveCat(cat1);

        owner.tameTheBeast(cat);
        owner.tameTheBeast(cat1);
        cat1.befriend(cat);
        owner.getRidOfTheBeast(cat1);

        ownerService.updateOwner(owner);
        catsService.updateCat(cat);
        catsService.updateCat(cat1);



    }
}