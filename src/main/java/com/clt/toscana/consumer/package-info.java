/**
 * Spring Cloud Stream consumers.
 *
 * <p>Each consumer listens on a Kafka or RabbitMQ binding and delegates
 * processing to the appropriate {@code core/service} class.
 * Consumers must remain thin – no business logic lives here.
 */
package com.clt.toscana.consumer;
