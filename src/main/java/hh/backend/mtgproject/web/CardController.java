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



    @RequestMapping(value = "/cardlist")
    public String bookList(Model model, Principal principal) {
        model.addAttribute("cards", repository.findAll());

        // KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", userRepository.getByUserName(username));

        return "cardlist";
    }


    @RequestMapping(value = "/viewcard/{id}")
    public String viewCard(@PathVariable("id") Long cardId, Model model, Principal principal) {
        model.addAttribute("selectedCard", repository.findById(cardId).get());
        // REPLACE WITH PROPER USER HANDLING
        // KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", userRepository.getByUserName(username));

        return "viewcard";
    }

   
}
