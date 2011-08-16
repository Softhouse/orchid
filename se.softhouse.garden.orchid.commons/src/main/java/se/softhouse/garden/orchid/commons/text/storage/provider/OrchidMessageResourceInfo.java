/**
 * 
 */
package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.util.Locale;

/**
 * @author mis
 * 
 */
public class OrchidMessageResourceInfo {

	private final String code;
	private final String localeCode;
	private final String ext;
	private final Locale locale;

	/**
     * 
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
	 * @return the ext
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
