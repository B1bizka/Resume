package org.libin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter

public class DtoOwner {
    private long id;
    private String name;
    private LocalDate birthday;
    private Set<Long> listOfCats;

}
