package hh.backend.mtgproject.domain;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
	//List<Card> findByName(@Param("cardName") String title);
}
