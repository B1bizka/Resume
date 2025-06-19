package org.libin.dao.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.libin.dao.models.Owner;
import org.libin.dao.models.Role;
import org.libin.dao.models.User;

@Getter
@Setter
@AllArgsConstructor
public class DtoUser {
    private long id;
    private String username;
    private String password;
    private Role Role;
    private Owner ownerId;

    public DtoUser(User user){
        username = user.getUsername();
        password = user.getPassword();
        Role = user.getUserRole();
        ownerId = user.getOwner();
    }
}
