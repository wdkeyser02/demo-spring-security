/**
 *
 */
package com.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * @author : Demo User
 * @date   : 17-Jun-2020
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class TestController {

	@GetMapping
	public String test() {
		return "Welcome to Demo Application";
	}

}
