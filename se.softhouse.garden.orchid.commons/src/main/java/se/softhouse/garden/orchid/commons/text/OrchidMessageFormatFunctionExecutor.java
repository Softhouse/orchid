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
 * The default function executor that will be created when a function is found
 * in the pattern.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageFormatFunctionExecutor {
	protected String function;
	protected String value;
	private OrchidMessageFormatLookup orchidMessageFormatLookup;

	/**
	 * The constructor.
	 * 
	 * @param function
	 *            The name of the function
	 * @param value
	 *            The value associated with the function
	 */
	public OrchidMessageFormatFunctionExecutor(String function, String value, OrchidMessageFormatLookup orchidMessageFormatLookup) {
		this.setFunction(function);
		this.setValue(value);
		this.setOrchidMessageFormatLookup(orchidMessageFormatLookup);
	}

	/**
	 * Sets the name of the function
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * Returns the name of the function
	 */
	public String getFunction() {
		return this.function;
	}

	/**
	 * Sets the value associated with the function
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the value associated with the function
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Set the lookup instance to use when resolving messages within messages
	 */
	public void setOrchidMessageFormatLookup(OrchidMessageFormatLookup orchidMessageFormatLookup) {
		this.orchidMessageFormatLookup = orchidMessageFormatLookup;
	}

	/**
	 * Returns the lookup instance that is used when resolving messages within
	 * messages
	 */
	public OrchidMessageFormatLookup getOrchidMessageFormatLookup() {
		return this.orchidMessageFormatLookup;
	}

	/**
	 * Executes this function.
	 * 
	 * @return The object to insert as an argument, by default it inserts this
	 *         instance.
	 */
	public Object execute(OrchidMessageArguments args, Locale locale) {
		if ("m".equals(this.function)) {
			if (this.getOrchidMessageFormatLookup() != null) {
				OrchidMessageFormat message = this.getOrchidMessageFormatLookup().getMessage(this.value, locale);
				if (message != null) {
					return message.format(args);
				}
			}
			return "{" + this.value + "}";
		}
		OrchidMessageFormatFunction func = (OrchidMessageFormatFunction) args.getArgs().get(OrchidMessageFormatFunction.ORCHID_FUNC + this.function);
		if (func != null) {
			return func.execute(this, args, locale);
		}
		return "{" + this.function + ":" + this.value + "}";
	}

}
