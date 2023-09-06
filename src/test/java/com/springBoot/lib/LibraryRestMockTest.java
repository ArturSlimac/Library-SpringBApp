package com.springBoot.lib;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import domain.Auteur;
import domain.Book;
import domain.LocationPlace;
import exceptions.BookNotFoundException;
import service.BookService;

@SpringBootTest
public class LibraryRestMockTest {
	@Mock
	private BookService mock;

	private BooksRestController controller;
	private MockMvc mockMvc;

	private final String TITLE = "My First Book";
	private final double PRICE = 5.55;
	private final String ISBN = "9781234567897";

	private final String FIRSTNAME_AUTHOR1 = "Author1First";
	private final String LASTNAME_AUTHOR1 = "Author1Last";
	private final String FIRSTNAME_AUTHOR2 = "Author2First";
	private final String LASTNAME_AUTHOR2 = "Author2Last";
	private final List<Auteur> AUTEURS = new ArrayList<>(Arrays.asList(new Auteur(FIRSTNAME_AUTHOR1, LASTNAME_AUTHOR1),
			new Auteur(FIRSTNAME_AUTHOR2, LASTNAME_AUTHOR2)));

	private final int CODE1 = 51;
	private final int CODE2 = 180;
	private final String NAME = "TestPlace";
	private final List<LocationPlace> LOCATIONS = new ArrayList<>(Arrays.asList(new LocationPlace(CODE1, CODE2, NAME)));

	@BeforeEach
	public void before() {
		MockitoAnnotations.openMocks(this);
		controller = new BooksRestController();
		mockMvc = standaloneSetup(controller).build();
		ReflectionTestUtils.setField(controller, "bookService", mock);
	}

	private Book aBook(String title, String isbn, double price) {
		Book book = new Book(title, isbn, price);
		return book;
	}

	private Book aBookWithAuthorsAndLocations(String title, String isbn, double price) {
		Book book = new Book(title, isbn, price);
		book.setLocations(LOCATIONS);
		AUTEURS.forEach(book::addAuteur);
		return book;
	}

	private void performRest(String uri) throws Exception {
		mockMvc.perform(get(uri)).andExpect(status().isOk()).andExpect(jsonPath("$.title").value(TITLE))
				.andExpect(jsonPath("$.price").value(PRICE)).andExpect(jsonPath("$.isbn").value(ISBN))
				.andExpect(jsonPath("$.auteurs", hasSize(AUTEURS.size())))
				.andExpect(jsonPath("$.auteurs[0].firstName").value(FIRSTNAME_AUTHOR1))
				.andExpect(jsonPath("$.auteurs[0].lastName").value(LASTNAME_AUTHOR1))
				.andExpect(jsonPath("$.auteurs[1].firstName").value(FIRSTNAME_AUTHOR2))
				.andExpect(jsonPath("$.auteurs[1].lastName").value(LASTNAME_AUTHOR2))
				.andExpect(jsonPath("$.stars").value(0)).andExpect(jsonPath("$.linkToCover").isString())
				.andExpect(jsonPath("$.locationPlaces", hasSize(LOCATIONS.size())))
				.andExpect(jsonPath("$.locationPlaces[0].code1").value(CODE1))
				.andExpect(jsonPath("$.locationPlaces[0].code2").value(CODE2))
				.andExpect(jsonPath("$.locationPlaces[0].name").value(NAME));

	}

	@Test
	public void testGetAllBooks_emptyList() throws Exception {
		Mockito.when(mock.findAll()).thenReturn(new ArrayList<>());
		mockMvc.perform(get("/rest/books/all")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
		Mockito.verify(mock).findAll();
	}

	@Test
	public void testGetAllBooks_noEmptyList() throws Exception {
		Book book1 = aBookWithAuthorsAndLocations(TITLE, ISBN, PRICE);
		Book book2 = aBook("My second book", "9780132350884", 2.2); // no authors, locations
		List<Book> listBooks = List.of(book1, book2);
		Mockito.when(mock.findAll()).thenReturn(listBooks);

		mockMvc.perform(get("/rest/books/all")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$[0].title").value(TITLE))
				.andExpect(jsonPath("$[0].price").value(PRICE)).andExpect(jsonPath("$[0].isbn").value(ISBN))
				.andExpect(jsonPath("$[0].auteurs", hasSize(AUTEURS.size())))
				.andExpect(jsonPath("$[0].auteurs[0].firstName").value(FIRSTNAME_AUTHOR1))
				.andExpect(jsonPath("$[0].auteurs[0].lastName").value(LASTNAME_AUTHOR1))
				.andExpect(jsonPath("$[0].auteurs[1].firstName").value(FIRSTNAME_AUTHOR2))
				.andExpect(jsonPath("$[0].auteurs[1].lastName").value(LASTNAME_AUTHOR2))
				.andExpect(jsonPath("$[0].stars").value(0)).andExpect(jsonPath("$[0].linkToCover").isString())
				.andExpect(jsonPath("$[0].locationPlaces", hasSize(LOCATIONS.size())))
				.andExpect(jsonPath("$[0].locationPlaces[0].code1").value(CODE1))
				.andExpect(jsonPath("$[0].locationPlaces[0].code2").value(CODE2))
				.andExpect(jsonPath("$[0].locationPlaces[0].name").value(NAME))
				.andExpect(jsonPath("$[1].title").value("My second book")).andExpect(jsonPath("$[1].price").value(2.2))
				.andExpect(jsonPath("$[1].isbn").value("9780132350884")).andExpect(jsonPath("$[1].auteurs", hasSize(0)))
				.andExpect(jsonPath("$[1].stars").value(0)).andExpect(jsonPath("$[1].linkToCover").isString())
				.andExpect(jsonPath("$[1].locationPlaces", hasSize(0)));

		Mockito.verify(mock).findAll();
	}

	// ISBN
	@Test
	public void testGetBookByISBN_isOk() throws Exception {

		Mockito.when(mock.findByISBN(ISBN)).thenReturn(aBookWithAuthorsAndLocations(TITLE, ISBN, PRICE));
		performRest("/rest/books?isbn=" + ISBN);
		Mockito.verify(mock).findByISBN(ISBN);
	}

	@Test
	public void testGetBookByISBN_notFound() throws Exception {
		Mockito.when(mock.findByISBN(ISBN)).thenThrow(new BookNotFoundException(ISBN));
		Exception exception = assertThrows(Exception.class, () -> {
			mockMvc.perform(get("/rest/books?isbn=" + ISBN)).andReturn();
		});

		assertTrue(exception.getCause() instanceof BookNotFoundException);

		Mockito.verify(mock).findByISBN(ISBN);
	}

	// AUTHORS
	//// FIRSTNAME
	@Test
	public void testGetBookByAuthorFirstName_isOk() throws Exception {
		List<Book> listBooks = List.of(aBookWithAuthorsAndLocations(TITLE, ISBN, PRICE));

		Mockito.when(mock.findByAuthor(FIRSTNAME_AUTHOR1, "")).thenReturn(listBooks);

		mockMvc.perform(get("/rest/books/author?firstname=" + FIRSTNAME_AUTHOR1)).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].title").value(TITLE)).andExpect(jsonPath("$[0].price").value(PRICE))
				.andExpect(jsonPath("$[0].isbn").value(ISBN))
				.andExpect(jsonPath("$[0].auteurs", hasSize(AUTEURS.size())))
				.andExpect(jsonPath("$[0].auteurs[0].firstName").value(FIRSTNAME_AUTHOR1))
				.andExpect(jsonPath("$[0].auteurs[0].lastName").value(LASTNAME_AUTHOR1))
				.andExpect(jsonPath("$[0].auteurs[1].firstName").value(FIRSTNAME_AUTHOR2))
				.andExpect(jsonPath("$[0].auteurs[1].lastName").value(LASTNAME_AUTHOR2))
				.andExpect(jsonPath("$[0].stars").value(0)).andExpect(jsonPath("$[0].linkToCover").isString())
				.andExpect(jsonPath("$[0].locationPlaces", hasSize(LOCATIONS.size())))
				.andExpect(jsonPath("$[0].locationPlaces[0].code1").value(CODE1))
				.andExpect(jsonPath("$[0].locationPlaces[0].code2").value(CODE2))
				.andExpect(jsonPath("$[0].locationPlaces[0].name").value(NAME));

		Mockito.verify(mock).findByAuthor(FIRSTNAME_AUTHOR1, "");
	}

	@Test
	public void testGetBookByAuthorFirstName_notFound() throws Exception {
		Mockito.when(mock.findByAuthor("Not_a_firstname", ""))
				.thenThrow(new BookNotFoundException("Not_a_firstname", ""));
		Exception exception = assertThrows(Exception.class, () -> {
			mockMvc.perform(get("/rest/books/author?firstname=" + "Not_a_firstname")).andReturn();
		});

		assertTrue(exception.getCause() instanceof BookNotFoundException);

		Mockito.verify(mock).findByAuthor("Not_a_firstname", "");
	}

}
