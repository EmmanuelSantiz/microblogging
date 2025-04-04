package com.test.microblogging.utils;

public class Constantes {

    // Rutas base
    public static final String BASE_API_PATH = "/v1";
    public static final String TWEETS_PATH = BASE_API_PATH + "/tweets";
    public static final String FOLLOWS_PATH = BASE_API_PATH + "/follows";
    public static final String TWEETS_INTERACTION_PATH = BASE_API_PATH + "/interactions";

    // Limite de caracteres por tweet
    public static final int MAX_CHARACTERES = 280;

    // Configuración de Swagger
    public static final String SWAGGER_API_TITLE = "Microblogging API";
    public static final String SWAGGER_API_DESCRIPTION = "API para un ejemplo de microblogging (Ejercicio tecnico)";
    public static final String SWAGGER_API_VERSION = "1.0.0";

    // Datos del desarrollador
    public static final String DEVELOPER_NAME = "Emmanuel Santiz";
    public static final String DEVELOPER_EMAIL = "emmanuel.07.01@hotmail.com";
    public static final String DEVELOPER_GITHUB = "https://github.com/EmmanuelSantiz/";

    private Constantes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }
    
}
