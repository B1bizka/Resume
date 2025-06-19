package org.libin.dao.dto;

import lombok.Getter;
import lombok.Setter;
import org.libin.dao.models.Colour;


import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
public class DtoCat {
    private Long id;
    private String name;
    private LocalDate birthday;
    private String breed;
    private Colour color;
    private Long ownerId;
    private Set<Long> friends;
}
