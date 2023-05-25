package com.demo.service;

import java.util.Optional;

import com.demo.dto.LoginResponse;
import com.demo.dto.UserLoginDto;
import com.demo.entity.UserLogin;
import com.demo.exception.NotFoundException;
import com.demo.exception.UnAuthorizationException;
import com.demo.exception.ValidationException;

/**
 *
 * @author Demo User
 * @date   14-Apr-2023
 *
 */
public interface UserLoginService {

	Optional<UserLogin> getUserLogin(Long id);

	LoginResponse adminLogin(UserLoginDto userLoginDto) throws ValidationException, NotFoundException, UnAuthorizationException;

	LoginResponse getUserInfo() throws NotFoundException;

}
