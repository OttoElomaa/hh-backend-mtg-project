package hh.backend.mtgproject.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import hh.backend.mtgproject.domain.MtgUserRepository;

@Controller
public class UserController {


	@Autowired
	private MtgUserRepository repository;



	@RequestMapping(value = "/userlist")
	public String bookList(Model model, Principal principal) {
		model.addAttribute("mtgUsers", repository.findAll());

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", repository.getByUserName(username));

		return "userlist";
	}


	@RequestMapping(value = "/viewuser/{id}")
    public String viewUser(@PathVariable("id") Long userId, Model model, Principal principal) {
        model.addAttribute("selectedUser", repository.findById(userId).get());

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", repository.getByUserName(username));

        return "viewuser";
    }

}
