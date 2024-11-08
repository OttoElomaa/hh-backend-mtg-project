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




	// FUNCTIONS ACTIVATED DURING THE EDITING OF YOUR OWN PROFILE
	// editprofile.html. ONLY EDITS CURRENTUSER'S PROFILE
    
	@PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/editprofile")
    public String editBook(Model model, Principal principal) {

		setUserIfLogged(principal, model, repository);
        return "editprofile";
    }

	@PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/savemodifieduser")
    public String saveModified(MtgUser editedUser){
        repository.save(editedUser);
        return "redirect:/userlist";
    }


	
}
