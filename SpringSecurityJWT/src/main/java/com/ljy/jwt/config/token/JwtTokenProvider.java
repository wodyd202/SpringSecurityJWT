package com.ljy.jwt.config.token;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.jwt.domain.user.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final String X_AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";

	@Value("${spring.jwt.secretKey}")
	private String secretKey;

	private long tokenValidTime = 30 * 60 * 1000L;

	private final UserService userService;
	private final ObjectMapper objMapper;

	public JwtToken createToken(String userPk, List<String> roles) {
		Claims claims = Jwts.claims()
							.setSubject(userPk);
		claims.put("roles", roles);
		Date now = new Date();
		Date expireDate = new Date(now.getTime() + tokenValidTime);
		JwtToken token = new JwtToken(
				Jwts.builder()
					.setClaims(claims)
					.setIssuedAt(now)
					.setExpiration(expireDate)
					.signWith(SignatureAlgorithm.HS256, secretKey)
					.compact()
					,expireDate.getTime()
				);
		return token;
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	private String getUserPk(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public JwtToken resolveToken(HttpServletRequest request) {
		String reqJwtToken = request.getHeader(X_AUTH_TOKEN_HEADER);
		try {
			return objMapper.readValue(reqJwtToken, JwtToken.class);
		} catch (Exception e) {
			return null;
		}
	}

	public boolean validToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
								     .setSigningKey(secretKey)
								     .parseClaimsJws(token);
			return !claims.getBody().getExpiration().before(new Date());
		}catch (Exception e) {
			return false;
		}
	}
	
	@PostConstruct
	public void setUp() {
		this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
	}
}
