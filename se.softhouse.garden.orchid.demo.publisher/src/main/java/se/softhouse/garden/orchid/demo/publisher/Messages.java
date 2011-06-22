package se.softhouse.garden.orchid.demo.publisher;

import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;

public enum Messages implements OrchidMessageCode {
	WEB_MAIN_TITLE("Main Title"), //
	;

	private final String realName;
	private final String pattern;

	private Messages(String pattern) {
		this.pattern = pattern;
		this.realName = name().toLowerCase().replaceAll("_", ".") + ".txt";
	}

	@Override
	public String getPattern() {
		return this.pattern;
	}

	@Override
	public String getRealName() {
		return this.realName;
	}

}
