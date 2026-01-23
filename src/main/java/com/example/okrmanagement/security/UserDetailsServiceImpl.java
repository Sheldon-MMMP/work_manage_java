package com.example.okrmanagement.security;

import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user;
        
        // Check if identifier is a numeric string (old tokens used userId as subject)
        if (identifier.matches("\\d+")) {
            // Treat as userId
            Long userId = Long.parseLong(identifier);
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + userId));
        } else {
            // Treat as email
            user = userRepository.findByEmail(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + identifier));
        }

        return user;
    }

    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        // 直接通过id查找用户，User实体已实现UserDetails，直接返回即可
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
    }
}