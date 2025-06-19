package org.libin.dao.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.libin.dao.models.Cat;
import org.libin.dao.models.Owner;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor

public class DtoOwner {
    private long id;
    private String name;
    private LocalDate birthday;

    public DtoOwner(Owner owner){
        id = owner.getOwnerId();
        birthday = owner.getOwnerBirthday();
    }

}
