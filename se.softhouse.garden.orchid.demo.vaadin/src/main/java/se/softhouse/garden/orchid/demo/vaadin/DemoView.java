package se.softhouse.garden.orchid.demo.vaadin;

import javax.annotation.Resource;

import se.softhouse.garden.orchid.spring.DI;
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource;
import se.softhouse.garden.orchid.spring.text.OrchidReloadableResourceBundleMessageSource;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class DemoView extends VerticalLayout {

	@SuppressWarnings("unused")
	private final Void di = DI.inject(this);

	@Resource
	private OrchidLocalizedMesageSource msg;
	@Resource
	private OrchidReloadableResourceBundleMessageSource messageSource;

	public DemoView() {
		addComponent(new Label(MSG.HELLO_MESSAGE("Smith")));
		addComponent(new Label(MSG.COLOR_MESSAGE("10")));
		addComponent(new Label(MSG.COLOR_MESSAGE("20")));
		addComponent(new Label(MSG.COLOR_MESSAGE("")));
	}
}
