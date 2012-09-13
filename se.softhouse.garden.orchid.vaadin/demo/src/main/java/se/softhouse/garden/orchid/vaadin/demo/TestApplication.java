package se.softhouse.garden.orchid.vaadin.demo;

import se.softhouse.garden.orchid.vaadin.OrchidInputField;
import se.softhouse.garden.orchid.vaadin.OrchidNumberField;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class TestApplication extends Application {

	@Override
	public void init() {
		Window mainWindow = new Window("TestApplication");
		mainWindow.setSizeFull();
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);

		mainWindow.addComponent(createInputField("search"));
		mainWindow.addComponent(createInputField("tel"));
		mainWindow.addComponent(createInputField("url"));
		mainWindow.addComponent(createInputField("email"));
		mainWindow.addComponent(createInputField("datetime"));
		mainWindow.addComponent(createInputField("date"));
		mainWindow.addComponent(createInputField("month"));
		mainWindow.addComponent(createInputField("week"));
		mainWindow.addComponent(createInputField("time"));
		mainWindow.addComponent(createInputField("datetime-local"));
		OrchidInputField number = createInputField("number");
		number.setInputStep("0.01");
		mainWindow.addComponent(number);
		mainWindow.addComponent(createInputField("range"));
		mainWindow.addComponent(createInputField("color"));
		mainWindow.addComponent(createNumberField("number"));
		setMainWindow(mainWindow);
	}

	private OrchidInputField createInputField(String type) {
		OrchidInputField field = new OrchidInputField();
		field.setCaption(type);
		field.setInputType(type);
		return field;
	}

	private OrchidNumberField createNumberField(String type) {
		OrchidNumberField field = new OrchidNumberField();
		field.setCaption("Number+" + type);
		field.setInputType(type);
		return field;
	}
}
