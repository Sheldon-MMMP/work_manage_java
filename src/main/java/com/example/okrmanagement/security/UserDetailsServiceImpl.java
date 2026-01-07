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
        // 先尝试通过邮箱查找用户
        User user = userRepository.findByEmail(identifier)
                .orElseGet(() -> 
                    // 如果邮箱查找不到，再尝试通过用户名查找用户
                    userRepository.findByUsername(identifier)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with identifier: " + identifier))
                );

        return user;
    }
}