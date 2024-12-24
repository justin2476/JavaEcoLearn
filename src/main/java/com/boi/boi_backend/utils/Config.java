package com.boi.boi_backend.utils;

import com.boi.boi_backend.model.User;
import com.boi.boi_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class Config {

    @Autowired
    private UserRepository userRepository;
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsername(username);

            return new CustomUserDetails(user,null);
        };
    }
}
