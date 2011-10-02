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

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunction;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunctionExecutor;
import se.softhouse.garden.orchid.spring.utils.LinkUtil;

/**
 * This function formats a link accodring to the current context
 * 
 * @author mis
 * 
 */
public class OrchidMessageFormatLinkFunction implements OrchidMessageFormatFunction {

	public static final String LINK_FUNC = OrchidMessageFormatFunction.ORCHID_FUNC + "link";

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public OrchidMessageFormatLinkFunction(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public Object execute(OrchidMessageFormatFunctionExecutor executor, OrchidMessageArguments args, Locale locale) {
		return LinkUtil.createUrl(executor.getValue(), this.request, this.response);
	}
}
