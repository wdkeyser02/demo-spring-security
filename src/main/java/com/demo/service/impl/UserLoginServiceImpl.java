package com.demo.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.demo.config.UserAwareUserDetails;
import com.demo.constant.Constant;
import com.demo.constant.EntityType;
import com.demo.constant.LoginWith;
import com.demo.constant.RegisterVia;
import com.demo.dto.LoginRequestDTO;
import com.demo.dto.LoginResponse;
import com.demo.dto.UserLoginDto;
import com.demo.entity.Role;
import com.demo.entity.UserLogin;
import com.demo.exception.BaseRuntimeException;
import com.demo.exception.NotFoundException;
import com.demo.exception.UnAuthorizationException;
import com.demo.exception.ValidationException;
import com.demo.locale.MessageByLocaleService;
import com.demo.repo.RoleRepository;
import com.demo.repo.UserLoginRepository;
import com.demo.service.UserLoginService;
import com.demo.util.CommonUtility;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Demo User
 * @date   14-Apr-20
 *
 */
@Service(value = "userLoginService")
@Transactional(rollbackFor = Throwable.class)
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService, UserDetailsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginServiceImpl.class);

	private final UserLoginRepository userLoginRepository;

	private final RoleRepository roleRepository;

	private final MessageByLocaleService messageByLocaleService;

	@Value("${local.service.url}")
	private String localServiceUrl;

	@Value("${real.sms}")
	private String realSMS;

	@Value("${static.otp}")
	private String staticOTP;

	@Override
	public UserDetails loadUserByUsername(final String username) {
		String actualUser = null;
		String actualUserLoginWith = null;
		String loginWith = null;
		String actualUserWithType = null;
		String requestVia = RegisterVia.APP.getStatusValue();
		String entityType = null;
		if (username != null && username.contains("#")) {
			actualUserWithType = username.split("#")[0];
			requestVia = username.split("#")[1];
		} else {
			actualUserWithType = username;
		}

		/**
		 * Check if the user name contains the role , if not throw error
		 */
		if (actualUserWithType != null && actualUserWithType.contains("!!")) {
			actualUserLoginWith = actualUserWithType.split("!!")[0];
			entityType = actualUserWithType.split("!!")[1];
		} else {
			throw new BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageByLocaleService.getMessage("specify.role", new Object[] {}));
		}

		/**
		 * Check if the user name whether login with email or contact number , if not throw error
		 */
		if (actualUserLoginWith != null && actualUserLoginWith.contains("~")) {
			actualUser = actualUserLoginWith.split("~")[0];
			loginWith = actualUserLoginWith.split("~")[1];
		} else {
			throw new BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageByLocaleService.getMessage("specify.login.with", new Object[] {}));
		}

		Optional<UserLogin> optUserLogin;

		if (RegisterVia.OTP.getStatusValue().equals(requestVia) || LoginWith.CONTACT_NUMBER.getStatusValue().equals(loginWith)) {

			optUserLogin = userLoginRepository.findByContactNumberAndEntityType(actualUser, entityType);
		} else {
			optUserLogin = userLoginRepository.findByEmailAndEntityType(actualUser, entityType);
		}
		/**
		 * If the userType is USERS and optUserLogin is not available, the user might be a superadmin, check if the user is superadmin.
		 */
		if (!optUserLogin.isPresent()) {
			try {
				if (LoginWith.CONTACT_NUMBER.getStatusValue().equals(loginWith)) {
					optUserLogin = userLoginRepository.findByContactNumberAndRole(actualUser, getRoleDetailByName(EntityType.SUPER_ADMIN.getStatusValue()));
				} else {
					optUserLogin = userLoginRepository.findByEmailAndRole(actualUser, getRoleDetailByName(EntityType.SUPER_ADMIN.getStatusValue()));
				}
			} catch (NotFoundException e) {
				LOGGER.error("SUPER_ADMIN role not found");
			}
		} else if (!optUserLogin.isPresent()) {
			throw new BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageByLocaleService.getMessage("invalid.username", null));
		}

		/**
		 * If user is not exists then throw an error
		 */
		if (!optUserLogin.isPresent()) {
			throw new BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageByLocaleService.getMessage("invalid.username", null));
		} else {
			return generateTokenDetails(actualUserWithType, requestVia, optUserLogin.get());
		}
	}

	Role getRoleDetailByName(final String name) throws NotFoundException {
		return roleRepository.findByNameIgnoreCase(name)
				.orElseThrow(() -> new NotFoundException(messageByLocaleService.getMessage("role.not.found.name", new Object[] { name })));
	}

	/**
	 * @param  actualUserWithType
	 * @param  requestVia
	 * @param  userLogin
	 * @return
	 */
	private UserDetails generateTokenDetails(final String actualUserWithType, final String requestVia, final UserLogin userLogin) {
		final String role = userLogin.getRole().getName();
		final SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
		if (RegisterVia.GOOGLE.getStatusValue().equals(requestVia)) {
			return new UserAwareUserDetails(actualUserWithType, userLogin.getGoogleKey(), Arrays.asList(authority), userLogin);
		} else if (RegisterVia.FACEBOOK.getStatusValue().equals(requestVia)) {
			return new UserAwareUserDetails(actualUserWithType, userLogin.getFacebookKey(), Arrays.asList(authority), userLogin);
		} else if (RegisterVia.APPLE.getStatusValue().equals(requestVia)) {
			return new UserAwareUserDetails(actualUserWithType, userLogin.getAppleKey(), Arrays.asList(authority), userLogin);
		} else if (RegisterVia.OTP.getStatusValue().equals(requestVia)) {
			return new UserAwareUserDetails(actualUserWithType, userLogin.getOtp(), Arrays.asList(authority), userLogin);
		} else {
			if (userLogin.getPassword() == null) {
				throw new BaseRuntimeException(HttpStatus.UNAUTHORIZED, messageByLocaleService.getMessage("user.unauthorized.social", null));
			}
			return new UserAwareUserDetails(actualUserWithType + "#" + requestVia, userLogin.getPassword(), Arrays.asList(authority), userLogin);
		}
	}

	@Override
	public Optional<UserLogin> getUserLogin(final Long id) {
		return userLoginRepository.findById(id);
	}

	@Override
	public LoginResponse adminLogin(final UserLoginDto userLoginDto) throws ValidationException, NotFoundException, UnAuthorizationException {
		userNameValidation(userLoginDto);
		Optional<UserLogin> optUserLogin;
		if (userLoginDto.getEmail() != null) {
			optUserLogin = userLoginRepository.getAdminPanelUserBasedOnEmailAndEntityType(userLoginDto.getEmail().toLowerCase(),
					Arrays.asList(EntityType.SUPER_ADMIN.getStatusValue()));
		} else {
			optUserLogin = userLoginRepository.getAdminPanelUserBasedOnContactNumberAndEntityType(userLoginDto.getContactNumber(),
					Arrays.asList(EntityType.SUPER_ADMIN.getStatusValue()));
		}
		if (optUserLogin.isPresent()) {
			userLoginDto.setUserType(EntityType.SUPER_ADMIN.name());
			userLoginDto.setRegisteredVia(userLoginDto.getRegisteredVia());
			return checkUserLogin(userLoginDto);
		} else {
			throw new ValidationException(messageByLocaleService.getMessage("invalid.username", null));
		}
	}

	public LoginResponse checkUserLogin(final UserLoginDto userLoginDto) throws NotFoundException, UnAuthorizationException, ValidationException {
		final LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setGrantType(Constant.GRANT_TYPE_PASSWORD);
		if (userLoginDto.getEmail() != null) {
			loginRequestDTO.setUserName(userLoginDto.getEmail().toLowerCase().concat("~").concat(LoginWith.EMAIL.getStatusValue()).concat("!!")
					.concat(userLoginDto.getUserType()).concat("#").concat(userLoginDto.getRegisteredVia()));
		} else {
			loginRequestDTO.setUserName(userLoginDto.getContactNumber().concat("~").concat(LoginWith.CONTACT_NUMBER.getStatusValue()).concat("!!")
					.concat(userLoginDto.getUserType()).concat("#").concat(userLoginDto.getRegisteredVia()));
		}

		loginRequestDTO.setPassword(userLoginDto.getPassword());
		LoginResponse loginResponse = generateAuthToken(localServiceUrl, loginRequestDTO);

		getUserInfo(loginResponse, userLoginDto);

		/**
		 * Invalidate the OTP once token generated
		 */
		if (RegisterVia.OTP.getStatusValue().equals(userLoginDto.getRegisteredVia())) {
			final UserLogin userLogin = getUserLoginBasedOnEmailOrContactNumberAndEntityType(null, userLoginDto.getContactNumber(), userLoginDto.getUserType())
					.orElseThrow(() -> new NotFoundException(
							messageByLocaleService.getMessage("user.not.found.contact", new Object[] { userLoginDto.getContactNumber() })));
			/**
			 * Generate OTP and save OTP in userLoing table
			 */
			String otp = String.valueOf(CommonUtility.getRandomNumber());
			userLogin.setOtp(CommonUtility.generateBcrypt(otp));
			userLoginRepository.save(userLogin);
		}
		return loginResponse;
	}

	private LoginResponse getUserInfo(final LoginResponse loginResponse, final UserLoginDto userLoginDto) throws NotFoundException {
		Optional<UserLogin> optUserLogin;
		if (!RegisterVia.OTP.getStatusValue().equals(userLoginDto.getRegisteredVia()) && userLoginDto.getEmail() != null) {

			optUserLogin = userLoginRepository.findByEmailAndEntityType(userLoginDto.getEmail().toLowerCase(), userLoginDto.getUserType());
		} else {
			optUserLogin = userLoginRepository.findByContactNumberAndEntityType(userLoginDto.getContactNumber(), userLoginDto.getUserType());
		}
		optUserLogin = setBasicInfo(loginResponse, userLoginDto, optUserLogin);
		/**
		 * if user has password then he can change the password. if user is login with only social media and till the request he hasn't set password then we
		 * will set can change password false
		 */
		if (optUserLogin.isPresent()) {
			loginResponse.setCanChangePassword(optUserLogin.get().getPassword() != null
					|| optUserLogin.get().getFacebookKey() == null && optUserLogin.get().getGoogleKey() == null && optUserLogin.get().getOtp() == null);
		}
		return loginResponse;
	}

	private Optional<UserLogin> setBasicInfo(final LoginResponse loginResponse, final UserLoginDto userLoginDto, Optional<UserLogin> optUserLogin)
			throws NotFoundException {
		if (optUserLogin.isPresent()) {
			BeanUtils.copyProperties(optUserLogin.get(), loginResponse);
			loginResponse.setUserId(optUserLogin.get().getId());
			loginResponse.setRoleId(optUserLogin.get().getRole().getId());
			loginResponse.setRoleName(optUserLogin.get().getRole().getName());
			if (optUserLogin.get().getEntityType() == null) {
				BeanUtils.copyProperties(optUserLogin.get(), loginResponse);
				loginResponse.setUserId(optUserLogin.get().getId());
				loginResponse.setName("Super Admin");
				loginResponse.setEmailVerified(true);
				loginResponse.setContactVerified(true);
				loginResponse.setRoleId(optUserLogin.get().getRole().getId());
				loginResponse.setRoleName(optUserLogin.get().getRole().getName());
			}
		} else {
			if (userLoginDto.getContactNumber() != null) {
				optUserLogin = userLoginRepository.findByContactNumberAndRole(userLoginDto.getContactNumber(),
						getRoleDetailByName(EntityType.SUPER_ADMIN.getStatusValue()));
			} else {
				optUserLogin = userLoginRepository.findByEmailAndRole(userLoginDto.getEmail().toLowerCase(),
						getRoleDetailByName(EntityType.SUPER_ADMIN.getStatusValue()));
			}

			if (optUserLogin.isPresent()) {
				BeanUtils.copyProperties(optUserLogin.get(), loginResponse);
				loginResponse.setUserId(optUserLogin.get().getId());
				loginResponse.setName("Super Admin");
				loginResponse.setEmailVerified(true);
				loginResponse.setContactVerified(true);
				loginResponse.setRoleId(optUserLogin.get().getRole().getId());
				loginResponse.setRoleName(optUserLogin.get().getRole().getName());
			}
		}
		return optUserLogin;
	}

	public Optional<UserLogin> getUserLoginBasedOnEmailOrContactNumberAndEntityType(final String email, final String contactNumber, final String userType)
			throws ValidationException {
		/**
		 * when entity type is user then check is email is exist for super admin or admin users
		 */
		if (EntityType.SUPER_ADMIN.name().equals(userType)) {
			if (CommonUtility.NOT_NULL_NOT_EMPTY_NOT_BLANK_STRING.test(email)) {
				return userLoginRepository.getAdminPanelUserBasedOnEmailAndEntityType(email.toLowerCase(), Arrays.asList(EntityType.SUPER_ADMIN.getStatusValue()));
			} else {
				return userLoginRepository.getAdminPanelUserBasedOnContactNumberAndEntityType(contactNumber, Arrays.asList(EntityType.SUPER_ADMIN.getStatusValue()));
			}
		} /**
			 * when entity type is vendor then check is email is exist for vendor or vendor users
			 */
		else {
			throw new ValidationException(messageByLocaleService.getMessage("invalid.user", null));
		}
	}

	public Optional<UserLogin> getUserLoginBasedOnEmailAndEntityType(final String email, final String entityType) {
		return userLoginRepository.findByEmailAndEntityType(email, entityType);

	}

	public Optional<UserLogin> getUserLoginBasedOnContactNumberAndEntityType(final String contactNumber, final String entityType) {
		return userLoginRepository.findByContactNumberAndEntityType(contactNumber, entityType);
	}

	/**
	 * validation related username for fields either contact number or email
	 *
	 * @param  userLoginDto
	 * @throws ValidationException
	 */
	private void userNameValidation(final UserLoginDto userLoginDto) throws ValidationException {
		if (userLoginDto.getEmail() == null && userLoginDto.getContactNumber() == null) {
			throw new ValidationException(messageByLocaleService.getMessage("username.required", null));
		} else if (userLoginDto.getEmail() != null && userLoginDto.getContactNumber() != null) {
			throw new ValidationException(messageByLocaleService.getMessage("both.username.not.possible", null));
		}
	}

	/**
	 * Internally generate token based on refresh token or userName & password
	 *
	 * @param  url
	 * @param  loginRequestDTO
	 * @return
	 * @throws UnAuthorizationException
	 * @throws NotFoundException
	 */
	private LoginResponse generateAuthToken(final String url, final LoginRequestDTO loginRequestDTO) throws UnAuthorizationException {

		RestTemplate restTemplate = null;
		LoginResponse result = null;
		MultiValueMap<String, String> map = null;
		HttpHeaders headers = null;

		restTemplate = new RestTemplate();

		String plainCreds = Constant.CLIENT_ID + ":" + Constant.SECRET_ID;
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.getEncoder().encode(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Authorization", "Basic " + base64Creds);

		map = new LinkedMultiValueMap<>();
		map.add("grant_type", loginRequestDTO.getGrantType());
		map.add("username", loginRequestDTO.getUserName());
		map.add("password", loginRequestDTO.getPassword());

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		String outhURL = url + "oauth2/token";
		ResponseEntity<LoginResponse> response = restTemplate.postForEntity(outhURL, request, LoginResponse.class);
		result = response.getBody();
		if (result.getStatus() == HttpStatus.UNAUTHORIZED.value()) {
			throw new UnAuthorizationException(result.getMessage());
		} else {
			result.setMessage("login successfully");
			result.setStatus(200);
		}
		return result;
	}

	@Override
	public LoginResponse getUserInfo() throws NotFoundException {
		LoginResponse loginResponse = new LoginResponse();
		UserLogin userLogin = ((UserAwareUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Optional<UserLogin> optUserLogin = Optional.ofNullable(userLogin);
		optUserLogin = setBasicInfo(loginResponse, new UserLoginDto(), optUserLogin);
		/**
		 * if user has password then he can change the password. if user is login with only social media and till the request he hasn't set password then we
		 * will set can change password false
		 */
		if (optUserLogin.isPresent()) {
			loginResponse.setCanChangePassword(optUserLogin.get().getPassword() != null
					|| optUserLogin.get().getFacebookKey() == null && optUserLogin.get().getGoogleKey() == null && optUserLogin.get().getOtp() == null);
		}
		return loginResponse;

	}

}
