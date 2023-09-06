package com.springBoot.lib;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Book;
import domain.User;
import jakarta.servlet.http.HttpServletRequest;
import repository.BookRepository;
import repository.UserRepository;

@Controller
@RequestMapping("")
public class UserController {
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/favourites")
	public String favBooks(Model model, HttpServletRequest request, Authentication authentication,
			RedirectAttributes redirectAttributes) {
		User user = userRepository.findByUsername(authentication.getName());
		model.addAttribute("user", user);
		model.addAttribute("books", user.getFavBooks());
		model.addAttribute("favActive", request.getRequestURI().equals("/favourites"));

		return "books";
	}

	@PostMapping(value = "/addBookToFav")
	public String addBookToFav(@RequestParam("bookId") Long bookId, Authentication authentication,
			RedirectAttributes redirectAttributes) {

		User user = getCurrentUser(authentication);
		Optional<Book> book = getBookById(bookId);

		if (user != null && book.isPresent()) {
			user.addBookToFav(book.get());
			userRepository.save(user);
			redirectAttributes.addFlashAttribute("toFavMessage", book.get().getTitle());

		}

		return "redirect:/books";
	}

	@PostMapping(value = "/removeBookFromFav")
	public String removeBookFromFav(@RequestParam("bookId") Long bookId, Authentication authentication,
			RedirectAttributes redirectAttributes) {

		User user = getCurrentUser(authentication);
		Optional<Book> book = getBookById(bookId);

		if (user != null && book.isPresent()) {
			user.removeBookFromFav(book.get());
			userRepository.save(user);
			redirectAttributes.addFlashAttribute("fromFavMessage", book.get().getTitle());
		}

		return "redirect:/books";
	}

	private User getCurrentUser(Authentication authentication) {
		return userRepository.findByUsername(authentication.getName());
	}

	private Optional<Book> getBookById(Long bookId) {
		return bookRepository.findById(bookId);
	}

}
