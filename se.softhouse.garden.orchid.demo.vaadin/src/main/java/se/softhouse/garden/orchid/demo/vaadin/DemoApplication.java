package se.softhouse.garden.orchid.demo.vaadin;

import se.softhouse.garden.orchid.spring.DI;

import com.vaadin.Application;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class DemoApplication extends Application {

	@SuppressWarnings("unused")
	private final Void di = DI.inject(this);

	@Override
	public void init() {
		Window mainWindow = new Window(MSG.WINDOW_TITLE);
		setMainWindow(mainWindow);
		mainWindow.setSizeFull();
		mainWindow.addComponent(new DemoView());
	}
}
