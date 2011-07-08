package se.softhouse.garden.orchid.commons.text;

import java.util.Locale;

/**
 * An interface for functions than can be passed as arguments to the format
 * method.
 * 
 * @author Mikael Svahn
 * 
 */
public interface OrchidMessageFormatFunction {

	public final static String ORCHID_FUNC = "orchid.func.";

	Object execute(OrchidMessageFormatFunctionExecutor executor, OrchidMessageArguments args, Locale locale);
}
