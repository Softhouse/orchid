package se.softhouse.garden.orchid.spring.text;

import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat.OrchidMessageFormatFunctionExecutorResolver;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunctionExecutor;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatLookup;
import se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageFormatLoader;

public class OrchidSpringDirectoryMessageFormatLoader extends OrchidDirectoryMessageFormatLoader {

	@Override
	protected OrchidMessageFormatFunctionExecutorResolver resolveFunction() {
		return new OrchidMessageFormatFunctionExecutorResolver() {
			@Override
			protected Object resolveFunction(String function, String value, OrchidMessageFormatLookup lookup) {
				return new OrchidMessageFormatFunctionExecutor(function, value, lookup);
			}
		};
	}
}
