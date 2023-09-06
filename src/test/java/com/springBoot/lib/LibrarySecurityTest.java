package com.springBoot.lib;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LibrarySecurityTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void loginGet() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("loginPage"));
	}

	@Test
	public void accessDeniedPageGet() throws Exception {
		mockMvc.perform(get("/403")).andExpect(status().isOk()).andExpect(view().name("403"));
	}

	@WithMockUser(username = "user", authorities = { "USER" })
	@Test
	public void testAccessWithUserRole() throws Exception {
		mockMvc.perform(get("/books")).andExpect(status().isOk()).andExpect(view().name("books"))
				.andExpect(model().attributeExists("user"));
	}

	@WithMockUser(username = "admin", roles = { "NOT_USER" })
	@Test
	public void testNoAccess() throws Exception {
		mockMvc.perform(get("/books")).andExpect(status().isForbidden());
	}

	@WithMockUser(username = "admin", authorities = { "ADMIN" })
	@Test
	public void testAccessWithAdminRole() throws Exception {
		mockMvc.perform(get("/books/add")).andExpect(status().isOk()).andExpect(view().name("addBook"))
				.andExpect(model().attributeExists("user"));
	}

	@WithMockUser(username = "user", roles = { "USER" })
	@Test
	public void testNoAccessAddBook() throws Exception {
		mockMvc.perform(get("/books/add")).andExpect(status().isForbidden());
	}

	@Test
	void testWrongPassword() throws Exception {
		mockMvc.perform(formLogin("/login").user("username", "user").password("password", "wrongPassword"))
				.andExpect(status().isFound()).andExpect(redirectedUrl("/login?error"));
	}

	@Test
	void testCorrectPassword() throws Exception {
		mockMvc.perform(formLogin("/login").user("username", "user").password("password", "12345678"))
				.andExpect(status().isFound()).andExpect(redirectedUrl("/books"));
	}

}
