/**
 *
 */
package com.demo.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public enum EntityType {

	/**
	 * Here USER There will be 2 types of users included : SUPER ADMIN & ADMIN USERS Here VENDOR there will be 2 types of users included : VENDOR & VENDOR USER
	 */
	SUPER_ADMIN("SUPER_ADMIN");

	String statusValue;

	/**
	 * add admin panel's users in list (do not add super admin)
	 */
	private static final List<String> ADMIN_PANEL_USERS = new ArrayList<>();

	private static final Map<String, EntityType> ENTITY_TYPE_MAP = new HashMap<>();
	static {
		ADMIN_PANEL_USERS.add(SUPER_ADMIN.getStatusValue());
		for (final EntityType entityType : values()) {
			ENTITY_TYPE_MAP.put(entityType.getStatusValue(), entityType);
		}
	}

	public static EntityType getByValue(final String value) {
		return ENTITY_TYPE_MAP.get(value);
	}

}
