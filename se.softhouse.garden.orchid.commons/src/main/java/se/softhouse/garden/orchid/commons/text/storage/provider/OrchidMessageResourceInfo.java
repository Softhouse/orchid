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
package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.util.Locale;

/**
 * A data class for holding information about a resource
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageResourceInfo {

	private final String code;
	private final String localeCode;
	private final String ext;
	private final Locale locale;

	/**
	 * Constructor
	 * 
	 * @param code
	 *            The code of the message
	 * @param localeCode
	 *            The locale code
	 * @param ext
	 *            The extension of the file
	 * @param locale
	 *            The locale of the file
	 */
	public OrchidMessageResourceInfo(String code, String localeCode, String ext, Locale locale) {
		this.code = code;
		this.localeCode = localeCode;
		this.ext = ext;
		this.locale = locale;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return the localeCode
	 */
	public String getLocaleCode() {
		return this.localeCode;
	}

	/**
	 * @return the extension
	 */
	public String getExt() {
		return this.ext;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

}
