package com.test.microblogging.utils;

public class Constantes {

    // Rutas base
    public static final String BASE_API_PATH = "/v1";
    public static final String TWEETS_PATH = BASE_API_PATH + "/tweets";
    public static final String FOLLOWS_PATH = BASE_API_PATH + "/follows";
    public static final String TWEETS_INTERACTION_PATH = BASE_API_PATH + "/interactions";

    // Codigos de respuesta
    public static final String API_CODE_SUCCESS = "200";
    public static final String API_CODE_CREATED = "201";
    public static final String API_CODE_NO_CONTENT = "204";
    public static final String API_CODE_ACCEPTED = "202";
    public static final String API_CODE_NOT_FOUND = "404";
    public static final String API_CODE_BAD_REQUEST = "400";
    public static final String API_CODE_INTERNAL_SERVER_ERROR = "500";
    public static final String API_CODE_UNAUTHORIZED = "401";
    public static final String API_CODE_FORBIDDEN = "403";
    public static final String API_CODE_CONFLICT = "409";

    // Mensajes de error
    public static final String API_MESSAGE_SUCCESS = "Operación exitosa";
    public static final String API_MESSAGE_CREATED = "Recurso creado exitosamente";
    public static final String API_MESSAGE_NO_CONTENT = "No hay contenido";
    public static final String API_MESSAGE_ACCEPTED = "Operación aceptada";
    public static final String API_MESSAGE_NOT_FOUND = "Recurso no encontrado";
    public static final String API_MESSAGE_BAD_REQUEST = "Solicitud incorrecta";
    public static final String API_MESSAGE_INTERNAL_SERVER_ERROR = "Error interno del servidor";
    public static final String API_MESSAGE_UNAUTHORIZED = "No autorizado";
    public static final String API_MESSAGE_FORBIDDEN = "Acceso prohibido";
    public static final String API_MESSAGE_CONFLICT = "Conflicto en la solicitud";

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

    // RabbitMQ
    public static final String RABBITMQ_QUEUE_NAME = "tweetsQueue";
    public static final String RABBITMQ_EXCHANGE_NAME = "tweetsExchange";
    public static final String RABBITMQ_ROUTING_KEY = "tweetsRoutingKey";
    public static final String RABBITMQ_QUEUE_NAME_FOLLOWS = "followsQueue";
    public static final String RABBITMQ_EXCHANGE_NAME_FOLLOWS = "followsExchange";
    public static final String RABBITMQ_ROUTING_KEY_FOLLOWS = "followsRoutingKey";
    public static final String RABBITMQ_QUEUE_NAME_INTERACTIONS = "interactionsQueue";
    public static final String RABBITMQ_EXCHANGE_NAME_INTERACTIONS = "interactionsExchange";
    public static final String RABBITMQ_ROUTING_KEY_INTERACTIONS = "interactionsRoutingKey";

    // Redis
    public static final String TOKEN_CACHE = "tokenCache";
    public static final String PROMISE_CACHE = "promiseCache";

    // Mensajes de excepcion Español
    public static final String EXCEPTION_USER_NOT_FOUND = "El usuario no fue encontrado: ";
    public static final String EXCEPTION_USER_NOT_FOUND_OR_CREATED = "No se pudo crear/obtener el usuario: ";

    private Constantes() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }
    
}
