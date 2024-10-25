package hh.backend.mtgproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import hh.backend.mtgproject.domain.CardRepository;




@Controller
public class CardController {


	@Autowired
    private CardRepository repository;


	@RequestMapping(value= "/cardlist")
    public String bookList(Model model) {
        model.addAttribute("cards", repository.findAll());
        return "cardlist";
    }

}