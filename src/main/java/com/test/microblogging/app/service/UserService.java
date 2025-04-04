package com.test.microblogging.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserOrAdd(String username) {
        return userRepository
                    .findByUsername(username)
                    .orElseGet(() -> addDefUser(username));
    }

    public User addDefUser(String username) {
        log.info("Creamos usuario por defecto: {}", username);

        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setStatus(true);
        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
