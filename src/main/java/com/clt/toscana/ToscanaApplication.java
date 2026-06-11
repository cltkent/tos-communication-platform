package com.clt.toscana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Entry-point for the Toscana Communication Platform.
 *
 * <p>{@link EnableCaching}  – activates Caffeine (L1) / Redis (L2) caching.
 * <p>{@link EnableAsync}    – enables @Async for off-loading blocking JPA calls
 *                             to a dedicated thread-pool inside a WebFlux context.
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class ToscanaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToscanaApplication.class, args);
    }
}
