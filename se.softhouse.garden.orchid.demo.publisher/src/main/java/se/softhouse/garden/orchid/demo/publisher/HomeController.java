package se.softhouse.garden.orchid.demo.publisher;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/home")
@Controller
public class HomeController {

	private static Logger logger = Logger.getLogger(HomeController.class);

	@RequestMapping
	public String home(Model model) {
		logger.debug("Accessing /home");
		return "private/home";
	}

}
