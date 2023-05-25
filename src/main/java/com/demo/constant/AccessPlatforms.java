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
public enum AccessPlatforms {

	DELIVERY_BOY("DELIVERY_BOY"), CUSTOMER("CUSTOMER"), VENDOR("VENDOR");

	String statusValue;

	private static final Map<String, AccessPlatforms> SECTION_LIST = new HashMap<>();
	static {
		for (final AccessPlatforms section : values()) {
			SECTION_LIST.put(section.getStatusValue(), section);
		}
	}

	public static AccessPlatforms getByValue(final String value) {
		return SECTION_LIST.get(value);
	}

}
