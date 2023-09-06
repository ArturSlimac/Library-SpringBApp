package com.springBoot.lib;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Book;
import domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import repository.AuthorRepository;
import repository.BookRepository;
import repository.UserRepository;
import validator.AuteursValidator;
import validator.IsbnValidation;
import validator.LocationPlaceValidation;

@Controller
@RequestMapping("/books")
public class BookController {
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IsbnValidation isbnValidator;

	@Autowired
	private LocationPlaceValidation locationPlaceValidator;

	@Autowired
	private AuteursValidator auteursValidator;

	@GetMapping
	public String allBooks(Model model, HttpServletRequest request, Authentication authentication,
			RedirectAttributes redirectAttributes) {
		List<Book> books = bookRepository.findAll();
		model.addAttribute("books", books);
		model.addAttribute("allBooksActive", request.getRequestURI().equals("/books"));

		handleFlashAttributes(model, redirectAttributes);
		addUserToModel(model, request, authentication);
		return "books";
	}

	@GetMapping(value = "/{id}")
	public String bookDetails(@PathVariable Long id, Model model, HttpServletRequest request,
			Authentication authentication) {

		Optional<Book> book = bookRepository.findById(id);
		if (!book.isPresent()) {
			return "redirect:/books";
		}
		model.addAttribute("book", book.get());

		addUserToModel(model, request, authentication);
		return "bookDetails";
	}

	@GetMapping(value = "/popular")
	public String popularBooks(Model model, HttpServletRequest request, Authentication authentication) {
		model.addAttribute("books", bookRepository.findAllSortedByStarsAndTitle());
		model.addAttribute("popularActive", request.getRequestURI().equals("/books/popular"));

		addUserToModel(model, request, authentication);
		return "books";
	}

	@GetMapping(value = "/add")
	public String addNewBook(Model model, HttpServletRequest request, Authentication authentication,
			RedirectAttributes redirectAttributes) {
		model.addAttribute("book", new Book());

		addUserToModel(model, request, authentication);
		return "addBook";
	}

	@PostMapping(value = "/add")
	public String onSubmit(@Valid Book book, BindingResult result, Model model, Authentication authentication,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		isbnValidator.validate(book, result);

		book.cleanLocationPlaces();
		locationPlaceValidator.validate(book.getLocationPlaces(), result);

		book.cleanAuteurs();
		auteursValidator.validate(book.getAuteurs(), result);

		if (result.hasErrors()) {
			addUserToModel(model, request, authentication);
			return "addBook";
		}

		book.generateLinkToCover();
		book.setLocations(book.getLocationPlaces());
		authorRepository.saveAll(book.getAuteurs());
		bookRepository.save(book);

		redirectAttributes.addFlashAttribute("addedBook", book);

		return "redirect:/books";
	}

	private void addUserToModel(Model model, HttpServletRequest request, Authentication authentication) {
		User user = userRepository.findByUsername(authentication.getName());
		model.addAttribute("user", user);
		model.addAttribute("currentUrl", request.getRequestURI());
	}

	private void handleFlashAttributes(Model model, RedirectAttributes redirectAttributes) {
		if (redirectAttributes.getFlashAttributes().containsKey("toFavMessage")) {
			model.addAttribute("toFavMessage", (String) redirectAttributes.getFlashAttributes().get("toFavMessage"));
		}
		if (redirectAttributes.getFlashAttributes().containsKey("fromFavMessage")) {
			model.addAttribute("fromFavMessage",
					(String) redirectAttributes.getFlashAttributes().get("fromFavMessage"));
		}

		if (redirectAttributes.getFlashAttributes().containsKey("addedBook")) {
			model.addAttribute("addedBook", (Book) redirectAttributes.getFlashAttributes().get("addedBook"));
		}
	}

}
