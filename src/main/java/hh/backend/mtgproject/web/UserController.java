package hh.backend.mtgproject.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import hh.backend.mtgproject.domain.MtgUser;
import hh.backend.mtgproject.domain.MtgUserRepository;

@Controller
public class UserController {


	@Autowired
	private MtgUserRepository repository;


	private static void setUserIfLogged(Principal principal, Model model, MtgUserRepository uRepository) {

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		if (principal != null) {
			String username = principal.getName();
			model.addAttribute("currentUser", uRepository.getByUserName(username));
		} else {
			System.out.println("No user Found!!");
		}
	}


	@RequestMapping(value = "/userlist")
	public String bookList(Model model, Principal principal) {
		model.addAttribute("mtgUsers", repository.findAll());

		setUserIfLogged(principal, model, repository);

		return "userlist";
	}


	@RequestMapping(value = "/viewuser/{id}")
    public String viewUser(@PathVariable("id") Long userId, Model model, Principal principal) {
        
		MtgUser selectedUser = repository.findById(userId).get();

		model.addAttribute("selectedUser", selectedUser);
		setUserIfLogged(principal, model, repository);

		// VERY CLEVER: IF LOOKING AT YOUR USER PAGE 
		// (selectedUser MATCHES LOGGED USER) -> THEN SHOW PERSONAL PROFILE WITH EDIT TOOLS
		if (principal != null) {

			MtgUser currUser = repository.getByUserName(principal.getName());
			if (currUser == selectedUser) {
				return "myprofile";
			}
		}
		// OTHERWISE, SHOW OTHER USER'S GENERIC USER PAGE
        return "viewuser";
    }

}
