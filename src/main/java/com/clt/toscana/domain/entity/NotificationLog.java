package com.clt.toscana.domain.entity;

import com.clt.toscana.domain.enums.ChannelType;
import com.clt.toscana.domain.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

/**
 * Audit trail of every notification delivery attempt.
 *
 * <p>One row is written per {@code (eventId, channel)} pair, capturing the full
 * lifecycle from {@link com.clt.toscana.domain.enums.MessageStatus#RECEIVED RECEIVED}
 * through to {@link com.clt.toscana.domain.enums.MessageStatus#SENT SENT} or
 * {@link com.clt.toscana.domain.enums.MessageStatus#FAILED FAILED}.
 *
 * <p>This table is the <em>primary instrument</em> for:
 * <ul>
 *   <li>Reconciliation — correlate TOS events with actual deliveries via {@code eventId}.</li>
 *   <li>Error investigation — {@code errorMessage} and {@code channelResponse} contain
 *       the full failure context.</li>
 *   <li>Retry analysis — {@code retryCount} tracks how many attempts were made.</li>
 * </ul>
 *
 * <p>Four indexes are created to support the most common admin query patterns
 * (lookup by event, user, status, and creation time).
 *
 * <p>Database table: {@code notification_logs}
 */
@Entity
@Table(
    name = "notification_logs",
    indexes = {
        @Index(name = "idx_nlog_event_id",   columnList = "event_id"),
        @Index(name = "idx_nlog_user_id",    columnList = "user_id"),
        @Index(name = "idx_nlog_status",     columnList = "status"),
        @Index(name = "idx_nlog_created_at", columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mirrors {@link com.clt.toscana.domain.dto.NotificationEventDTO#getEventId()}.
     * This is the <strong>primary correlation / idempotency key</strong>.
     * One event can produce N log rows — one per channel in the fan-out.
     */
    @Column(name = "event_id", nullable = false, length = 100)
    private String eventId;

    /** TOS user identifier of the notification recipient. */
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    /** The specific channel this log row tracks. */
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 50)
    private ChannelType channel;

    /**
     * Current delivery status.
     * Updated in-place on each state transition
     * ({@code RECEIVED → PROCESSING → SENT | FAILED}).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MessageStatus status;

    /**
     * Template code resolved by the dispatcher for this event+channel.
     * {@code null} if the message was sent without a stored template
     * (e.g. raw contextData passed directly).
     */
    @Column(name = "template_code", length = 100)
    private String templateCode;

    /**
     * The fully-rendered, final message body that was handed to the channel adapter.
     * Useful for reproducing exactly what was sent without re-rendering.
     */
    @Column(name = "resolved_message", columnDefinition = "TEXT")
    private String resolvedMessage;

    /**
     * Full error message (and optional stack-trace excerpt) captured when
     * {@code status == FAILED}. {@code null} on successful delivery.
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Number of delivery attempts executed so far.
     * {@code 0} = first (and possibly only) attempt.
     * Incremented by the retry mechanism in {@code NotificationDispatcherService}.
     */
    @Builder.Default
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    /**
     * UTC timestamp when the channel adapter received a positive delivery
     * acknowledgment ({@code status == SENT}).
     * Remains {@code null} until confirmed delivery.
     */
    @Column(name = "sent_at")
    private Instant sentAt;

    /**
     * Snapshot of the original {@code contextData} map from the TOS event.
     * Stored as JSONB so admins can query by template variables directly in SQL.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "context_data", columnDefinition = "jsonb")
    private Map<String, Object> contextData;

    /**
     * Raw response payload returned by the channel provider.
     *
     * <p>Examples:
     * <ul>
     *   <li>Telegram Bot API: {@code {"ok": true, "result": {"message_id": 42}}}</li>
     *   <li>SMTP: {@code {"messageId": "<xxxx@mail.example.com>", "accepted": ["user@example.com"]}}</li>
     *   <li>FCM:  {@code {"multicast_id": 123, "success": 1, "failure": 0}}</li>
     * </ul>
     * Invaluable for debugging provider-side failures (rate limits, invalid tokens, etc.).
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "channel_response", columnDefinition = "jsonb")
    private Map<String, Object> channelResponse;
}
