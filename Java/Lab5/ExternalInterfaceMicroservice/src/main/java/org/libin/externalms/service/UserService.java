package org.libin.externalms.service;

import lombok.AllArgsConstructor;
import org.libin.dao.dto.user.DtoUser;
import org.libin.dao.dto.util.DtoUtil;
import org.libin.dao.models.Owner;
import org.libin.dao.models.Role;
import org.libin.dao.models.User;
import org.libin.dao.repository.OwnerRepository;
import org.libin.dao.repository.UserRepository;
import org.libin.externalms.errors.EntityNotFoundException;
import org.libin.externalms.security.UserDetailsConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@ComponentScan(basePackages = "org.libin.dao.repository")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder encoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user =userRepository.findByUsername(username);
        return user.map(UserDetailsConfig:: new).orElseThrow(
                () -> new UsernameNotFoundException(String.format("user with username " + username + " is not found"))
        );
    }

    public DtoUser registerUser(DtoUser dto) {
        User user = DtoUtil.castToUser(dto);
        user.setUsername(user.getUsername());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setUserRole(Role.USER);
        User saved = userRepository.save(user);
        return DtoUtil.castToDtoUser(saved);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id= " + id));
        userRepository.deleteById(id);

    }

    public DtoUser changeUserRole(Long userId, String role) {
        Role r = Role.valueOf(role.toUpperCase());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id= " + userId));
        user.setUserRole(r);
        User updated = userRepository.save(user);
        return DtoUtil.castToDtoUser(updated);
    }

    public DtoUser changeOwner(Long userId,Long id){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id= " + userId));
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id= ",id));

        user.setOwner(owner);
        User updated = userRepository.save(user);
        return DtoUtil.castToDtoUser(updated);
    }

}
