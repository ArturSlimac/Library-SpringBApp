package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findAllSortedByStarsAndTitle();

	boolean existsByIsbn(String isbn);

	Book findByIsbn(String isbn);

	List<Book> findByAuteursFirstNameAndAuteursLastName(String firstName, String lastName);

	List<Book> findByAuteursFirstName(String firstName);

	List<Book> findByAuteursLastName(String lastName);
}