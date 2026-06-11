/**
 * Adapter layer – outbound communication ports.
 *
 * <p>Contains the {@link com.clt.toscana.adapter.NotificationSender} interface
 * (Strategy Pattern root) that all channel implementations must implement.
 * The {@code core} layer is only allowed to depend on this interface,
 * never on any class inside {@code adapter.impl}.
 */
package com.clt.toscana.adapter;
