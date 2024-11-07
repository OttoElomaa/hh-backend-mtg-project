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
public class UserController {

	@Autowired
	private MtgUserRepository repository;
	@Autowired
	private AppUserRepository appUserRepository;

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
	public String userList(Model model, Principal principal) {
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
		// (selectedUser MATCHES LOGGED USER) -> THEN SHOW PERSONAL PROFILE WITH EDIT
		// TOOLS
		if (principal != null) {

			MtgUser currUser = repository.getByUserName(principal.getName());
			if (currUser == selectedUser) {
				return "myprofile";
			}
		}
		// OTHERWISE, SHOW OTHER USER'S GENERIC USER PAGE
		return "viewuser";

	}

	// REGISTER NEW USER

	@GetMapping(value = { "/registeruser" })
	public String registerMethod(Model model, Principal principal) {

		AppUser newUser = new AppUser();
		newUser.setRole("USER");
		model.addAttribute("newAppUser", newUser);
		setUserIfLogged(principal, model, repository);
		return "/registeruser";
	}

	// -> AND SAVE IT

	@PostMapping(value = "/savenewuser")
	public String saveNew(@ModelAttribute("newAppUser") AppUser newUser, BindingResult result) {

		if (result.hasErrors()) {
			return "registeruser"; // Return to form if there are binding errors
		}

		System.out.println("STARTING NEW USER SAVE");

		MtgUser newMtgUser = new MtgUser();
		newMtgUser.setUserName(newUser.getUsername());
		newMtgUser.setAppUser(newUser);

		System.out.println("NEW APP USER: " + newUser.toString());
		System.out.println("NEW MTG USER: " + newMtgUser.toString());

		repository.save(newMtgUser);

		return "redirect:/index";
	}

	@RequestMapping(value = "/signup")
	public String addStudent(Model model) {
		model.addAttribute("signupform", new SignUpForm());
		return "signup";
	}

	/**
	 * Create new user
	 * Check if user already exists & form validation
	 * 
	 * @param signupForm
	 * @param bindingResult
	 * @return
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

}
