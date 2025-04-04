package com.test.microblogging.app.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.test.microblogging.app.dto.TweetResponseDTO;
import com.test.microblogging.app.dto.TweetRequestDTO;
import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.exception.InvalidRequestException;
import com.test.microblogging.app.exception.ResourceNotFoundException;
import com.test.microblogging.app.repository.TweetInteractionRepository;
import com.test.microblogging.app.repository.TweetRepository;
import com.test.microblogging.utils.Constantes;

import lombok.extern.log4j.Log4j2;

/**
 * TweetService.java
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
@Log4j2
@Service
public class TweetService {

    private final TweetRepository tweetRepository;
    private final TweetInteractionRepository tweetInteractionRepository;

    private final UserService userService;
    private final MessagePublisherService messagePublisherService;

    @Autowired
    public TweetService(
        TweetRepository tweetRepository, 
        TweetInteractionRepository tweetInteractionRepository,
        UserService userService,
        MessagePublisherService messagePublisherService
        ) {
        this.tweetRepository = tweetRepository;
        this.tweetInteractionRepository = tweetInteractionRepository;
        this.userService = userService;
        this.messagePublisherService = messagePublisherService;
    }

    /**
     * Publicar un tweet
     * 
     * @param tweetRequestDTO DTO con la información del tweet
     * @throws ResourceNotFoundException si el usuario no existe
     * @throws InvalidRequestException si el tweet no se pudo crear
     * @throws Exception si ocurre un error al publicar el tweet
     * @return TweetResponseDTO con la información del tweet publicado
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional
    //@CacheEvict(value = "myTimeline", key = "#tweetRequestDTO.username")
    public TweetResponseDTO publishTweet(TweetRequestDTO tweetRequestDTO) {
        log.info("Recibe informacion: [{}]", tweetRequestDTO);

        // Busca al usuario
        // Si no existe lo crea
        User user = userService.getUserOrAdd(tweetRequestDTO.getUsername().trim());
        if (Objects.isNull(user)) {
            throw new InvalidRequestException(Constantes.EXCEPTION_USER_NOT_FOUND_OR_CREATED + tweetRequestDTO.getUsername());
        }

        // Busca si ya existe un tweet con esta informacion
        // Si existe lanza una excepcion
        tweetRepository.findByUserUsernameAndContent(tweetRequestDTO.getUsername(), tweetRequestDTO.getContent()).ifPresent(tweet ->
            // Aca podemos Actualizar el contendio aunque si es el mismo no tiene mucho sentido
            new InvalidRequestException("Ya existe un tweet con esta informacion"));


        // Transforma el DTO a entidad
        // y lo guarda en la base de datos
        // Si ocurre un error lo lanza
        Tweet tweet = tweetRequestDTO.toEntity(tweetRequestDTO, user);
        tweet = tweetRepository.save(tweet);

        if (Objects.isNull(tweet)) {
            log.error("Error en Tweet: [{}]", tweetRequestDTO);
            throw new ResourceNotFoundException("No se pudo crear el tweet");
        }

        // Publica el tweet en RabbitMQ
        messagePublisherService.publishTweet(tweet);

        log.info("Éxito: se publico un nuevo Tweet!");
        return new TweetResponseDTO().from(tweet);
    }

    /**
     * Obtener el timeline de un usuario
     * 
     * @param username Nombre de usuario
     * @return List<Tweet> lista de tweets
     * @throws ResourceNotFoundException si el usuario no existe
     * @throws InvalidRequestException si el usuario no tiene tweets
     * @throws Exception si ocurre un error al obtener el timeline
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional(readOnly = true)
    //@Cacheable(value = "myTimeline", key = "#username")
    public List<Tweet> getTimeline(String username, int page, int size) {
        log.info("Recibe informacion: Username: [{}], page: [{}], size: [{}]", username, page, size);
        Pageable pageable = PageRequest.of(page, size);

        // Busca al usuario
        // Si no existe lanza una excepcion
        User user = userService.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException(Constantes.EXCEPTION_USER_NOT_FOUND + username));
        // Obtiene la lista de tweets
        // Si no tiene tweets lanza una excepcion
        List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);
        if (tweets.isEmpty()) {
            throw new ResourceNotFoundException("El usuario: " + username + " no tiene ningun tweet");
        }

        log.info("El usuario: [{}] tiene: [{}] tweets", username, tweets.size());
        return tweets;
    }

    /**
     * Actualizar un tweet
     * 
     * @param tweetId ID del tweet a actualizar
     * @param tweetRequestDTO DTO con la nueva información del tweet
     * @throws ResourceNotFoundException si el tweet no existe
     * @throws InvalidRequestException si el usuario no está autorizado
     * @throws Exception si ocurre un error al actualizar el tweet
     * @return TweetResponseDTO con la información actualizada
     * @author Emmanuel Santiz 
     * @date 2025-04-01
     */
    @Transactional
    public TweetResponseDTO  updatedTweet(Long tweetId, TweetRequestDTO tweetRequestDTO) {
        log.info("Recibe informacion: TweetId: [{}] Comentario: [{}]", tweetId.toString(), tweetRequestDTO.getContent());

        // Busca al tweet
        // Si no existe lanza una excepcion
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("No encontramos el Tweet: " + tweetId.toString()));

        // Busca al usuario con el que se creo el tweet
        // Si el usuario no es el mismo que el que publico el tweet
        if (!tweet.getUser().getUsername().equals(tweetRequestDTO.getUsername())) {
            throw new InvalidRequestException("El usuario no es el mismo que el que publico el tweet");
        }

        // Actualiza el tweet
        tweet.setContent(tweetRequestDTO.getContent());
        tweet = tweetRepository.save(tweet);

        log.info("Éxito: se actualizo el tweet: [{}]", tweetId.toString());
        return new TweetResponseDTO().from(tweet);
    }

    /**
     * Elimina un tweet y todas sus relaciones asociadas (likes, comentarios, etc.)
     * Solo el propietario del tweet puede eliminarlo.
     *
     * @param tweetId ID del tweet a eliminar
     * @param username Nombre de usuario que intenta realizar la eliminación
     * @throws ResourceNotFoundException si el tweet no existe
     * @throws InvalidRequestException si el usuario no está autorizado
     * @throws Exception si ocurre un error al eliminar el tweet
     * @return void
     * @author Emmanuel Santiz
     * @date 2025-04-01
     */
    @Transactional
    //@CacheEvict(value = "tweets", allEntries = true)
    public void deleteTweet(Long tweetId, String username) {
        log.info("Recibe información para eliminar: TweetId: [{}], Username: [{}]", tweetId, username);

        // Buscar el tweet por ID
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResourceNotFoundException("No se eoncontro el Tweet: " + tweetId));

        // Verificar que el usuario sea el propietario del tweet
        if (!tweet.getUser().getUsername().equals(username)) {
            throw new InvalidRequestException("El usuario no es el mismo que el que publico el tweet");
        }

        try {
            // Eliminar todas las interacciones asociadas al tweet
            // Esto incluye likes, comentarios, etc.
            List<TweetInteraction> allInteractions = tweetInteractionRepository.findByTweetId(tweetId);
            if(!allInteractions.isEmpty()) {
                tweetInteractionRepository.deleteAll(allInteractions);
            }

            // Finalmente eliminar el tweet
            tweetRepository.delete(tweet);
            log.info("Tweet eliminado correctamente: [{}]", tweetId);
        } catch (Exception e) {
            log.error("Error al eliminar el tweet [{}]: [{}]", tweetId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * para test
     */
    public Tweet getTweetById(Long id) {
        return tweetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tweet not found"));
    }
    
}
