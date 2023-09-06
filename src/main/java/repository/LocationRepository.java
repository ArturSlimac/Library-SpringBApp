package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.LocationPlace;

@Repository
public interface LocationRepository extends JpaRepository<LocationPlace, Long> {

}