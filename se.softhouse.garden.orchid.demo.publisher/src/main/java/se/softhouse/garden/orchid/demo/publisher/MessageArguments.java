package se.softhouse.garden.orchid.demo.publisher;

import se.softhouse.garden.orchid.commons.text.OrchidMessageArgumentCode;

public enum MessageArguments implements OrchidMessageArgumentCode {
	TITLE, NAME;

	private final String realName;

	private MessageArguments() {
		this.realName = name().toLowerCase().replaceAll("_", ".") + ".txt";
	}

	@Override
	public String getRealName() {
		return this.realName;
	}
}
