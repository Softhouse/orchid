package se.softhouse.garden.orchid.commons.text;

import java.util.Locale;

public interface OrchidMessageFormatLookup {
	OrchidMessageFormat getMessage(String code, Locale locale);
}
