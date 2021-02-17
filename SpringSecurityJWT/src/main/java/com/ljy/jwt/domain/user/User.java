package com.ljy.jwt.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Column(length = 100, nullable = false, unique = true)
	private String email;

	@Column(length = 300, nullable = false)
	private String password;

	public void encodePassword(String encodePassowrd) {
		this.password = encodePassowrd;
	}
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
