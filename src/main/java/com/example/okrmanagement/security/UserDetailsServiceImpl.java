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
        // 先尝试通过uuId查找用户
        User user = userRepository.findByUuId(identifier)
                .orElseGet(() -> 
                    // 如果uuId找不到，尝试通过email查找用户
                    userRepository.findByEmail(identifier)
                            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with identifier: " + identifier))
                );

        return user;
    }
}