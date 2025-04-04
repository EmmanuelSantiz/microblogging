package com.test.microblogging.app.controller;

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
@Tag(name = "Follow API", description = "Ejemplo basico para seguir y dejar de seguir usuarios")
public class FollowController {
    
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @Operation(summary = "Obtener seguidores", description = "Permite a un usuario obtener la lista de sus seguidores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved followers", content = @Content(schema = @Schema(implementation = FollowResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/followers")
    public ResponseEntity<List<User>> getFollowers(@RequestParam String username) {
        List<User> response = followService.getFollowers(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Obtener seguidos", description = "Permite a un usuario obtener la lista de sus seguidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved followers", content = @Content(schema = @Schema(implementation = FollowResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/followed")
    public ResponseEntity<List<User>> getFollowed(@RequestParam String username) {
        List<User> response = followService.getFollowed(username);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Seguir", description = "Permite a un usuario seguir a otro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully followed user", content = @Content(schema = @Schema(implementation = FollowResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Already following this user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/follow/{followerUsername}/{followedUsername}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FollowResponseDTO> followUser(@PathVariable String followerUsername, @PathVariable String followedUsername) {
        FollowResponseDTO response = followService.followUser(followerUsername, followedUsername);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Dejar de seguir", description = "Permite a un usuario dejar de seguir a otro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully unfollowed user", content = @Content(schema = @Schema(implementation = FollowResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Not following this user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/unfollow/{followerUsername}/{followedUsername}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FollowResponseDTO> unfollowUser(@PathVariable String followerUsername, @PathVariable String followedUsername) {
        FollowResponseDTO response = followService.unfollowUser(followerUsername, followedUsername);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
