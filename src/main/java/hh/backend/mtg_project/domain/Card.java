package hh.backend.mtg_project.domain;

import java.util.List;

public class Card {


	private Long id;

	private String cardId;
	private String cardName;
	private String oracleText;

	private String imageUrl;
	private String setName;
	private String typeText;

	private List<String> manaCost;
	private List<String> producedMana;

	private int power;
	private int toughness;

}
