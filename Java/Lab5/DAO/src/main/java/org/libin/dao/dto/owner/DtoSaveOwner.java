package org.libin.dao.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.libin.dao.models.Cat;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
public class DtoSaveOwner {
    private String name;
    private LocalDate birthday;
}
