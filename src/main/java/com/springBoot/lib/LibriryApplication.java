package com.springBoot.lib;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import service.BookService;
import service.BookServiceImpl;
import validator.AuteursValidator;
import validator.IsbnValidation;
import validator.LocationPlaceValidation;

@EnableJpaRepositories("repository")
@EntityScan("domain")
@SpringBootApplication
public class LibriryApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(LibriryApplication.class, args);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/books");
		registry.addRedirectViewController("/books/", "/books");
		registry.addViewController("/403").setViewName("403");
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("en"));
		return localeResolver;
	}

	@Bean
	IsbnValidation IsbnValidation() {
		return new IsbnValidation();
	}

	@Bean
	LocationPlaceValidation locationPlaceValidation() {
		return new LocationPlaceValidation();
	}

	@Bean
	AuteursValidator auteursValidator() {
		return new AuteursValidator();
	}

	@Bean
	BookService bookService() {
		return new BookServiceImpl();
	}
}
