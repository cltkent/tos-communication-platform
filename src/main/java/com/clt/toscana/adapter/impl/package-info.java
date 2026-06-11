/**
 * Concrete implementations of {@link com.clt.toscana.adapter.NotificationSender}.
 *
 * <p>Each class is responsible for a single communication channel:
 * <ul>
 *   <li>{@code TelegramNotificationSender}</li>
 *   <li>{@code EmailNotificationSender}</li>
 *   <li>{@code LineNotificationSender}</li>
 *   <li>{@code WebSocketNotificationSender}</li>
 *   <li>… (extensible via Strategy + Spring {@code @Component} injection)</li>
 * </ul>
 *
 * <p><strong>Architecture rule:</strong> classes in this package must NEVER be
 * referenced directly from {@code core.*}. Always inject via the interface.
 */
package com.clt.toscana.adapter.impl;
