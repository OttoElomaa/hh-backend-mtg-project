package hh.backend.mtgproject.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import hh.backend.mtgproject.domain.AppUser;
import hh.backend.mtgproject.domain.AppUserRepository;
import hh.backend.mtgproject.domain.Deck;
import hh.backend.mtgproject.domain.MtgUser;
import hh.backend.mtgproject.domain.MtgUserRepository;


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



	// FUNCTION TO SPECIFICALLY VIEW OWN PROFILE
	// AFTER CRUD ACTIONS + BY PRESSING 'MY PROFILE' BUTTON

	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
	@RequestMapping(value = "/myprofile")
	public String viewMyProfile(Model model, Principal principal) {

		setUserIfLogged(principal, model, repository);

		// SHOW PROFILE ONLY IF LOGGED IN
		// -> THEN SHOW PERSONAL PROFILE WITH EDIT TOOLS
		if (principal != null) {
			model.addAttribute("selectedUser", repository.getByUserName(principal.getName()));
			return "myprofile";
			}
		
		// OTHERWISE, SHOW OTHER USER'S GENERIC USER PAGE
		return "userlist";
	}




	// FUNCTIONS ACTIVATED DURING THE EDITING OF YOUR OWN PROFILE
	// editprofile.html. ONLY EDITS CURRENTUSER'S PROFILE
    
	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @RequestMapping(value = "/editprofile")
    public String editProfile(Model model, Principal principal) {

		setUserIfLogged(principal, model, repository);
        return "editprofile";
    }

	@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PostMapping(value = "/savemodifieduser")
    public String saveModified(MtgUser editedUser){

		// MOVE CARD INFO FROM OLD DECK TO NEW DECK
		MtgUser oldUser = repository.findById(editedUser.getUserId()).get();
		editedUser.setAppUser(oldUser.getAppUser());

        repository.save(editedUser);
        return "redirect:/userlist";
    }


	    // FUNCTION TO DELETE USER SELECTED IN USERLIST.HTML BY AN ADMIN

		@PreAuthorize("hasRole('ADMIN')")
		@GetMapping(value = "/deleteuser/{id}")
		public String deleteBook(@PathVariable("id") Long userId, Model model) {
			repository.deleteById(userId);
			return "redirect:/userlist";
		}


	
}
