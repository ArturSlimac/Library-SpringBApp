package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import domain.Auteur;

@Repository
public interface AuthorRepository extends CrudRepository<Auteur, Long> {
}
