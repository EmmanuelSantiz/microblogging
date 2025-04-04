package com.test.microblogging.app.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.microblogging.app.dto.FollowResponseDTO;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.entity.UserFollowing;
import com.test.microblogging.app.exception.InvalidRequestException;
import com.test.microblogging.app.exception.ResourceNotFoundException;
import com.test.microblogging.app.repository.UserFollowingRepository;
import com.test.microblogging.utils.Constantes;

import lombok.extern.log4j.Log4j2;

/**
 * FollowService.java
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
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
     * 
     * @param followerUsername nombre de usuario que sigue
     * @param followedUsername nombre de usuario seguido
     * @throws ResourceNotFoundException si el usuario no existe
     * @throws InvalidRequestException si el usuario no existe o no se pudo crear
     * @return FollowResponseDTO respuesta con el estado de la operación
     * @throws Exception si ocurre un error al seguir al usuario
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional
    public FollowResponseDTO followUser(String followerUsername, String followedUsername) {
        log.info("Recibe informacion: Follower: [{}], Followed: [{}]", followerUsername, followedUsername);

        // Busca al follower
        // Si no existe lo crea
        User follower = userService.getUserOrAdd(followerUsername);
        if (Objects.isNull(follower)) {
            throw new InvalidRequestException(Constantes.EXCEPTION_USER_NOT_FOUND_OR_CREATED + followerUsername);
        }

        // Busca al followed
        // Si no existe lanza una excepcion
        User followed = userService.findByUsername(followedUsername)
        .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + followedUsername));

        // Verifica si ya sigue al usuario
        // Si ya lo sigue lanza una excepcion
        if (userFollowingRepository.findByFollower_IdAndFollowed_Id(follower.getId(), followed.getId()).isPresent()) {
            throw new ResourceNotFoundException("Descuida: "+ followerUsername + " ya sigues a este usuario: " + followedUsername);
        }

        // Crea la relación de seguimiento
        UserFollowing userFollowing = new UserFollowing();
        userFollowing.setFollower(follower);
        userFollowing.setFollowed(followed);
        userFollowingRepository.save(userFollowing);

        log.info("Éxito: El usuario: [{}] ahora sigue a: [{}]", followerUsername, followedUsername);

        // Devuelve la respuesta de éxito
        return new FollowResponseDTO(true, "Followed successfully");
    }

    /**
     * dejar de seguir a un usuario
     * 
     * @param followerUsername nombre de usuario que sigue
     * @param followedUsername nombre de usuario seguido
     * @throws ResourceNotFoundException si el usuario no existe
     * @throws InvalidRequestException si el usuario no existe o no se pudo crear
     * @throws Exception si ocurre un error al dejar de seguir al usuario
     * @return Void 
     * @author Emmanuel Santiz 
     * @date 2025-04-01
     */
    @Transactional
    /*@Caching(evict = {
        @CacheEvict(value = "myFollowers", key = "#followerUsername"),
        @CacheEvict(value = "myFollowed", key = "#followedUsername")
    })*/
    public void unfollowUser(String followerUsername, String followedUsername) {
        log.info("Recibe informacion: Follower: [{}], Followed: [{}]", followerUsername, followedUsername);

        // Busca al follower
        // Si no existe lanza una excepcion
        User follower = userService.findByUsername(followerUsername)
        .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + followedUsername));

        // Busca al followed
        // Si no existe lanza una excepcion
        User followed = userService.findByUsername(followedUsername)
        .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + followedUsername));

        // Verifica si ya sigue al usuario
        // Si no lo sigue lanza una excepcion
        UserFollowing userFollowing = userFollowingRepository.findByFollower_IdAndFollowed_Id(follower.getId(), followed.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Felicidades: " + followerUsername + " ya no sigues a este usuario: " + followedUsername));

        // Elimina la relación de seguimiento
        userFollowingRepository.delete(userFollowing);
    }

    /**
     * Obtener la lista de seguidores de un usuario
     * 
     * @param username nombre de usuario 
     * @return List<User> respuesta con la lista de seguidores
     * @throws ResourceNotFoundException si el usuario no existe
     * @throws InvalidRequestException si el usuario no tiene seguidores
     * @throws Exception si ocurre un error al obtener la lista de seguidores
     * @author Emmanuel Santiz 
     * @date 2025-04-01
     */
    @Transactional(readOnly = true)
    //@Cacheable(value = "myFollowers", key = "#username")
    public List<User> getFollowers(String username, int page, int size) {
        log.info("Recibe informacion: Username: [{}]", username);
        Pageable pageable = PageRequest.of(page, size);
        // Busca al usuario
        // Si no existe lanza una excepcion
        User user = userService.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + username));

        // Obtiene la lista de seguidores
        // Si no tiene seguidores lanza una excepcion
        List<UserFollowing> userFollowings = userFollowingRepository.findByFollowed_Id(user.getId(), pageable);
        // Verifica si la lista está vacía
        if (userFollowings.isEmpty()) {
            throw new ResourceNotFoundException("El usuario no tiene seguidores");
        }

        log.info("Éxito: El usuario [{}] tiene [{}] seguidores", username, userFollowings.size());

        // Mapea la lista de seguidores
        return userFollowings.stream().map(uf -> {
            User userResponse = new User();
            userResponse.setUsername(uf.getFollower().getUsername());
            return userResponse;
        }).toList();
    }

    /**
     * Obtener la lista de usuarios seguidos por un usuario
     * 
     * @param username nombre de usuario
     * @param page número de página
     * @param size tamaño de la página
     * @throws ResourceNotFoundException si el usuario no existe
     * @throws InvalidRequestException si el usuario no tiene seguidos
     * @throws Exception si ocurre un error al obtener la lista de seguidos
     * @return List<User> respuesta con la lista de seguidos
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional(readOnly = true)
    //@Cacheable(value = "myFollowed", key = "#username")
    public List<User> getFollowed(String username, int page, int size) {
        log.info("Recibe informacion: Username: [{}]", username);
        Pageable pageable = PageRequest.of(page, size);
        // Busca al usuario
        // Si no existe lanza una excepcion
        User user = userService.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + username));

        // Obtiene la lista de usuarios seguidos
        // Si no tiene seguidores lanza una excepcion
        List<UserFollowing> userFollowings = userFollowingRepository.findByFollower_Id(user.getId(), pageable);

        // Verifica si la lista está vacía
        if (userFollowings.isEmpty()) {
            throw new ResourceNotFoundException("El usuario: " + username + " no tiene seguidores");
        }

        log.info("El usuario [{}] tiene [{}] seguidores", username, userFollowings.size());
        // Mapea la lista de seguidores
        return userFollowings.stream().map(uf -> {
            User userResponse = new User();
            userResponse.setUsername(uf.getFollowed().getUsername());
            return userResponse;
        }).toList();
    }

}
