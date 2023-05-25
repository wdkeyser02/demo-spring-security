package com.demo.exception;

import java.util.Locale;

import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.demo.locale.MessageByLocaleService;
import com.demo.response.handler.GenericResponseHandlers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

/**
 *
 * @author : Demo User
 * @date   : 13-Apr-2023
 */
@ControllerAdvice(basePackages = "com.demo")
@AllArgsConstructor
public class ErrorHandlingController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final MessageByLocaleService messageByLocaleService;

	/**
	 * Central exception handler and generate common custom response
	 *
	 * @param  request
	 * @param  exception
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	ResponseEntity<Object> handleControllerException(final HttpServletRequest request, final Throwable exception, final Locale locale) {
		HttpStatus status = null;
		String message = exception.getMessage();
		if (exception instanceof BaseException) {
			status = ((BaseException) exception).getStatus();
			message = exception.getMessage();
		} else if (exception instanceof BaseRuntimeException) {
			status = ((BaseRuntimeException) exception).getStatus();
			message = exception.getMessage();
		} else if (exception instanceof WebApplicationException) {
			status = HttpStatus.valueOf(((WebApplicationException) exception).getResponse().getStatus());
			message = exception.getMessage();
		} else if (exception instanceof AccessDeniedException) {
			status = HttpStatus.UNAUTHORIZED;
			message = exception.getMessage();
		} else if (exception instanceof MissingServletRequestParameterException || exception instanceof MissingRequestHeaderException
				|| exception instanceof HttpMessageNotReadableException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = exception.getMessage();
		} else if (exception instanceof MethodArgumentTypeMismatchException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = "Argument mis matched";
		} else if (exception instanceof ObjectOptimisticLockingFailureException) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = "Something went wrong. Please try again";
		} else {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			message = messageByLocaleService.getMessage("common.error", null);
			StringBuffer requestedURL = request.getRequestURL();
			logger.info("Requested URL:{}", requestedURL);
			logger.error("exception : {}", exception);
		}
		return new GenericResponseHandlers.Builder().setStatus(status).setMessage(message).create();
	}

}
