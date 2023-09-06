package com.springBoot.lib;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT username, role FROM users WHERE username = ?").rolePrefix("");
		;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().and().authorizeHttpRequests(requests ->

		requests.requestMatchers(new AntPathRequestMatcher("/rest/**")).permitAll().requestMatchers("/login**")
				.permitAll().requestMatchers("/css/**").permitAll().requestMatchers("/403**").permitAll()
				.requestMatchers("/hideDiv.js").permitAll().requestMatchers("/books/add")
				.access(new WebExpressionAuthorizationManager("hasAuthority('ADMIN')"))
				.requestMatchers("/books", "/popular", "/books/{id}", "/favourites", "removeBookFromFav",
						"/addBookToFav")
				.hasAnyAuthority("USER", "ADMIN"))

				.formLogin(form -> form.defaultSuccessUrl("/books", true).loginPage("/login")
						.usernameParameter("username").passwordParameter("password"))
				.exceptionHandling().accessDeniedPage("/403");

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}