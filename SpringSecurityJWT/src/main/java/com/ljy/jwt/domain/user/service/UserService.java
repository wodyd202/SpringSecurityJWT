package com.ljy.jwt.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljy.jwt.domain.user.User;
import com.ljy.jwt.domain.user.authentication.UserPrincipal;
import com.ljy.jwt.domain.user.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	public User create(User user) {
		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.encodePassword(encodePassword);
		return userRepository.save(user);
	}
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User findUser = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException(username));
		return new UserPrincipal(findUser);
	}

}
