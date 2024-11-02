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


	@GetMapping(value = { "/", "/index" })
	public String indexMethod(Model model, Principal principal) {

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		String username = principal.getName();
		model.addAttribute("currentUser", uRepository.getByUserName(username));

		return "index";
	}


}
