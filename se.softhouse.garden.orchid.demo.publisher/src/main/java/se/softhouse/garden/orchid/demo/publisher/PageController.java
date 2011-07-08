package se.softhouse.garden.orchid.demo.publisher;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@RequestMapping("/page")
@Controller
public class PageController {

	private static Logger logger = Logger.getLogger(HomeController.class);

	@Resource
	MessageSource messageSource;

	@RequestMapping("/{pageId}")
	public String page(@PathVariable String pageId, Model model, Locale locale) throws NoSuchRequestHandlingMethodException {
		logger.debug("Accessing /page/" + pageId);
		String view = this.messageSource.getMessage("page." + pageId + ".view", null, locale);
		if (view != null) {
			model.addAttribute("pageId", pageId);
			return view;
		} else {
			throw new NoSuchRequestHandlingMethodException("page", PageController.class);
		}
	}
}
