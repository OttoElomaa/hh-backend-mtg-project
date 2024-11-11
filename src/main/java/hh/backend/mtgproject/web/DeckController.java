package hh.backend.mtgproject.web;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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



	private static void setUserIfLogged(Principal principal, Model model, MtgUserRepository uRepository) {

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		if (principal != null) {
			String username = principal.getName();
			model.addAttribute("currentUser", uRepository.getByUserName(username));
		} else {
			System.out.println("No user Found!!");
		}
	}


	// FUNCTIONS ACTIVATED DURING THE ADDING OF NEW DECKS IN CREATEDECK.HTML

	private void addCardToDeck(Card card, Deck deck) {

		//setUserIfLogged(principal, model, uRepository);

		Set<Card> cardsInDeck = deck.getCardsInDeck();
		cardsInDeck.add(card);
		deck.setCardsInDeck(cardsInDeck);
		repository.save(deck);
	}




	// DECK CREATION
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/createdeck")
	public String addBook(Model model, Principal principal) {
		model.addAttribute("newDeck", new Deck());
		
		setUserIfLogged(principal, model, userRepository);

		return "createdeck";
	}

	// USER CAN ONLY SAVE DECKS TO THEIR _OWN_ PROFILE
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@PostMapping(value = "/savenew")
	public String saveNew(Deck newDeck, Model model, Principal principal) {
		
		// LISÄÄ USER NYKYISEKSI KÄYTTÄJÄKSI
		setUserIfLogged(principal, model, userRepository);
		MtgUser setUser = userRepository.getByUserName(principal.getName());

		newDeck.setUser(setUser);
		repository.save(newDeck);

		return "redirect:/myprofile";
	}



	// VIEW DECK
	@RequestMapping(value = "/viewdeck/{id}")
	public String viewCard(@PathVariable("id") Long deckId, Model model, Principal principal) {
		model.addAttribute("selectedDeck", repository.findById(deckId).get());
	
		setUserIfLogged(principal, model, userRepository);
		return "viewdeck";
	}



	// SAVE CARD IN DECK
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@PostMapping("/savecardindeck")
	public String saveNewCardToDeck(@RequestParam Long cardId, @RequestParam Long deckId, 
	Principal principal, Model model) {
		// Use cardId and deckId to add the card to the deck
		Card card = cardRepository.findById(cardId).get();
		Deck deck = repository.findById(deckId).get();

		addCardToDeck(card, deck);

		setUserIfLogged(principal, model, userRepository);

		return "redirect:/viewdeck/" + deck.getDeckId().toString();
	}



	// DELETE FUNCTION FOR DECKS SELECTED IN MYPROFILE.HTML

	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @GetMapping(value = "/deletedeck/{id}")
    public String deleteBook(@PathVariable("id") Long deckId, Model model, Principal principal) {

		setUserIfLogged(principal, model, userRepository);
		Deck selectedDeck = repository.findById(deckId).get();
		MtgUser currUser = userRepository.getByUserName(principal.getName());

		if (currUser == selectedDeck.getUser()) {
        	repository.deleteById(deckId);
        	return "redirect:/myprofile";
		}
		return "redirect:/myprofile";
    }




	// FUNCTIONS ACTIVATED DURING THE EDITING OF A DECK BELONGING TO YOUR USER
	// editdeck.html. ONLY EDITS CURRENTUSER'S DECK
    
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @RequestMapping(value = "/editdeck/{id}")
    public String editDeck(@PathVariable("id") Long deckId, Model model, Principal principal) {

		setUserIfLogged(principal, model, userRepository);
		Deck toEdit = repository.findById(deckId).get();

		// ONLY IF CURRENT USER == DECK.OWNER
		if (principal != null) {
			MtgUser currUser = userRepository.getByUserName(principal.getName());
			if (currUser == toEdit.getUser()) {

				model.addAttribute("currentDeck", toEdit);
				return "editdeck";
			}
		}
        return "userlist";
    }

	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PostMapping(value = "/savemodifieddeck")
    public String saveModified(Deck editedDeck, Model model, Principal principal){

		// SET CURRENT USER AS DECK OWNER
		setUserIfLogged(principal, model, userRepository);
		editedDeck.setUser(userRepository.getByUserName(principal.getName()));

		// MOVE CARD INFO FROM OLD DECK TO NEW DECK
		Deck oldDeck = repository.findById(editedDeck.getDeckId()).get();
		editedDeck.setCardsInDeck(oldDeck.getCardsInDeck());

        repository.save(editedDeck);
        return "redirect:/myprofile";
    }

}
