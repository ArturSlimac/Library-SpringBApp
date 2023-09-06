package com.springBoot.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import domain.Auteur;
import domain.Book;
import domain.LocationPlace;
import domain.User;
import jakarta.transaction.Transactional;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.UserRepository;

@Component
public class Runner implements CommandLineRunner {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		// Authors
		Auteur author1 = new Auteur("John", "Smith");
		Auteur author2 = new Auteur("Jane", "Doe");
		Auteur author3 = new Auteur("Robert", "Johnson");
		Auteur author4 = new Auteur("Sarah", "Lee");
		Auteur author5 = new Auteur("Michael", "Brown");
		Auteur author6 = new Auteur("Emily", "Garcia");
		Auteur author7 = new Auteur("David", "Wilson");
		Auteur author8 = new Auteur("Samantha", "Davis");
		Auteur author9 = new Auteur("Jessica", "Anderson");
		Auteur author10 = new Auteur("William", "Jackson");

		// Locations
		LocationPlace location1 = new LocationPlace(50, 100, "Main Shelf");
		LocationPlace location2 = new LocationPlace(100, 150, "Children's Section");
		LocationPlace location3 = new LocationPlace(150, 200, "Fiction Section");
		LocationPlace location4 = new LocationPlace(200, 250, "New Releases");
		LocationPlace location5 = new LocationPlace(250, 300, "Main Shelf");
		LocationPlace location6 = new LocationPlace(50, 150, "Biography Section");
		LocationPlace location7 = new LocationPlace(100, 200, "Classics Section");
		LocationPlace location8 = new LocationPlace(150, 250, "Science Fiction Section");
		LocationPlace location9 = new LocationPlace(200, 300, "Non-Fiction Section");
		LocationPlace location10 = new LocationPlace(50, 200, "Main Shelf");
		LocationPlace location11 = new LocationPlace(100, 250, "Science Section");
		LocationPlace location12 = new LocationPlace(150, 300, "Classics Section");
		LocationPlace location13 = new LocationPlace(50, 250, "Fiction Section");
		LocationPlace location14 = new LocationPlace(100, 300, "Mystery Section");
		LocationPlace location15 = new LocationPlace(50, 150, "Main Shelf");
		LocationPlace location16 = new LocationPlace(100, 200, "History Section");
		LocationPlace location17 = new LocationPlace(150, 250, "History Section");

//		// Save authors to database
		authorRepository.save(author1);
		authorRepository.save(author2);
		authorRepository.save(author3);
		authorRepository.save(author4);
		authorRepository.save(author5);
		authorRepository.save(author6);
		authorRepository.save(author7);
		authorRepository.save(author8);
		authorRepository.save(author9);
		authorRepository.save(author10);

		// Create books with authors
		Book book1 = new Book("The Secret Garden", "9780143106456", 10.99);
		book1.addAuteur(author1);
		book1.addAuteur(author2);
		book1.addAuteur(author3);

		Book book2 = new Book("Pride and Prejudice", "9780486284736", 12.50);
		book2.addAuteur(author2);

		Book book3 = new Book("The Catcher in the Rye", "9780316769174", 9.99);
		book3.addAuteur(author3);
		book3.addAuteur(author10);
		book3.addAuteur(author7);
		book3.addAuteur(author5);

		Book book4 = new Book("To Kill a Mockingbird", "9780446310789", 8.99);
		book4.addAuteur(author4);

		Book book5 = new Book("Harry Potter and the Philosopher's Stone", "9780590353427", 15.99);
		book5.addAuteur(author5);

		Book book6 = new Book("Lord of the Flies", "9780571295715", 11.50);
		book6.addAuteur(author6);

		Book book7 = new Book("1984", "9780451524935", 13.99);
		book7.addAuteur(author7);

		Book book8 = new Book("Animal Farm", "9780451526342", 9.99);
		book8.addAuteur(author7);

		Book book9 = new Book("The Great Gatsby", "9780743273565", 11.50);
		book9.addAuteur(author8);

		Book book10 = new Book("Brave New World", "9780060850524", 14.99);
		book10.addAuteur(author9);

		Book book11 = new Book("One Hundred Years of Solitude", "9780060883287", 12.99);
		book11.addAuteur(author10);

		Book book12 = new Book("War and Peace", "9781400079988", 17.50);
		book12.addAuteur(author1);
		book12.addAuteur(author3);

		Book book13 = new Book("Crime and Punishment", "9780486415871", 10.99);
		book13.addAuteur(author3);

		Book book14 = new Book("Moby-Dick", "9781503275812", 16.99);
		book14.addAuteur(author4);

		Book book15 = new Book("Don Quixote", "9780060934347", 18.50);
		book15.addAuteur(author10);

		// Adding locations to books
		book15.addLocation(location16);
		book15.addLocation(location17);
		book14.addLocation(location15);
		book13.addLocation(location14);
		book12.addLocation(location13);
		book11.addLocation(location12);
		book10.addLocation(location11);
		book9.addLocation(location10);
		book8.addLocation(location9);
		book7.addLocation(location8);
		book6.addLocation(location7);
		book5.addLocation(location6);
		book4.addLocation(location5);
		book3.addLocation(location4);
		book2.addLocation(location3);
		book1.addLocation(location2);
		book1.addLocation(location1);

		// Users
		User user = new User(5, "user", passwordEncoder().encode("12345678"), "USER");
		User user1 = new User(2, "user1", new BCryptPasswordEncoder().encode("12345678"), "USER");
		User user2 = new User(3, "user2", new BCryptPasswordEncoder().encode("12345678"), "USER");
		User user3 = new User(1, "user3", new BCryptPasswordEncoder().encode("12345678"), "USER");
		User admin = new User(10, "admin", new BCryptPasswordEncoder().encode("12345678"), "ADMIN");

		user.addBookToFav(book15);
		user.addBookToFav(book14);
		user.addBookToFav(book13);
		user2.addBookToFav(book15);

		userRepository.save(user);
		userRepository.save(user1);
		userRepository.save(user2);
		userRepository.save(user3);
		userRepository.save(admin);

		// Save books to database
		bookRepository.save(book1);
		bookRepository.save(book2);
		bookRepository.save(book3);
		bookRepository.save(book4);
		bookRepository.save(book5);
		bookRepository.save(book6);
		bookRepository.save(book7);
		bookRepository.save(book8);
		bookRepository.save(book9);
		bookRepository.save(book10);
		bookRepository.save(book11);
		bookRepository.save(book12);
		bookRepository.save(book13);
		bookRepository.save(book14);
		bookRepository.save(book15);

	}

	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
