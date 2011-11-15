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
/**
 * Copyright (c) 2011, Mikael Svahn, Softhouse Consulting AB
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so.
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

import java.util.HashMap;
import java.util.Map;

/**
 * This is a builder class used to create the arguments used for the
 * {@link OrchidMessageFormat}. Each call to the arg method will add an argument
 * to the instance.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageArguments {

	private final Map<String, Object> args;

	/**
	 * Creates an instance without any arguments
	 */
	public OrchidMessageArguments() {
		this.args = new HashMap<String, Object>();
	}

	/**
	 * Copy constructor
	 * 
	 * @param other
	 *            The object to copy
	 */
	public OrchidMessageArguments(OrchidMessageArguments other) {
		this.args = new HashMap<String, Object>();
		this.args.putAll(other.getArgs());
	}

	/**
	 * Creates an instance and adds one argument.
	 * 
	 * @param code
	 *            The name of the argument specified as a code
	 * @param value
	 *            The value of the argument
	 */
	public OrchidMessageArguments(OrchidMessageArgumentCode code, Object value) {
		this.args = new HashMap<String, Object>();
		this.args.put(code.getRealName(), value);
	}

	/**
	 * Adds an argument to this instance.
	 * 
	 * @param code
	 *            The name of the argument specified as a code
	 * @param value
	 *            The value of the argument
	 */
	public OrchidMessageArguments arg(OrchidMessageArgumentCode code, Object value) {
		this.args.put(code.getRealName(), value);
		return this;
	}

	/**
	 * Creates an instance and adds one argument.
	 * 
	 * @param name
	 *            The name of the argument
	 * @param value
	 *            The value of the argument
	 */
	public OrchidMessageArguments(String name, Object value) {
		this.args = new HashMap<String, Object>();
		this.args.put(name, value);
	}

	/**
	 * Adds an argument to this instance.
	 * 
	 * @param subkey
	 *            The name of the argument
	 * @param value
	 *            The value of the argument
	 */
	public OrchidMessageArguments arg(String id, Object value) {
		this.args.put(id, value);
		return this;
	}

	/**
	 * Adds an argument to this instance.
	 * 
	 * @param subkey
	 *            The name of the argument
	 * @param value
	 *            The value of the argument
	 */
	public OrchidMessageArguments func(String id, Object value) {
		this.args.put(OrchidMessageFormatFunction.ORCHID_FUNC + id, value);
		return this;
	}

	/**
	 * Returns a {@link Map} of all arguments of this instance.
	 */
	public Map<String, Object> getArgs() {
		return this.args;
	}

}
