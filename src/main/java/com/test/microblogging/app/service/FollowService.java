package com.test.microblogging.app.service;

import java.util.Optional;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.microblogging.app.dto.FollowResponseDTO;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.entity.UserFollowing;
import com.test.microblogging.app.exception.InvalidRequestException;
import com.test.microblogging.app.exception.ResourceNotFoundException;
import com.test.microblogging.app.repository.UserFollowingRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class FollowService {
    
    private final UserFollowingRepository userFollowingRepository;
    private final UserService userService;

    @Autowired
    FollowService(UserFollowingRepository userFollowingRepository, UserService userService) {
        this.userFollowingRepository = userFollowingRepository;
        this.userService = userService;
    }

    /**
     * Seguir un usuario
     * @param followerUsername
     * @param followedUsername
     * @return FollowResponseDTO
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional
    public FollowResponseDTO followUser(String followerUsername, String followedUsername) {
        log.info("Recibe informacion: Follower: {}, Followed: {}", followerUsername, followedUsername);

        // Busca al follower
        // Si no existe lo crea
        User follower = userService.getUserOrAdd(followerUsername);
        if (Objects.isNull(follower)) {
            log.error("Error Interno no se pudo crear el usuario: {}", followerUsername);
            throw new InvalidRequestException("Error Interno no se pudo crear el usuario");
        }

        // Busca al followed
        User followed = userService.findByUsername(followedUsername)
        .orElseThrow(() -> {
            log.error("Usuario no encontrado: Followed: {}", followedUsername);
            return new ResourceNotFoundException("Usuario " + followedUsername + " no encontrado");
        });

        // Verifica si ya sigue al usuario
        // Si ya lo sigue lanza una excepcion
        if (userFollowingRepository.findByFollower_IdAndFollowed_Id(follower.getId(), followed.getId()).isPresent()) {
            throw new ResourceNotFoundException("Ya sigues a este usuario");
        }

        // Crea la relación de seguimiento
        UserFollowing userFollowing = new UserFollowing();
        userFollowing.setFollower(follower);
        userFollowing.setFollowed(followed);
        userFollowingRepository.save(userFollowing);

        log.info("Éxito: El usuario {} ahora sigue a {}", followerUsername, followedUsername);

        // Devuelve la respuesta de éxito
        return new FollowResponseDTO(true, "Followed successfully");
    }

    /**
     * dejar de seguir a un usuario
     * @param followerUsername
     * @param followedUsername
     * @return FollowResponseDTO
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional
    public FollowResponseDTO unfollowUser(String followerUsername, String followedUsername) {
        log.info("Recibe informacion: Follower: {}, Followed: {}", followerUsername, followedUsername);

        // Busca al follower
        // Si no existe lanza una excepcion
        User follower = userService.findByUsername(followerUsername)
        .orElseThrow(() -> {
            log.error("Usuario no encontrado: Follower: {}", followerUsername);
            return new ResourceNotFoundException("Usuario " + followerUsername + " no encontrado");
        });

        // Busca al followed
        // Si no existe lanza una excepcion
        User followed = userService.findByUsername(followedUsername)
        .orElseThrow(() -> {
            log.error("Usuario no encontrado: Followed: {}", followedUsername);
            return new ResourceNotFoundException("Usuario " + followedUsername + " no encontrado");
        });

        // Verifica si ya sigue al usuario
        // Si no lo sigue lanza una excepcion
        UserFollowing userFollowing = userFollowingRepository.findByFollower_IdAndFollowed_Id(follower.getId(), followed.getId())
        .orElseThrow(() -> {
            log.error("No existe la relación de seguimiento: Follower: {}, Followed: {}", followerUsername, followedUsername);
            return new ResourceNotFoundException("No sigues a este usuario");
        });

        // Elimina la relación de seguimiento
        userFollowingRepository.delete(userFollowing);
        
        log.info("Éxito: El usuario {} dejó de seguir a {}", followerUsername, followedUsername);
        // Retorna el DTO de respuesta
        return new FollowResponseDTO(true, "Unfollowed successfully");
    }

    /**
     * Obtener la lista de seguidores de un usuario
     * @param username
     * @return List<User>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional(readOnly = true)
    public List<User> getFollowers(String username) {
        log.info("Recibe informacion: Username: {}", username);

        // Busca al usuario
        // Si no existe lanza una excepcion
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            log.error("Usuario vacio: {}", userOptional.toString());
            throw new ResourceNotFoundException("User not found");
        }

        // Obtiene la lista de seguidores
        // Si no tiene seguidores lanza una excepcion
        User user = userOptional.get();
        List<UserFollowing> userFollowings = userFollowingRepository.findByFollowed_Id(user.getId());

        // Verifica si la lista está vacía
        if (userFollowings.isEmpty()) {
            log.error("El usuario {} no tiene seguidores", username);
            throw new ResourceNotFoundException("El usuario no tiene seguidores");
        }

        // Mapea la lista de seguidores
        return userFollowings.stream().map(uf -> {
            User userResponse = new User();
            userResponse.setUsername(uf.getFollower().getUsername());
            return userResponse;
        }).toList();
    }

    /**
     * Obtener la lista de usuarios seguidos por un usuario
     * @param username
     * @return List<User>
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional(readOnly = true)
    public List<User> getFollowed(String username) {
        log.info("Recibe informacion: Username: {}", username);

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isEmpty()) {
            log.error("Usuario vacio: {}", userOptional.toString());
            throw new ResourceNotFoundException("User not found");
        }

        User user = userOptional.get();
        List<UserFollowing> userFollowings = userFollowingRepository.findByFollower_Id(user.getId());

        // Verifica si la lista está vacía
        if (userFollowings.isEmpty()) {
            log.error("El usuario {} no tiene seguidores", username);
            throw new ResourceNotFoundException("El usuario no tiene seguidores");
        }

        // Mapea la lista de seguidores
        return userFollowings.stream().map(uf -> {
            User userResponse = new User();
            userResponse.setUsername(uf.getFollowed().getUsername());
            return userResponse;
        }).toList();
    }

}
