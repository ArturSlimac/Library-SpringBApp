package com.springBoot.lib;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import exceptions.BookNotFoundException;
import exceptions.EmptyDBExeption;

@RestControllerAdvice
class BookErrorAdvice {
	@ResponseBody
	@ExceptionHandler(BookNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String bookNotFoundHandler(BookNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(EmptyDBExeption.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	String dbError(EmptyDBExeption ex) {
		return ex.getMessage();
	}

}
