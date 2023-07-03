package com.homework.springboot.featureNote.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {


    public boolean hasAuthority(String name) {
        Collection<GrantedAuthority> authorities = getUser().getAuthorities();
        List<String> collectAuthority = authorities.stream()
                .map(it -> it.getAuthority())
                .collect(Collectors.toList());
        System.out.println("User = " + name);
        System.out.println("collectAuthority = " + collectAuthority);
        return  authorities
                .stream()
                .map(it -> it.getAuthority())
                .anyMatch((it) -> it.equals(name));
//        for (GrantedAuthority authority : getUser().getAuthorities()) {
//            if (name.equals(authority.getAuthority())) {
//               return true;
//            }
//        }
//        return false;
    }
    public String getUsername() {
        return getUser().getUsername();
    }
    private User getUser() {
        Authentication authentication = getAuthentication();
//        System.out.println("User authentication.getName() = " + authentication.getName());
//        System.out.println("User  authentication.getCredentials() = " + authentication.getCredentials());
//        System.out.println("User  authentication.getPrincipal() = " + authentication.getPrincipal());


        UserDetails userDetails = User.builder()
                .username(authentication.getName())
                .password("")
                .authorities(authentication.getAuthorities())
                .build();

        return (User) userDetails;
    }

    private Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        System.out.println("authentication.getClass() = " + authentication.getClass());

        return authentication;
    }
}
