package com.demo.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Data
public class LoginResponse implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -9129942395904924485L;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("expires_in")
	private long expiresIn;

	private String scope;

	private Long userId;

	private String name;
	private String firstName;
	private String lastName;

	private String message;

	private int status;

	private Long entityId;

	private String entityType;

	private String email;

	private String contactNumber;

	private Long roleId;

	private String roleName;

	private Boolean canChangePassword;

	private Boolean emailVerified;

	private Boolean contactVerified;

	private boolean profileCompleted;
	/**
	 * This flags are used at the time of vendor and vendor-user login</br>
	 * See more detail at user login controller-> vendor login api.
	 */
	private boolean pendingAdminApproval;

	/**
	 * to get the what will be the next registration step
	 */
	private Long registrationStep;
}
