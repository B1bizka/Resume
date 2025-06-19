package org.libin.dto;

import lombok.Getter;
import lombok.Setter;
import org.libin.dao.models.Role;

@Getter
@Setter
public class DtoUser {
    private long id;
    private String username;
    private String password;
    private Role Role;
    private Long ownerId;
}
