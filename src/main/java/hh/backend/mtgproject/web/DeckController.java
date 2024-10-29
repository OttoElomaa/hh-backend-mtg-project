package hh.backend.mtgproject.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


	// FUNCTIONS ACTIVATED DURING THE ADDING OF NEW DECKS IN CREATEDECK.HTML

    @RequestMapping(value = "/createdeck")
    public String addBook(Model model){
        model.addAttribute("newDeck", new Deck());
        //model.addAttribute("categories", repository.findAll());
        return "createdeck";
    }

    
    @PostMapping(value = "/savenew")
    public String saveNew(Deck newDeck, Principal principal){
		// LISÄÄ USER 1 NYKYISEKSI KÄYTTÄJÄKSI
		MtgUser setUser = userRepository.getByUserName("user1");

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		//String username = principal.getName();
		newDeck.setUser(setUser);
        repository.save(newDeck);

        return "redirect:/userlist";
    }

}
