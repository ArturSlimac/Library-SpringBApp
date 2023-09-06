package domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(exclude = "id")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int maxAmountOfFavBooks;

	private String username;

	private String password;

	private int enabled;

	private String role;

	public User(int maxAmountOfFavBooks, String username, String password, String role) {
		this.maxAmountOfFavBooks = maxAmountOfFavBooks;
		this.username = username;
		this.password = password;
		this.enabled = 1;
		this.role = role;

	}

	@ManyToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Set<Book> favBooks = new HashSet<>();

	public void addBookToFav(Book book) {
		book.addUser(this);
		favBooks.add(book);
	}

	public void removeBookFromFav(Book book) {
		book.removeUser(this);
		favBooks.remove(book);
	}

}
