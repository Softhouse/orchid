/**
 * Copyright (c) 2011, Mikael Svahn, Softhouse Consulting AB
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

	/**
	 * Executes the function
	 * 
	 * @param executor
	 *            The executor calling this function
	 * @param args
	 *            The arguments provided to the format operation
	 * @param locale
	 *            The locale to use
	 * 
	 * @return The result of the function
	 */
	Object execute(OrchidMessageFormatFunctionExecutor executor, OrchidMessageArguments args, Locale locale);
}
