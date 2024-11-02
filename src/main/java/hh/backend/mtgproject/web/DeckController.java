package hh.backend.mtgproject.web;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hh.backend.mtgproject.domain.Card;
import hh.backend.mtgproject.domain.CardRepository;
import hh.backend.mtgproject.domain.Deck;
import hh.backend.mtgproject.domain.DeckRepository;
import hh.backend.mtgproject.domain.MtgUser;
import hh.backend.mtgproject.domain.MtgUserRepository;

@Controller
public class DeckController {

	@Autowired
	private DeckRepository repository;
	@Autowired
	private MtgUserRepository userRepository;
	@Autowired
	private CardRepository cardRepository;




	// FUNCTIONS ACTIVATED DURING THE ADDING OF NEW DECKS IN CREATEDECK.HTML

	private void addCardToDeck(Card card, Deck deck) {

		Set<Card> cardsInDeck = deck.getCardsInDeck();
		cardsInDeck.add(card);
		deck.setCardsInDeck(cardsInDeck);
		repository.save(deck);
	}




	// DECK CREATION
	@RequestMapping(value = "/createdeck")
	public String addBook(Model model, Principal principal) {
		model.addAttribute("newDeck", new Deck());
		
		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", userRepository.getByUserName(username));

		return "createdeck";
	}

	@PostMapping(value = "/savenew")
	public String saveNew(Deck newDeck, Model model, Principal principal) {
		// LISÄÄ USER 1 NYKYISEKSI KÄYTTÄJÄKSI
		String username = principal.getName();
		MtgUser setUser = userRepository.getByUserName(username);

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		// String username = principal.getName();
		newDeck.setUser(setUser);
		repository.save(newDeck);

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		
		model.addAttribute("currentUser", setUser);
		return "redirect:/userlist";
	}

	// VIEW DECK
	@RequestMapping(value = "/viewdeck/{id}")
	public String viewCard(@PathVariable("id") Long deckId, Model model, Principal principal) {
		model.addAttribute("selectedDeck", repository.findById(deckId).get());
	
		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", userRepository.getByUserName(username));
		return "viewdeck";
	}

	// SAVE CARD IN DECK
	@PostMapping("/savecardindeck")
	public String saveNewCardToDeck(@RequestParam Long cardId, @RequestParam Long deckId, 
	Principal principal, Model model) {
		// Use cardId and deckId to add the card to the deck
		Card card = cardRepository.findById(cardId).get();
		Deck deck = repository.findById(deckId).get();

		addCardToDeck(card, deck);

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", userRepository.getByUserName(username));

		return "redirect:/viewdeck/" + deck.getDeckId().toString();
	}

}
