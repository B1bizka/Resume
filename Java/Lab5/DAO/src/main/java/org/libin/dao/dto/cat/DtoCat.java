package org.libin.dao.dto.cat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.libin.dao.models.Cat;
import org.libin.dao.models.Colour;
import org.libin.dao.models.Owner;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class DtoCat {
    private Long id;
    private String name;
    private LocalDate birthday;
    private String breed;
    private Colour color;
    private Long ownerId;

    public DtoCat(Cat cat) {
        id = cat.getCatId();
        name = cat.getCatName();
        birthday = cat.getCatBirthday();
        breed = cat.getCatBreed();
        color = cat.getCatColour();
        ownerId = cat.getOwner().getOwnerId();
    }
}
