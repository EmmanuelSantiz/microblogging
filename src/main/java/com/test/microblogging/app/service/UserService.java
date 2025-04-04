package com.test.microblogging.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.exception.InvalidRequestException;
import com.test.microblogging.app.exception.ResourceNotFoundException;
import com.test.microblogging.app.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

/**
 * UserService.java
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
@Log4j2
@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtener un usuario por su nombre de usuario o crear uno por defecto
     * 
     * @param username nombre de usuario
     * @throws InvalidRequestException si el nombre de usuario es nulo o vacío
     * @throws ResourceNotFoundException si el usuario no se encuentra
     * @return User Usuario encontrado o creado
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    public User getUserOrAdd(String username) {
        // Busca al usuario
        // Si no existe lo crea
        return userRepository
                    .findByUsername(username)
                    .orElseGet(() -> addDefUser(username));
    }

    /**
     * Crear un usuario por defecto
     * 
     * @param username nombre de usuario
     * @throws InvalidRequestException si el nombre de usuario es nulo o vacío
     * @throws ResourceNotFoundException si el usuario no se encuentra
     * @throws InvalidRequestException si el nombre de usuario es nulo o vacío
     * @return User Usuario creado
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    public User addDefUser(String username) {
        log.info("Creamos usuario por defecto con Username: [{}]", username);

        // Creamos el usuario por defecto
        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setStatus(true);
        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        log.info("Recibe informacion: [{}]", username);
        // Busca al usuario
        return userRepository.findByUsername(username);
    }

}
