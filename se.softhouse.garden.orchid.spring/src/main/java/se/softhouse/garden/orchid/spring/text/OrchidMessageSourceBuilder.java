package se.softhouse.garden.orchid.spring.text;

/**
 * 
 * Copyright (c) 2011, Mikael Svahn, Softhouse Consulting AB
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
import org.springframework.context.MessageSourceResolvable;

import se.softhouse.garden.orchid.commons.text.OrchidMessageArgumentCode;
import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;
import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;

/**
 * This builder class is used to add code and arguments to the formatting
 * method.
 * 
 * @author Mikael Svahn
 */
public class OrchidMessageSourceBuilder implements MessageSourceResolvable {

	private final OrchidMessageCode code;
	private final String[] codes;
	private final OrchidMessageArguments args;

	/**
	 * The custructor which creates an instance and assign the code
	 * 
	 * @param code
	 *            The code to assign
	 */
	public OrchidMessageSourceBuilder(OrchidMessageCode code) {
		this.code = code;
		this.codes = new String[] { code.getRealName() };
		this.args = new OrchidMessageArguments();
	}

	/**
	 * A "copy" constructor which overrides the code
	 * 
	 * @param other
	 *            The builder to copy
	 * @param code
	 *            The code to use
	 */
	public OrchidMessageSourceBuilder(OrchidMessageSourceBuilder other, OrchidMessageCode code) {
		this.code = code;
		this.codes = new String[] { this.code.getRealName() };
		this.args = new OrchidMessageArguments(other.args);
	}

	/**
	 * Adds an argument to this builder
	 * 
	 * @param code
	 *            The code of the argument
	 * @param value
	 *            The value of the argument
	 * 
	 * @return This builder
	 */
	public OrchidMessageSourceBuilder arg(OrchidMessageArgumentCode code, Object value) {
		this.args.arg(code, value);
		return this;
	}

	/**
	 * Return the arguments
	 */
	public OrchidMessageArguments getArgs() {
		return this.args;
	}

	/**
	 * Set the code to use in the builder
	 */
	public OrchidMessageSourceBuilder code(OrchidMessageCode code) {
		return new OrchidMessageSourceBuilder(code);
	}

	/**
	 * Create a copy of this instance and assign the new code
	 * 
	 * @param code
	 *            The new code to assign
	 * 
	 * @return The created {@link OrchidMessageSourceBuilder}
	 */
	public OrchidMessageSourceBuilder copy(OrchidMessageCode code) {
		return new OrchidMessageSourceBuilder(this, code);
	}

	/**
	 * Return the assigned code
	 */
	public OrchidMessageCode getCode() {
		return this.code;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getCodes()
	 */
	@Override
	public String[] getCodes() {
		return this.codes;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.MessageSourceResolvable#getArguments()
	 */
	@Override
	public Object[] getArguments() {
		return new Object[] { this };
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.context.MessageSourceResolvable#getDefaultMessage()
	 */
	@Override
	public String getDefaultMessage() {
		return this.code.getPattern();
	}

}
