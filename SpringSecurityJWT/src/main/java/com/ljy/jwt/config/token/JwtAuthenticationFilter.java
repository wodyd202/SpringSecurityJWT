package com.ljy.jwt.config.token;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

	private final JwtTokenProvider provider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		JwtToken token = provider.resolveToken((HttpServletRequest) request);

		if(checkInvalidJwtTokenData(token)) {
			chain.doFilter(request, response);
			return;
		}
		
		if(token != null && provider.validToken(token.getAccessToken())) {
			Authentication authentication = provider.getAuthentication(token.getAccessToken());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		chain.doFilter(request, response);
	}
	
	private boolean checkInvalidJwtTokenData(JwtToken token) {
		return token == null || token.getAccessToken() == null;
	}
}
