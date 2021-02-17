package com.ljy.jwt.domain.user.dto;

import com.ljy.jwt.domain.user.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateUser {

	private String email;
	private String password;

	public User toEntity() {
		return new User(email, password);
	}
}
