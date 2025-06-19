package org.libin.dao.dto.user;

import org.libin.dao.models.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoSaveUser {
    private String username;
    private String password;
    private Role Role;
    private Long ownerId;
}
