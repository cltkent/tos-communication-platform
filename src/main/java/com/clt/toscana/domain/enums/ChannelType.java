package com.clt.toscana.domain.enums;

/**
 * Supported notification delivery channels.
 *
 * <p>Add new channels here; the Strategy pattern in {@code adapter.impl}
 * will automatically pick them up once a matching
 * {@link com.clt.toscana.adapter.NotificationSender} bean is registered.
 */
public enum ChannelType {

    /** Real-time in-app delivery via WebSocket. */
    WEBSOCKET,

    /** Telegram Bot API message. */
    TELEGRAM,

    /** LINE Messaging API. */
    LINE,

    /** SMTP / transactional email (rendered via Thymeleaf). */
    EMAIL,

    /** Mobile push notification (FCM / APNs). */
    PUSH
}
