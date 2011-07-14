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
import javax.servlet.jsp.JspException;

import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunction;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunctionExecutor;

/**
 * This function formats a link accodring to the current context
 * 
 * @author mis
 * 
 */
public class OrchidMessageFormatLinkFunction implements OrchidMessageFormatFunction {

	private static final String URL_TYPE_ABSOLUTE = "://";

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public OrchidMessageFormatLinkFunction(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public Object execute(OrchidMessageFormatFunctionExecutor executor, OrchidMessageArguments args, Locale locale) {
		return createUrl(executor.getValue());
	}

	/**
	 * Build the URL for the tag from the tag attributes and parameters.
	 * 
	 * @return the URL value as a String
	 * @throws JspException
	 */
	private String createUrl(String link) {
		StringBuilder url = new StringBuilder();
		UrlType type = getType(link);
		if (type == UrlType.CONTEXT_RELATIVE) {
			// add application context to url
			url.append(this.request.getContextPath());
		}
		if (type != UrlType.RELATIVE && type != UrlType.ABSOLUTE && !link.startsWith("/")) {
			url.append("/");
		}

		url.append(link);

		String urlStr = url.toString();
		if (type != UrlType.ABSOLUTE) {
			// Add the session identifier if needed
			// (Do not embed the session identifier in a remote link!)
			urlStr = this.response.encodeURL(urlStr);
		}

		return urlStr;
	}

	/**
	 * Sets the value of the URL
	 */
	public UrlType getType(String url) {
		if (url.contains(URL_TYPE_ABSOLUTE)) {
			return UrlType.ABSOLUTE;
		} else if (url.startsWith("/")) {
			return UrlType.CONTEXT_RELATIVE;
		} else {
			return UrlType.RELATIVE;
		}
	}

	/**
	 * Internal enum that classifies URLs by type.
	 */
	private enum UrlType {
		CONTEXT_RELATIVE, RELATIVE, ABSOLUTE
	}
}
