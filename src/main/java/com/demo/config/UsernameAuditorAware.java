/**
 *
 */
package com.demo.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
public class UsernameAuditorAware implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
			return Optional.ofNullable(1L);
		}

		return Optional.ofNullable(((UserAwareUserDetails) authentication.getPrincipal()).getUser().getId());
	}
}
