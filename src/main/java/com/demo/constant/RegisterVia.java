/**
 *
 */
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
public enum RegisterVia {

	APP("APP"), WEB("WEB"), GOOGLE("GOOGLE"), APPLE("APPLE"), FACEBOOK("FACEBOOK"), OTP("OTP");

	private String statusValue;

}
