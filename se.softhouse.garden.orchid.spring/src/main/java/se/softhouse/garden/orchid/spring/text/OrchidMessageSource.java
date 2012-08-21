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
package se.softhouse.garden.orchid.spring.text;

import se.softhouse.garden.orchid.commons.text.OrchidDefaultMessageCode;
import se.softhouse.garden.orchid.commons.text.OrchidMessage;
import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;

/**
 * This is a factory class which contains methods for creating
 * {@link OrchidMessageSourceBuilder}.
 * 
 * @author Mikael Svahn
 * 
 */
public abstract class OrchidMessageSource extends OrchidMessage {

	/**
	 * Creates an instance with the specified code.
	 * 
	 * @param code
	 *            The name of the message specified as a code
	 * 
	 * @return The created {@link OrchidMessageSourceBuilder}
	 */
	public static OrchidMessageSourceBuilder code(OrchidMessageCode code) {
		return new OrchidMessageSourceBuilder(code);
	}

	/**
	 * Creates an instance with the specified code.
	 * 
	 * @param code
	 *            The name of the message specified as a code
	 * 
	 * @return The created {@link OrchidMessageSourceBuilder}
	 */
	public static OrchidMessageSourceBuilder code(String code) {
		return new OrchidMessageSourceBuilder(new OrchidDefaultMessageCode(code));
	}

	/**
	 * Creates an instance with the specified code and default value.
	 * 
	 * @param code
	 *            The name of the message specified as a code
	 * @param defaultValue
	 *            The default value to use when the property is missing
	 * 
	 * @return The created {@link OrchidMessageSourceBuilder}
	 */
	public static OrchidMessageSourceBuilder code(String code, String defaultValue) {
		return new OrchidMessageSourceBuilder(new OrchidDefaultMessageCode(code, defaultValue));
	}

}
