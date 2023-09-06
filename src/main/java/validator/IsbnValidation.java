package validator;

import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Book;
import repository.BookRepository;

public class IsbnValidation implements Validator {
	@Autowired
	private BookRepository bookRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return Book.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Book book = (Book) target;
		String isbn = book.getIsbn();

		if (isbn == null)
			return;

		String resultValidation = isValidISBN(isbn);
		if (resultValidation == null)
			return;

		errors.rejectValue("isbn", resultValidation, "isbn code is incorrect");

	}

	private String isValidISBN(String isbn) {

		String cleanedIsbn = isbn.replaceAll("[^a-zA-Z0-9]", "");

		if (!cleanedIsbn.matches("\\d{13}"))
			return "isbnValidation.13dig";

		int sum = IntStream.range(0, 12)
				.map(i -> Character.getNumericValue(cleanedIsbn.charAt(i)) * (i % 2 == 0 ? 1 : 3)).sum();
		int checkDigit = 10 - (sum % 10);
		if (checkDigit == 10) {
			checkDigit = 0;
		}
		int lastDigit = Character.getNumericValue(cleanedIsbn.charAt(12));

		if (checkDigit != lastDigit)
			return "isbnValidation.invalidCode";

		boolean isbnExistsInDatabase = bookRepository.existsByIsbn(cleanedIsbn);
		if (isbnExistsInDatabase)
			return "isbnValidation.alreadyExist";

		return null;

	}

}
