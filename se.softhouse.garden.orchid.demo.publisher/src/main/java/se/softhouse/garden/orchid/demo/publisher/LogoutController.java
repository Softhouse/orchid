package se.softhouse.garden.orchid.demo.publisher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/logout")
@Controller
public class LogoutController {

	@RequestMapping
	public String logout() {
		return "redirect:/resources/j_spring_security_logout";
	}

}
