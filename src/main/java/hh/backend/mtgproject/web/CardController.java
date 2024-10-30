package hh.backend.mtgproject.web;

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
    public String bookList(Model model) {
        model.addAttribute("cards", repository.findAll());
        return "cardlist";
    }

    @RequestMapping(value = "/viewcard/{id}")
    public String viewCard(@PathVariable("id") Long cardId, Model model) {
        model.addAttribute("selectedCard", repository.findById(cardId).get());
        // REPLACE WITH PROPER USER HANDLING
        model.addAttribute("currentUser", userRepository.getByUserName("user1"));
        return "viewcard";
    }

    // @PostMapping("/savecardindeck")
    // public String saveNewCardToDeck(@RequestParam Long cardId, @RequestParam Long deckId) {
    //     // Use cardId and deckId to add the card to the deck
    //     Card card = repository.findById(cardId).get();
    //     Deck deck = deckRepository.findById(deckId).get();

        

    //     return "redirect:/success";
    // }

}
