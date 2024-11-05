package hh.backend.mtgproject.web;

import java.security.Principal;

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
import hh.backend.mtgproject.domain.MtgUserRepository;

@Controller
public class CardController {

    @Autowired
    private CardRepository repository;
    @Autowired
    private MtgUserRepository userRepository;
    @Autowired
    private DeckRepository deckRepository;


    private static void setUserIfLogged(Principal principal, Model model, MtgUserRepository uRepository) {

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		if (principal != null) {
			String username = principal.getName();
			model.addAttribute("currentUser", uRepository.getByUserName(username));
		} else {
			System.out.println("No user Found!!");
		}
	}


    @RequestMapping(value = "/cardlist")
    public String bookList(Model model, Principal principal) {
        model.addAttribute("cards", repository.findAll());

		setUserIfLogged(principal, model, userRepository);

        return "cardlist";
    }


    @RequestMapping(value = "/viewcard/{id}")
    public String viewCard(@PathVariable("id") Long cardId, Model model, Principal principal) {
        model.addAttribute("selectedCard", repository.findById(cardId).get());
        
		setUserIfLogged(principal, model, userRepository);

        return "viewcard";
    }

   
}
