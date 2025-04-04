package com.test.microblogging.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class CacheService {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public CacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Guarda en Redis o devuelve el valor de la cache si ya existe.
     *
     * @param key   La clave para la cache.
     * @param value El valor que se desea almacenar o consultar.
     * @param ttl   Tiempo en segundos para la expiración de la cache (Time-to-Live).
     * @return El valor de la cache o el valor calculado si no existe en Redis.
     */
    public String getOrSave(String key, String value, long ttl) {
        // Verificar si el valor ya está en la cache (Redis)
        String cachedValue = redisTemplate.opsForValue().get(key);

        // Si el valor está en la cache, retornarlo
        if (cachedValue != null) {
            System.out.println("Cache hit: " + key);
            return cachedValue;
        }

        // Si no está en la cache, realizar la operación costosa
        System.out.println("Cache miss: " + key);

        // Aquí deberías realizar la consulta costosa, ejemplo:
        String result = value; // Simulando una consulta

        // Guardar el valor en la cache con un tiempo de expiración (TTL)
        redisTemplate.opsForValue().set(key, result, ttl);

        return result;
    }

    /**
     * Genera una clave única para cada consulta (opcionalmente puedes hacer esto con hashing de parámetros).
     *
     * @param query La consulta que se utilizará para formar la clave de cache.
     * @return La clave generada para la cache.
     */
    public String generateCacheKey(String query) {
        return DigestUtils.md5DigestAsHex(query.getBytes());
    }
}
