package org.libin.service;

import lombok.AllArgsConstructor;
import org.libin.config.SecurityUtil;
import org.libin.dto.DtoUser;
import org.libin.dao.models.Owner;
import org.libin.dao.models.Role;
import org.libin.dao.models.User;
import org.libin.config.UserDetailsConfig;
import org.libin.dao.repository.OwnerRepository;
import org.libin.dao.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;



@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder encoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user =userRepository.findByUsername(username);
        return user.map(UserDetailsConfig :: new).orElseThrow(
                () -> new EntityNotFoundException(String.format("user with username " + username + " is not found"))
        );
    }

    public DtoUser registerUser(DtoUser dto) {
        User user = toEntity(dto);
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setUserRole(Role.USER);
        User saved = userRepository.save(user);
        return toDto(saved);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id=" + id));
        if (!SecurityUtil.isAdmin()
                && !(user.getUserId() == (SecurityUtil.ownerId()))) {
            throw new AccessDeniedException("You are not allowed to delete other user");
        }
        userRepository.deleteById(id);


    }

    public DtoUser changeUserRole(Long userId, String role) {
        Role r = Role.valueOf(role.toUpperCase());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id=" + userId));
        user.setUserRole(r);
        User updated = userRepository.save(user);
        return toDto(updated);
    }

    public DtoUser changeOwner(Long userId,Long id){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id=" + userId));
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id=" + id));

        user.setOwner(owner);
        User updated = userRepository.save(user);
        return toDto(updated);
    }

    private User toEntity(DtoUser dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setUserRole(dto.getRole());
        if (dto.getOwnerId() != null) {
            Owner owner = ownerRepository.findById(dto.getOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner not found with id=" + dto.getOwnerId()));
            user.setOwner(owner);
        }else {
            user.setOwner(null);
        }
        return user;
    }

    private DtoUser toDto(User user) {
        DtoUser dto = new DtoUser();
        dto.setId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getUserRole());
        if (user.getOwner() != null) {
            dto.setOwnerId(user.getOwner().getOwnerId());
        }
        return dto;
    }

}
