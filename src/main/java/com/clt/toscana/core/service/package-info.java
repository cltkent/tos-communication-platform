/**
 * Core service layer – orchestration and business logic.
 *
 * <p>Key services:
 * <ul>
 *   <li><b>NotificationDispatcher</b> – routes a message to the correct
 *       {@link com.clt.toscana.adapter.NotificationSender} via the Strategy pattern.</li>
 *   <li><b>IdempotencyService</b>    – deduplicates events using Redis.</li>
 *   <li><b>TemplateService</b>       – resolves Thymeleaf templates for rich messages.</li>
 * </ul>
 *
 * <p><strong>Architecture rule:</strong> this package MUST NOT import anything
 * from {@code adapter.impl}. It communicates exclusively through the
 * {@link com.clt.toscana.adapter.NotificationSender} interface.
 */
package com.clt.toscana.core.service;
