package hh.backend.mtgproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Entity
public class Deck {


	// PRIMARY KEY OF "TABLE" == ENTITY
    // ENTITY INTERFACES WITH THE ACTUAL TABLE IN DATABASE
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long deckId;

	private String name;
	private String description;


	@ManyToOne
    @JsonIgnoreProperties("decks")
    @JoinColumn(name = "userId")
	private MtgUser user;


	public Deck() {
	}


	public Deck(String name, String description) {
		this.name = name;
		this.description = description;
	}


	public Long getDeckId() {
		return deckId;
	}

	public void setDeckId(Long deckId) {
		this.deckId = deckId;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public MtgUser getUser() {
		return user;
	}

	public void setUser(MtgUser user) {
		this.user = user;
	}



	@Override
	public String toString() {
		return "Deck [deckId=" + deckId + ", name=" + name + ", description=" + description + ", user=" + user + "]";
	}


}
