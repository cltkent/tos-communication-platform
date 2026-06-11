package com.clt.toscana.domain.entity;

import com.clt.toscana.domain.enums.ChannelType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

/**
 * Stores reusable Thymeleaf-based message templates for each channel.
 *
 * <p>A template is uniquely identified by <strong>{@code (templateCode, channelType)}</strong>,
 * meaning the same logical event (e.g. {@code "ORDER_CONFIRMED"}) can have a
 * rich HTML variant for EMAIL and a compact text variant for TELEGRAM.
 *
 * <p>The dispatcher resolves the correct row via
 * {@code NotificationTemplateRepository.findByTemplateCodeAndChannelType()} and
 * delegates rendering to {@code TemplateService}.
 *
 * <p>Database table: {@code notification_templates}
 */
@Entity
@Table(
    name = "notification_templates",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_template_code_channel",
        columnNames = {"template_code", "channel_type"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Human-readable template identifier, e.g. {@code "ORDER_CONFIRMED"}, {@code "OTP_LOGIN"}.
     * Combined with {@code channelType} to form the unique business key.
     */
    @Column(name = "template_code", nullable = false, length = 100)
    private String templateCode;

    /**
     * Delivery channel this template variant targets.
     * A single {@code templateCode} can have multiple rows, one per channel.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false, length = 50)
    private ChannelType channelType;

    /**
     * Email subject line.
     * Only meaningful when {@code channelType == EMAIL}.
     * Thymeleaf expressions are supported: {@code "Your order [[${orderId}]] is confirmed"}.
     */
    @Column(name = "subject", length = 500)
    private String subject;

    /**
     * Full Thymeleaf template body.
     * Variables from {@link com.clt.toscana.domain.dto.NotificationEventDTO#getContextData()}
     * are injected at render time by {@code TemplateService}.
     *
     * <p>Plain-text example (Telegram):
     * <pre>{@code Hello [[${username}]], your order [[${orderId}]] has been confirmed!}</pre>
     *
     * <p>HTML example (Email):
     * <pre>{@code <p>Hello <strong th:text="${username}"></strong>…</p>}</pre>
     */
    @Column(name = "body_template", nullable = false, columnDefinition = "TEXT")
    private String bodyTemplate;

    /**
     * Soft-delete / activation flag.
     * Inactive templates are skipped by the dispatcher; no exception is thrown.
     */
    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /** Admin-facing description for the template management UI. */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * Channel-specific extra configuration stored as JSONB.
     *
     * <p>Examples:
     * <ul>
     *   <li>Telegram: {@code {"parse_mode": "MarkdownV2"}}</li>
     *   <li>Email:    {@code {"cc": ["admin@example.com"], "replyTo": "no-reply@example.com"}}</li>
     * </ul>
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    /**
     * Optimistic locking version counter.
     * Prevents lost-updates when multiple admins edit the same template concurrently.
     */
    @Version
    @Column(name = "version")
    private Long version;
}
