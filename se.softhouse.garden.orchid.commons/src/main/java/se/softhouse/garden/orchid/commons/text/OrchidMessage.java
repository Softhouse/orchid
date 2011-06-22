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

/**
 * This is a factory class which contains methods for creating
 * {@link OrchidMessageArguments}.
 * 
 * @author Mikael Svahn
 * 
 */
public abstract class OrchidMessage {

	/**
	 * Creates an instance and adds one argument.
	 * 
	 * @param code
	 *            The name of the argument specified as a code
	 * @param value
	 *            The value of the argument
	 * 
	 * @return The created {@link OrchidMessageArguments}
	 */
	public static OrchidMessageArguments arg(OrchidMessageArgumentCode code, Object value) {
		return new OrchidMessageArguments(code, value);
	}

	/**
	 * Creates an instance and adds one argument.
	 * 
	 * @param name
	 *            The name of the argument
	 * @param value
	 *            The value of the argument
	 * 
	 * @return The created {@link OrchidMessageArguments}
	 */
	public static OrchidMessageArguments arg(String name, Object value) {
		return new OrchidMessageArguments(name, value);
	}

	/**
	 * Creates an instance without any arguments
	 */
	public static OrchidMessageArguments args() {
		return new OrchidMessageArguments();
	}

}
