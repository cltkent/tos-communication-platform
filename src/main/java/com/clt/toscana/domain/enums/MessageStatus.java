package com.clt.toscana.domain.enums;

/**
 * Lifecycle status of a single notification delivery attempt.
 *
 * <pre>
 * RECEIVED ──► PROCESSING ──► SENT
 *                          └──► FAILED
 * </pre>
 *
 * <p>Each transition is persisted as a new / updated row in
 * {@link com.clt.toscana.domain.entity.NotificationLog}.
 */
public enum MessageStatus {

    /**
     * Event received from TOS and persisted to the log.
     * Idempotency check has passed; not yet dispatched.
     */
    RECEIVED,

    /**
     * Dispatcher has picked up the event and is invoking
     * the channel {@link com.clt.toscana.adapter.NotificationSender}.
     */
    PROCESSING,

    /** Channel adapter confirmed successful delivery. */
    SENT,

    /** Delivery failed after all configured retry attempts. */
    FAILED
}
