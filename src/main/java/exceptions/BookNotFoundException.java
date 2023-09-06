package exceptions;

public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BookNotFoundException(String isbn) {
		super(String.format("Could not find any book with isbn %s", isbn));
	}

	public BookNotFoundException(String firstname, String lastname) {
		super(String.format("Could not find any book by author %s %s", firstname != null ? firstname : "",
				lastname != null ? lastname : ""));
	}
}