package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Book;
import exceptions.BookNotFoundException;
import exceptions.EmptyDBExeption;
import repository.BookRepository;

public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> findByAuthor(String firstName, String lastName) {
		List<Book> books = null;
		try {

			if (firstName != null && lastName != null)
				books = bookRepository.findByAuteursFirstNameAndAuteursLastName(firstName, lastName);

			if (firstName != null)
				books = bookRepository.findByAuteursFirstName(firstName);

			if (lastName != null)
				books = bookRepository.findByAuteursLastName(lastName);

			if (books != null && !books.isEmpty())
				return books;

		} catch (Exception e) {
			// TODO: handle exception
			throw new EmptyDBExeption(null);
		}
		throw new BookNotFoundException(firstName, lastName);
	}

	public Book findByISBN(String isbn) {
		// TODO Auto-generated method stub
		Book book = null;
		try {
			book = bookRepository.findByIsbn(isbn);
		} catch (Exception e) {
			// TODO: handle exception
			throw new EmptyDBExeption(null);
		}

		if (book == null)
			throw new BookNotFoundException(isbn);
		return book;
	}

	public List<Book> findAll() {
		List<Book> books = null;
		try {
			books = bookRepository.findAll();
		} catch (Exception e) {
			// TODO: handle exception
			throw new EmptyDBExeption(null);
		}
		return books;
	}

}
