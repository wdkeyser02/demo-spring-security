/**
 *
 */
package com.demo.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
@Data
public class UserLoginDto implements Serializable {

	private static final long serialVersionUID = -7133474062798438886L;
	private String email;
	private String contactNumber;
	@NotNull(message = "{password.not.null}")
	private String password;

	/**
	 * CUSTOMER,DELIVERY_BOY,USER,VENDOR
	 */
	private String userType;

	/**
	 * For Login purpose only : OTP,APP,GOOGLE,FACEBOOK,APPLE
	 */
	private String registeredVia;

	/**
	 * For Email purpose only: Now, it will use consider
	 */
	private boolean isNewUser;
	private Long entityId;
	private Long userId;

	private Long masterCityId;
}
