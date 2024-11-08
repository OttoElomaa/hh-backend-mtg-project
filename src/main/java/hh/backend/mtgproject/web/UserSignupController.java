package hh.backend.mtgproject.web;


import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hh.backend.mtgproject.domain.AppUser;
import hh.backend.mtgproject.domain.AppUserRepository;
import hh.backend.mtgproject.domain.Deck;
import hh.backend.mtgproject.domain.MtgUser;
import hh.backend.mtgproject.domain.MtgUserRepository;
import hh.backend.mtgproject.domain.SignUpForm;
import jakarta.validation.Valid;

@Controller
public class UserSignupController {



	@Autowired
	private MtgUserRepository repository;
	@Autowired
	private AppUserRepository appUserRepository;

	private static void setUserIfLogged(Principal principal, Model model, MtgUserRepository uRepository) {

		// KYSY PRINCIPAL-Oliolta KUKA ON KÄYTTÄJÄ
		if (principal != null) {
			String username = principal.getName();
			model.addAttribute("currentUser", uRepository.getByUserName(username));
		} 
	}


	// --------------------------------------------------------------------------
	// REGISTER NEW USER


	@RequestMapping(value = "/signup")
	public String addStudent(Model model) {
		model.addAttribute("signupform", new SignUpForm());
		return "signup";
	}

	/**
	 * Create new user
	 * Check if user already exists & form validation
	 * 
	 * param signupForm
	 * param bindingResult
	 * return
	 */
	@RequestMapping(value = "/saveuser", method = RequestMethod.POST)
	public String save(@Valid @ModelAttribute("signupform") SignUpForm signupForm, BindingResult bindingResult) {

		if (!bindingResult.hasErrors()) { // validation errors
			if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) { // check password match
				String pwd = signupForm.getPassword();
				BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
				String hashPwd = bc.encode(pwd);

				AppUser newUser = new AppUser();
				newUser.setPassword(hashPwd);
				newUser.setUsername(signupForm.getUsername());
				newUser.setRole("USER");

				MtgUser newMtgUser = new MtgUser();
				newMtgUser.setUserName(newUser.getUsername());
				newMtgUser.setAppUser(newUser);


				if (repository.getByUserName(signupForm.getUsername()) == null) { // Check if user exists
					repository.save(newMtgUser);
				} else {
					bindingResult.rejectValue("username", "err.username", "Username already exists");
					return "signup";
				}
			} else {
				bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");
				return "signup";
			}
		} else {
			return "signup";
		}
		return "redirect:/login";
	}

	// USER REGISTRATION END
	//--------------------------------------------------------------------------------------------


}
