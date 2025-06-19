package org.libin.externalms.restController;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.user.DtoUser;
import org.libin.externalms.service.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@ComponentScan(basePackages = "org.libin.externalms.security")
public class UserController {
    private final UserService userService;

    @PostMapping("/new_user")
    public ResponseEntity<DtoUser> registerUser(@RequestBody DtoUser dtoUser) {
        DtoUser created = userService.registerUser(dtoUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}/{role}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoUser> changeUserRole(
            @PathVariable("id") Long id,
            @PathVariable("role") String role
    ){
        DtoUser updated = userService.changeUserRole(id, role);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{userId}/addOwner/{ownerId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DtoUser> changeOwner(
            @PathVariable("userId") Long userId,
            @PathVariable("ownerId") Long ownerId
    ){
        DtoUser updated = userService.changeOwner(userId,ownerId);
        return ResponseEntity.ok(updated);
    }
}
