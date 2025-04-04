package com.test.microblogging.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.test.microblogging.app.dto.FollowResponseDTO;
import com.test.microblogging.app.entity.User;
import com.test.microblogging.app.service.FollowService;
import com.test.microblogging.utils.Constantes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping(Constantes.FOLLOWS_PATH)
@Tag(name = "Api para seguidores", description = "Api para administras los seguidores de un usuario")
public class FollowController {
    
    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @Operation(summary = "Listar seguidores", description = "Permite a un usuario obtener la lista usuarios que indicaron ser seguidores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS, 
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/followers/{username}")
    public ResponseEntity<List<User>> getFollowers(
        @PathVariable String username,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<User> response = followService.getFollowers(username, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Listar seguidos", description = "Permite a un usuario obtener la lista de usuarios que sigue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS, 
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_NOT_FOUND, description = Constantes.API_MESSAGE_NOT_FOUND),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @GetMapping("/followed/{username}")
    public ResponseEntity<List<User>> getFollowed(
        @PathVariable String username,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<User> response = followService.getFollowed(username, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Seguir", description = "Permite a un usuario seguir a otro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS, 
                    content = @Content(schema = @Schema(implementation = FollowResponseDTO.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @PostMapping("/follow/{followerUsername}/{followedUsername}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FollowResponseDTO> followUser(
        @PathVariable String followerUsername, 
        @PathVariable String followedUsername
    ) {
        FollowResponseDTO response = followService.followUser(followerUsername, followedUsername);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Dejar de seguir", description = "Permite a un usuario dejar de seguir a otro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = Constantes.API_CODE_SUCCESS, description = Constantes.API_MESSAGE_SUCCESS, 
                    content = @Content(schema = @Schema(implementation = FollowResponseDTO.class))),
            @ApiResponse(responseCode = Constantes.API_CODE_BAD_REQUEST, description = Constantes.API_MESSAGE_BAD_REQUEST),
            @ApiResponse(responseCode = Constantes.API_CODE_INTERNAL_SERVER_ERROR, description = Constantes.API_MESSAGE_INTERNAL_SERVER_ERROR)
    })
    @DeleteMapping("/unfollow/{followerUsername}/{followedUsername}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unfollowUser(
        @PathVariable String followerUsername, 
        @PathVariable String followedUsername
    ) {
        followService.unfollowUser(followerUsername, followedUsername);
        return ResponseEntity.noContent().build();
    }
}
