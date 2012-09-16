package se.softhouse.garden.orchid.demo.vaadin;

import javax.annotation.Resource;

import se.softhouse.garden.orchid.spring.DI;
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource;

import com.vaadin.Application;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class DemoApplication extends Application {

	@SuppressWarnings("unused")
	private final Void di = DI.inject(this);

	@Resource
	protected OrchidLocalizedMesageSource msg;

	@Override
	public void init() {
		this.msg.setLocale(getLocale());
		Window mainWindow = new Window(MSG.WINDOW_TITLE);
		setMainWindow(mainWindow);
		mainWindow.setSizeFull();
		mainWindow.addComponent(new DemoView());
	}
}
