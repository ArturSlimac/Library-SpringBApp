package service;

import java.util.List;

import domain.Book;

public interface BookService {
	public List<Book> findByAuthor(String firstName, String lastName);

	public Book findByISBN(String isbn);

	public List<Book> findAll();
}
