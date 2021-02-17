package com.ljy.jwt.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ljy.jwt.domain.user.dto.CreateUser;
import com.ljy.jwt.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
public class UserAPI {
	private final UserService userService;

	@PostMapping
	public ResponseEntity<CreateUser> create(@RequestBody CreateUser createUser) {
		userService.create(createUser.toEntity());
		return new ResponseEntity<>(createUser, HttpStatus.CREATED);
	}
	
	
}
