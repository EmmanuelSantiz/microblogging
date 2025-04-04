package com.test.microblogging.app.controller;

import com.test.microblogging.app.dto.CommentTweetRequestDTO;
import com.test.microblogging.app.entity.TweetInteraction;
import com.test.microblogging.app.service.TweetInteractionService;
import com.test.microblogging.utils.Constantes;

import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Tweet Interactions API", description = "API para administrar tweet comentarios y me gusta")
public class TweetInteractionController {

    private final TweetInteractionService interactionService;

    @Autowired
    public TweetInteractionController(TweetInteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @Operation(summary = "Agregar un comentario a un tweet", description = "Se puede agregar un comentario a un tweet si el usuario que realiza la operacion no existe se crea por default")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment added successfully"),
            @ApiResponse(responseCode = "404", description = "User or tweet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TweetInteraction> addComment(@RequestParam @Validated String username, @RequestBody @Validated CommentTweetRequestDTO commentTweet) {
        TweetInteraction comment = interactionService.addComment(username, commentTweet);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @Operation(summary = "Me gusta un tweet", description = "Se puede agragar un me gusta a un tweet si el usuario que realiza la operacion no existe se crea ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tweet liked successfully"),
            @ApiResponse(responseCode = "400", description = "User already liked this tweet"),
            @ApiResponse(responseCode = "404", description = "User or tweet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/like")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TweetInteraction> likeTweet(@RequestParam String username, @RequestParam Long tweetId) {
        TweetInteraction like = interactionService.likeTweet(username, tweetId);
        return new ResponseEntity<>(like, HttpStatus.CREATED);
    }

    @Operation(summary = "No me gusta un tweet", description = "Se puede remover un me gusta de un tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tweet unliked successfully"),
            @ApiResponse(responseCode = "404", description = "User, tweet, or like not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unlikeTweet(@RequestParam String username, @RequestParam Long tweetId) {
        interactionService.unlikeTweet(username, tweetId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar comentarios de un tweet", description = "Lista todos los comentarios de un tweet en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/comments/{tweetId}")
    public ResponseEntity<List<TweetInteraction>> getComments(@PathVariable Long tweetId) {
        List<TweetInteraction> comments = interactionService.getComments(tweetId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Obtener la cantidad de megustas de un tweet", description = "Regresa el numero de me gustas de un tweet en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Like count retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/likes/count/{tweetId}")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable Long tweetId) {
        long likeCount = interactionService.getLikeCount(tweetId);
        return ResponseEntity.ok(Map.of("likeCount", likeCount));
    }

    @Operation(summary = "Verifica si un usuario le gusta un tweet", description = "Revisa si un usuario le gusta un tweet especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check completed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/likes/check")
    public ResponseEntity<Map<String, Boolean>> hasUserLikedTweet(@RequestParam String username, @RequestParam Long tweetId) {
        boolean hasLiked = interactionService.hasUserLikedTweet(username, tweetId);
        return ResponseEntity.ok(Map.of("hasLiked", hasLiked));
    }
}