package com.springBoot.lib;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import domain.Book;
import service.BookService;

@RestController
@RequestMapping(value = "/rest/books")
public class BooksRestController {

	@Autowired
	private BookService bookService;

	@GetMapping(value = "/all")
	public List<Book> getAllBooks() {
		return bookService.findAll();
	}

	@GetMapping(value = "/author")
	public List<Book> getBooksByAuthor(@RequestParam(// value = "firstname",
			required = false, name = "firstname") String firstName, @RequestParam(// value = "lastname",
					required = false, name = "lastname") String lastName) {
		return bookService.findByAuthor(firstName, lastName);
	}

	@GetMapping()
	public Book getBookByISBN(@RequestParam("isbn") String isbn) {
		return bookService.findByISBN(isbn);
	}

}
