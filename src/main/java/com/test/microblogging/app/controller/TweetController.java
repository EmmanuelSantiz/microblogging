package com.test.microblogging.app.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
@Tag(name = "Tweet API", description = "Ejemplo basico para publicar y listar tweets")
public class TweetController {
    
    private final TweetService tweetService;
    
    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Operation(summary = "Publicar Tweet", description = "Crea o publica un nuevo tweet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tweet publicado exitosamente",
                    content = @Content(schema = @Schema(implementation = TweetRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TweetResponseDTO> publishTweet(@RequestBody @Validated TweetRequestDTO tweet) {
        TweetResponseDTO response = tweetService.publishTweet(tweet);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Obtener linea de tiempo", description = "Obtiene la linea de tiempo de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the timeline",
                    content = @Content(schema = @Schema(implementation = Tweet.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/timeline")
    public ResponseEntity<List<Tweet>> getTimeline(@RequestParam String username) {
        List<Tweet> tweets = tweetService.getTimeline(username);
        return ResponseEntity.ok(tweets);
    }
}
