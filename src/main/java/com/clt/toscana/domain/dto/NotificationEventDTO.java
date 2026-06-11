package com.clt.toscana.domain.dto;

import com.clt.toscana.domain.enums.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * Raw event payload pushed by TOS to this platform (Kafka / RabbitMQ topic).
 *
 * <p>This DTO is the <em>single contract</em> between the TOS system and the
 * Toscana Communication Platform. It crosses the
 * {@code consumer → core} boundary and carries everything the
 * {@code NotificationDispatcherService} needs to route and render messages.
 *
 * <table border="1">
 *   <caption>Field reference</caption>
 *   <tr><th>Field</th><th>Role</th></tr>
 *   <tr><td>eventId</td><td>Globally unique UUID — primary idempotency key.</td></tr>
 *   <tr><td>userId</td><td>Target recipient identifier within TOS.</td></tr>
 *   <tr><td>channels</td><td>Fan-out targets; dispatcher sends to each channel independently.</td></tr>
 *   <tr><td>contextData</td><td>Template variables resolved by {@code TemplateService} at render time.</td></tr>
 * </table>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventDTO {

    /**
     * Globally unique event ID (UUID v4 recommended).
     * Used as the Redis idempotency key to prevent duplicate deliveries.
     */
    @NotBlank(message = "eventId must not be blank")
    private String eventId;

    /**
     * The TOS user identifier of the notification recipient.
     * Mapped to channel-specific address (e.g. Telegram chat ID, email address)
     * via a user-preference lookup inside the dispatcher.
     */
    @NotBlank(message = "userId must not be blank")
    private String userId;

    /**
     * One or more delivery channels to fan out to.
     * Using {@link Set} guarantees no duplicate channel entries in a single event.
     * Each element results in one independent {@link com.clt.toscana.domain.entity.NotificationLog} row.
     */
    @NotEmpty(message = "At least one channel must be specified")
    private Set<ChannelType> channels;

    /**
     * Arbitrary key/value map provided by TOS.
     * Values are passed to the Thymeleaf template engine to produce the final message body.
     * May be {@code null} if the template requires no dynamic variables.
     *
     * <p>Example:
     * <pre>{@code
     * {
     *   "username":  "Nguyen Van A",
     *   "orderId":   "ORD-20240611-001",
     *   "totalPrice": "1,500,000 VND"
     * }
     * }</pre>
     */
    private Map<String, Object> contextData;
}
