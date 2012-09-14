package se.softhouse.garden.orchid.demo.vaadin;

import static se.softhouse.garden.orchid.spring.text.OrchidMessageSource.code;

import javax.annotation.Resource;

import se.softhouse.garden.orchid.spring.DI;
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource;

public class MSG {

	private static MSG in = new MSG();

	@SuppressWarnings("unused")
	private final Void di = DI.inject(this);

	@Resource
	private OrchidLocalizedMesageSource msg;

	// Constants for the properties that are defined in the message bundle

	public static String HELLO_MESSAGE(String name) {
		return in.msg.get(code("hello.message").arg("name", name));
	}

	public static String COLOR_MESSAGE(String id) {
		return in.msg.get(code("color.message").arg("colorId", id));
	}

	public static String WINDOW_TITLE = in.msg.get("window.title");
}
