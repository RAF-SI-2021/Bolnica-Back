package raf.si.bolnica.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import raf.si.bolnica.gateway.feign.UserInterface;
import raf.si.bolnica.gateway.responses.RoleResponseDTO;
import raf.si.bolnica.gateway.responses.UserResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserInterface userInterface;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponseDTO userResponseDTO = userInterface.fetchUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username:" + username + " not found"));

        List<GrantedAuthority> grantedAuthorities = userResponseDTO.getRoles()
                .stream().map(RoleResponseDTO::getName).map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(String.join("-", username, userResponseDTO.getEmail()),
                userResponseDTO.getPassword(), true, true, true,
                true, grantedAuthorities);
    }
}