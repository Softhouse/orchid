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

package se.softhouse.garden.orchid.spring.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class makes it possible to get messages from the messageSource using the
 * $message variable as follows ${message['/a/b']['/c']}
 * 
 * @author Mikael Svahn
 * 
 */
public class UrlMap extends StringMap {

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public UrlMap(String string, HttpServletRequest request, HttpServletResponse response) {
		super(string);
		this.request = request;
		this.response = response;
	}

	@Override
	public UrlMap get(Object link) {
		return new UrlMap(this.string + link, this.request, this.response);
	}

	@Override
	public String toString() {
		return LinkUtil.createUrl(this.string, this.request, this.response);
	}
}
