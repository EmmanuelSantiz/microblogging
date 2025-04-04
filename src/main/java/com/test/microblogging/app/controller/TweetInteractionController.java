
package com.test.microblogging.app.controller;

import com.test.microblogging.app.dto.TweetRequestDTO;
import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.app.service.TweetInteractionService;
import com.test.microblogging.utils.Constantes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constantes.TWEETS_INTERACTION_PATH)
@Tag(name = "Api para interactuar con los tweets", description = "API para administrar tweet, agregar comentarios y me gusta, quitar comentarios, y me gusta")
public class TweetInteractionController {

    private final TweetInteractionService interactionService;

    @Autowired
    public TweetInteractionController(TweetInteractionService interactionService) {
        this.interactionService = interactionService;
    }
    @Operation(summary = "Agregar un comentario a un tweet", description = "Se puede agregar un comentario (Tweet) a un tweet si el usuario que realiza la operacion no existe se crea por default")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_CREATED, description = Constantes.API_MESSAGE_CREATED, 
                    content = @Content(schema = @Schema(implementation = TweetInteraction.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @PostMapping("/comment/{tweetId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TweetInteraction> addComment(
        @PathVariable Long tweetId,
        @RequestBody @Validated TweetRequestDTO tweetRequestDTO
    ) {
        TweetInteraction comment = interactionService.addComment(tweetId, tweetRequestDTO);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @Operation(summary = "Indicar que me gusta un tweet", description = "Se puede agragar un me gusta (like) a un tweet especifico, si el usuario que realiza la operacion no existe se crea por default")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_CREATED, description = Constantes.API_MESSAGE_CREATED),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @PostMapping("/like/{tweetId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TweetInteraction> likeTweet(
        @RequestParam String username, 
        @PathVariable Long tweetId) {
        TweetInteraction like = interactionService.likeTweet(username, tweetId);
        return new ResponseEntity<>(like, HttpStatus.CREATED);
    }

    @Operation(summary = "Inidcar que ya no me gusta un tweet", description = "Se puede remover el me gusta (like) de un tweet en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_NO_CONTENT, description = Constantes.API_MESSAGE_NO_CONTENT),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @DeleteMapping("/like/{tweetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unlikeTweet(
        @RequestParam String username, 
        @PathVariable Long tweetId
    ) {
        interactionService.unlikeTweet(username, tweetId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar comentarios de un tweet", description = "Lista todos los comentarios relacionados de un tweet en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/comments/{tweetId}")
    public ResponseEntity<List<TweetInteraction>> getComments(
        @PathVariable Long tweetId
    ) {
        List<TweetInteraction> comments = interactionService.getComments(tweetId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Obtener la cantidad de me gustas (likes) de un tweet", description = "Regresa el numero entero de me gustas (likes) de un tweet en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/likes/count/{tweetId}")
    public ResponseEntity<Map<String, Long>> getLikeCount(
        @PathVariable Long tweetId
    ) {
        long likeCount = interactionService.getLikeCount(tweetId);
        return ResponseEntity.ok(Map.of("likeCount", likeCount));
    }

    @Operation(summary = "Verifica si un usuario ya indico que le gusta un Tweet", description = "Revisa y regresa un bandera boleana si un usuario ya indico que le gusta un tweet especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/likes/check")
    public ResponseEntity<Map<String, Boolean>> hasUserLikedTweet(@RequestParam String username, @RequestParam Long tweetId) {
        boolean hasLiked = interactionService.hasUserLikedTweet(username, tweetId);
        return ResponseEntity.ok(Map.of("hasLiked", hasLiked));
    }
}
