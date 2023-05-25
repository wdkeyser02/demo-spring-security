package com.demo.constant;

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
public enum LoginWith {

	CONTACT_NUMBER("CONTACT_NUMBER"), EMAIL("EMAIL");

	String statusValue;

}
