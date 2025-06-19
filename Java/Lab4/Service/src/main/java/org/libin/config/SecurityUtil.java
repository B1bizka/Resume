package org.libin.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static UserDetailsConfig current() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsConfig) auth.getPrincipal();
    }
    public static boolean isAdmin() {
        return current().getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ADMIN"));
    }
    public static Long ownerId() {
        if (!current().getOwnerId().equals(null))
            return current().getOwnerId();
        else return 0L;
    }
}
