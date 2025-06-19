package org.libin.dao.dto.cat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.libin.dao.models.Cat;
import org.libin.dao.models.Colour;
import org.libin.dao.models.Owner;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
public class DtoSaveCat {
    private String name;
    private LocalDate birthday;
    private String breed;
    private Colour color;
    private Owner owner;

    public DtoSaveCat(Cat cat){
        name = cat.getCatName();
        birthday = cat.getCatBirthday();
        breed = cat.getCatBreed();
        color = cat.getCatColour();
        owner = cat.getOwner();

    }
}
