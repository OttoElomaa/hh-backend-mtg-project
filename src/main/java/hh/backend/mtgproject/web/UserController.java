package hh.backend.mtgproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import hh.backend.mtgproject.domain.MtgUserRepository;

@Controller
public class UserController {


	@Autowired
	private MtgUserRepository repository;



	@RequestMapping(value = "/userlist")
	public String bookList(Model model) {
		model.addAttribute("mtgUsers", repository.findAll());
		return "userlist";
	}

}