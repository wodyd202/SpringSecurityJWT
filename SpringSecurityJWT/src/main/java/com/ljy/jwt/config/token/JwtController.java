package com.ljy.jwt.config.token;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ljy.jwt.domain.user.authentication.UserPrincipal;
import com.ljy.jwt.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("oauth")
@RequiredArgsConstructor
public class JwtController {
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("token")
	public ResponseEntity<JwtToken> oauthToken(@RequestBody LoginInfo loginInfo) {
		UserPrincipal loginUser = (UserPrincipal) userService.loadUserByUsername(loginInfo.getEmail());

		if (!verfyExistUsernamePassword(loginInfo)) {
			final String errorToken = "아이디 혹은 비밀번호를 입력해주세요.";
			return new ResponseEntity<>(new JwtToken(errorToken,0L), HttpStatus.BAD_REQUEST);
		}
		
		if (loginUser != null && passwordEncoder.matches(loginInfo.getPassword(), loginUser.getPassword())) {
			JwtToken createToken = jwtTokenProvider.createToken(loginUser.getUsername(), Arrays.asList("ROLE_ADMIN"));
			return new ResponseEntity<>(createToken, HttpStatus.OK);
		}

		final String errorToken = "아이디 혹은 비밀번호가 일치하지 않습니다.";
		return new ResponseEntity<>(new JwtToken(errorToken,0L), HttpStatus.BAD_REQUEST);
	}

	private boolean verfyExistUsernamePassword(LoginInfo loginInfo) {
		return loginInfo != null && loginInfo.getEmail() != null && loginInfo.getPassword() != null;
	}
}
