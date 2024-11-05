package hh.backend.mtgproject.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import hh.backend.mtgproject.domain.MtgUserRepository;

@Controller
public class IndexController {

	@Autowired
	private MtgUserRepository uRepository;


	private static void setUserIfLogged(Principal principal, Model model, MtgUserRepository uRepository) {

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		if (principal != null) {
			String username = principal.getName();
			model.addAttribute("currentUser", uRepository.getByUserName(username));
		} else {
			System.out.println("No user Found!!");
		}
	}



	@GetMapping(value = { "/", "/index" })
	public String indexMethod(Model model, Principal principal) {

		setUserIfLogged(principal, model, uRepository);
		return "/index";


	}

}
