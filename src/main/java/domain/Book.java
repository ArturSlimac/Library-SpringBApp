package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import validator.PriceRange;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "isbn")
@JsonPropertyOrder({ "id", "title", "price", "isbn", "auteurs" })
@NamedQueries({
		@NamedQuery(name = "Book.findAllSortedByStarsAndTitle", query = "SELECT b FROM Book b ORDER BY b.stars DESC, b.title ASC") })
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String COVER_URL_FORMAT = "https://covers.openlibrary.org/b/isbn/%s-L.jpg";

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
	@NotBlank(message = "{bookValidation.title}")
	private String title;

	@Getter
	@Setter
	@NumberFormat(pattern = "#.00")
	@PriceRange
	private Double price;

	@Getter
	private int stars;

	@Getter
	@Setter
	private String isbn;

	@Setter
	@Getter
	private String linkToCover;

	@Getter
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Auteur> auteurs = new ArrayList<>();

	@Getter
	@OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
	@JsonManagedReference
	private List<LocationPlace> locationPlaces = new ArrayList<>();

	@Getter
	@ManyToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<User> users = new ArrayList<>();

	public Book(String title, String isbn, double price) {
		super();
		this.title = title;
		this.price = price;
		this.isbn = isbn;
		generateLinkToCover();
	}

	public final void addAuteur(Auteur auteur) {
		this.auteurs.add(auteur);
	}

	public final void addLocation(LocationPlace location) {
		this.locationPlaces.add(location);
		location.setBook(this);
	}

	public void setLocations(List<LocationPlace> locations) {
		locations.forEach(l -> l.setBook(this));
		this.locationPlaces = locations;
	}

	public void cleanLocationPlaces() {
		this.locationPlaces.removeIf(locationPlace -> StringUtils.isBlank(locationPlace.getName())
				|| locationPlace.getCode1() == null || locationPlace.getCode2() == null);
	}

	public void cleanAuteurs() {
		this.auteurs.removeIf(
				auteur -> StringUtils.isBlank(auteur.getFirstName()) || StringUtils.isBlank(auteur.getLastName()));
	}

	public final void addUser(User user) {
		this.users.add(user);
		this.stars++;
	}

	public final void removeUser(User user) {
		this.users.remove(user);
		this.stars--;
	}

	public void generateLinkToCover() {
		linkToCover = String.format(COVER_URL_FORMAT, isbn);
	}

}
