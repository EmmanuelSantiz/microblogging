package com.test.microblogging.app.repository;

import com.test.microblogging.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Consulta para obtener un usuario por su nombre de usuario
     * @param username
     * @return Optional<User>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    Optional<User> findByUsername(String username);
}
