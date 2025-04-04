package com.test.microblogging.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.test.microblogging.app.dto.TweetRequestDTO;
import com.test.microblogging.app.dto.TweetResponseDTO;
import com.test.microblogging.app.entity.Tweet;
import com.test.microblogging.app.service.TweetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.test.microblogging.utils.Constantes;

import java.util.List;

@RestController
@RequestMapping(Constantes.TWEETS_PATH)
@Tag(name = "API para tweets", description = "Api para administrar tweets, publicar, editar, eliminar y obtener la linea de tiempo")
public class TweetController {
    
    private final TweetService tweetService;
    
    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Operation(summary = "Publicar Tweet", description = "Crea o publica un nuevo tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_CREATED, description = Constantes.API_MESSAGE_SUCCESS,
                    content = @Content(schema = @Schema(implementation = TweetResponseDTO.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TweetResponseDTO> publishTweet(
        @RequestBody @Validated TweetRequestDTO tweet
    ) {
        TweetResponseDTO response = tweetService.publishTweet(tweet);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener linea de tiempo", description = "Obtiene la linea de tiempo de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS,
                    content = @Content(schema = @Schema(implementation = Tweet.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/timeline")
    public ResponseEntity<List<Tweet>> getTimeline(
        @RequestParam String username,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<Tweet> tweets = tweetService.getTimeline(username, page, size);
        return ResponseEntity.ok(tweets);
    }

    @Operation(summary = "Actualizar el tweet", description = "Actualiza el contenido del tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS,
                    content = @Content(schema = @Schema(implementation = TweetResponseDTO.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @PatchMapping("/update/{tweetId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TweetResponseDTO> updateTweet(
        @PathVariable Long tweetId, 
        @RequestBody @Validated TweetRequestDTO tweet
    ) {
        TweetResponseDTO updatedTweet = tweetService.updatedTweet(tweetId, tweet);
        return new ResponseEntity<>(updatedTweet, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar el tweet", description = "Elimina el tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_NO_CONTENT, description = Constantes.API_MESSAGE_NO_CONTENT),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @DeleteMapping("/delete/{tweetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTweet(
        @PathVariable Long tweetId,
        @RequestParam @Validated String username
    ) {
        tweetService.deleteTweet(tweetId, username);
        return ResponseEntity.noContent().build();
    }
}
