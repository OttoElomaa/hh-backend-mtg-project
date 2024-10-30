package hh.backend.mtgproject.domain;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;


@Entity
public class Card {

	// PRIMARY KEY OF "TABLE" == ENTITY
    // ENTITY INTERFACES WITH THE ACTUAL TABLE IN DATABASE
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String cardId;
	private String cardName;
	private String oracleText;


	private String imageUrl;
	private String setName;
	private String typeText;
	private List<String> colorIdentity;

	private String manaCost;
	private List<String> producedMana;

	private int power;
	private int toughness;


	@ManyToMany(mappedBy = "cardsInDeck")
	Set<Deck> inDecks;



	public Card(String cardId, String cardName, String oracleText, String imageUrl, String setName,
			String typeText, List<String> colorIdentity, String manaCost, List<String> producedMana, int power,
			int toughness) {
		
		this.cardId = cardId;
		this.cardName = cardName;
		this.oracleText = oracleText;
		this.imageUrl = imageUrl;
		this.setName = setName;
		this.typeText = typeText;
		this.colorIdentity = colorIdentity;
		this.manaCost = manaCost;
		this.producedMana = producedMana;
		this.power = power;
		this.toughness = toughness;
	}




	public Card() {
	}


	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}


	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}


	public String getOracleText() {
		return oracleText;
	}
	public void setOracleText(String oracleText) {
		this.oracleText = oracleText;
	}


	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getSetName() {
		return setName;
	}
	public void setSetName(String setName) {
		this.setName = setName;
	}


	public String getTypeText() {
		return typeText;
	}
	public void setTypeText(String typeText) {
		this.typeText = typeText;
	}


	public String getManaCost() {
		return manaCost;
	}
	public void setManaCost(String manaCost) {
		this.manaCost = manaCost;
	}


	public List<String> getProducedMana() {
		return producedMana;
	}
	public void setProducedMana(List<String> producedMana) {
		this.producedMana = producedMana;
	}


	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}


	public int getToughness() {
		return toughness;
	}
	public void setToughness(int toughness) {
		this.toughness = toughness;
	}

	
	public List<String> getColorIdentity() {
		return colorIdentity;
	}

	public void setColorIdentity(List<String> colorIdentity) {
		this.colorIdentity = colorIdentity;
	}

	public Set<Deck> getInDecks() {
		return inDecks;
	}




	public void setInDecks(Set<Deck> inDecks) {
		this.inDecks = inDecks;
	}



	@Override
	public String toString() {
		return "Card [id=" + id + ", cardId=" + cardId + ", cardName=" + cardName + ", oracleText=" + oracleText
				+ ", imageUrl=" + imageUrl + ", setName=" + setName + ", typeText=" + typeText + ", manaCost="
				+ manaCost + ", producedMana=" + producedMana + ", power=" + power + ", toughness=" + toughness + "]";
	}




	





	
	
}
