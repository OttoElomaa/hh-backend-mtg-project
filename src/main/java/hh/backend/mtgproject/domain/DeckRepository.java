package hh.backend.mtgproject.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckRepository extends CrudRepository<Deck, Long> {
	// List<Card> findByName(@Param("cardName") String title);
}
