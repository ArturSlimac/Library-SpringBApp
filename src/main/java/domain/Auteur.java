package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter

@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@JsonPropertyOrder({ "id", "firstName", "lastName" })
public class Auteur implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@Column(name = "firstname")
	private String firstName;

	@Setter
	@Column(name = "lastname")
	private String lastName;

	@ManyToMany(mappedBy = "auteurs", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JsonBackReference
	private Set<Book> books = new HashSet<>();

	public Auteur(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public void addBook(Book book) {
		this.books.add(book);
	}

	public void setBooks(Set<Book> books) {
		books.forEach(book -> book.addAuteur(this));
		this.books = books;
	}

}
