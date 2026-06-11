package com.clt.toscana.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Activates Spring Data JPA Auditing so that
 * {@code @CreatedDate}, {@code @LastModifiedDate}, and {@code @CreatedBy}
 * are automatically populated on every
 * {@link com.clt.toscana.domain.entity.BaseEntity} sub-class.
 *
 * <p>To fill {@code createdBy} from the security context, register an
 * {@code AuditorAware<String>} bean once Spring Security is configured:
 *
 * <pre>{@code
 * @Bean
 * public AuditorAware<String> auditorProvider() {
 *     return () -> Optional.ofNullable(SecurityContextHolder.getContext())
 *             .map(SecurityContext::getAuthentication)
 *             .filter(Authentication::isAuthenticated)
 *             .map(Authentication::getName);
 * }
 * }</pre>
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // AuditorAware<String> bean — add after Spring Security is wired in.
}
