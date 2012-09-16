package se.softhouse.garden.orchid.demo.vaadin;

import static se.softhouse.garden.orchid.spring.text.OrchidMessageSource.code;
import se.softhouse.garden.orchid.spring.text.OrchidMessageConstants;

public class MSG extends OrchidMessageConstants {

	private static MSG in = new MSG();

	public static String HELLO_MESSAGE(String name) {
		return in.msg.get(code("hello.message").arg("name", name));
	}

	public static String COLOR_MESSAGE(String id) {
		return in.msg.get(code("color.message").arg("colorId", id));
	}

	public static String WINDOW_TITLE = in.msg.get("window.title");
}
