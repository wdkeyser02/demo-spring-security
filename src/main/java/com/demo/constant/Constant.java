
package com.demo.constant;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
public final class Constant {

	private Constant() {
		super();
	}

	public static final String ANONYMOUS_USER = "anonymousUser";
	public static final String SORT_DIRECTION_ASC = "ASC";
	public static final String SORT_DIRECTION_DESC = "DESC";

	public static final String GRANT_TYPE_PASSWORD = "custom_password";
	public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	public static final String CLIENT_ID = "demo-client";
	public static final String SECRET_ID = "demo-secret";

	/**
	 * Messages
	 */
	public static final String LOGIN_SUCCESS = "login.success";
	public static final String UNAUTHORIZED = "unauthorized";
}
