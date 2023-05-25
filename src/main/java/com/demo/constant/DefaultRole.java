/**
 *
 */
package com.demo.constant;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Getter
@AllArgsConstructor
public enum DefaultRole {

	SUPER_ADMIN("SUPER_ADMIN");

	String statusValue;

	private static final Map<String, DefaultRole> DEFAULT_ROLE_MAP = new HashMap<>();
	static {
		for (final DefaultRole entityType : values()) {
			DEFAULT_ROLE_MAP.put(entityType.getStatusValue(), entityType);
		}
	}

	public static DefaultRole getByValue(final String value) {
		return DEFAULT_ROLE_MAP.get(value);
	}

}
