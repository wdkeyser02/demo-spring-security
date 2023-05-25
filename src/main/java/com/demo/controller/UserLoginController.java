package com.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.constant.Constant;
import com.demo.constant.RegisterVia;
import com.demo.dto.LoginResponse;
import com.demo.dto.UserLoginDto;
import com.demo.exception.NotFoundException;
import com.demo.exception.UnAuthorizationException;
import com.demo.exception.ValidationException;
import com.demo.locale.MessageByLocaleService;
import com.demo.response.handler.GenericResponseHandlers;
import com.demo.service.UserLoginService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class UserLoginController {

	private final UserLoginService userLoginService;

	private final MessageByLocaleService messageByLocaleService;

	/**
	 * ADMIN & ADMIN USER Can Login and generate token
	 *
	 * @param  userLoginDto
	 * @param  result
	 * @return
	 * @throws ValidationException
	 * @throws NotFoundException
	 * @throws UnAuthorizationException
	 */
	@PostMapping("/admin")
	public ResponseEntity<Object> adminLogin(@RequestBody @Valid final UserLoginDto userLoginDto, final BindingResult result)
			throws ValidationException, NotFoundException, UnAuthorizationException {
		final List<FieldError> fieldErrors = result.getFieldErrors();
		if (!fieldErrors.isEmpty()) {
			throw new ValidationException(fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(",")));
		}
		userLoginDto.setRegisteredVia(RegisterVia.APP.getStatusValue());
		LoginResponse loginResponse = userLoginService.adminLogin(userLoginDto);
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(loginResponse)
				.setMessage(messageByLocaleService.getMessage(Constant.LOGIN_SUCCESS, null)).create();
	}

	/**
	 * Get user info based on token
	 *
	 * @param  accessToken
	 * @return
	 * @throws NotFoundException
	 * @throws ValidationException
	 */
	@GetMapping(path = "/basic")
	public ResponseEntity<Object> getUserInfo(@RequestHeader("Authorization") final String accessToken) throws NotFoundException {
		LoginResponse userInfo = userLoginService.getUserInfo();
		return new GenericResponseHandlers.Builder().setStatus(HttpStatus.OK).setData(userInfo)
				.setMessage(messageByLocaleService.getMessage("users.detail.message", null)).create();
	}
}
