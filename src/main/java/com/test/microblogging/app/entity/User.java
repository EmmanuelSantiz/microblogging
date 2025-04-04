
package com.test.microblogging.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.CascadeType;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Modelo de la entidad User.
 * Representa un usuario en la aplicación de microblogging.
 * Contiene información sobre el nombre de usuario, el estado (activo/inactivo) y la relación con los tweets y las interacciones realizadas por el usuario.
 * @author Emmanuel Santiz
 * @date 2025-04-01
 */
@Getter
@Setter
@Entity
@Table(name = "app_user", indexes = {
    @Index(name = "idx_user_username", columnList = "username") // Índice en el campo 'username'
}, uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tweet> tweets;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-interactions")
    private List<TweetInteraction> interactions;
}
