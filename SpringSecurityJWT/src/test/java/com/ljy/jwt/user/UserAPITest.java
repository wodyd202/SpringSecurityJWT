package com.ljy.jwt.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.jwt.config.token.JwtToken;
import com.ljy.jwt.domain.user.dto.CreateUser;
import com.ljy.jwt.domain.user.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAPITest {
	
	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objMapper;

	@Autowired
	private UserService userService;
	
	@Test
	@Disabled
	public void create() throws Exception{
		CreateUser createUser = new CreateUser("wodyd202@naver.com", "12345");
		mvc.perform(post("/api/v1/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objMapper.writeValueAsString(createUser)))
		.andDo(print())
		.andExpect(status().isCreated());
	}
	
	@Test
	public void loginAfterCreate() throws Exception{
		CreateUser createUser = new CreateUser("wodyd2002@naver.com", "12345");
		userService.create(createUser.toEntity());
		
		mvc.perform(post("/oauth/token")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objMapper.writeValueAsString(createUser))
			)
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void error_403() throws Exception{
		JwtToken token = new JwtToken("403", 0L);
		mvc.perform(get("/api/vi/user/")
			.header("X-AUTH-TOKEN", objMapper.writeValueAsString(token)))
			.andExpect(status().isForbidden());
	}
	
}
