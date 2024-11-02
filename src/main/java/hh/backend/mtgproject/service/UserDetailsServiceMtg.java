package hh.backend.mtgproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import hh.backend.mtgproject.domain.AppUser;
import hh.backend.mtgproject.domain.AppUserRepository;

@Service
public class UserDetailsServiceMtg implements UserDetailsService {

    private final AppUserRepository repository;

    @Autowired
    public UserDetailsServiceMtg(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = repository.findByUsername(username);
        UserBuilder builder = null;
        if (user.isPresent()) {
            AppUser currentUser = user.get();
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(currentUser.getPassword());
            builder.roles(currentUser.getRole());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();
    }
}
