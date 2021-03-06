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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A message cache that loads strings from bare files in a directory structure.
 * The file names have the following format
 * key_[language]_[country]_[variant].[ext]
 * 
 * If the [ext] equals properties the files is read as a property and insert
 * into the property tree. Note that the location and name of the property file
 * will be inserted into the message key.
 * 
 * The getMessage operations returns the found content or if a none leaf node is
 * queried the children of that node is returned.
 * 
 * This class is not thread safe.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageStorageCache<T> {

	protected OrchidMessageStorageCacheTree cachedTree;
	protected Map<String, Map<String, T>> cachedMessages;
	protected MessageFactory<T> messageFactory;
	protected int packageStartLevel;

	/**
	 * Creates a cache instance
	 */
	public OrchidMessageStorageCache() {
		this.cachedMessages = new HashMap<String, Map<String, T>>();
		this.cachedTree = new OrchidMessageStorageCacheTree();
	}

	/**
	 * Sets the message factory to use
	 */
	public void setMessageFactory(MessageFactory<T> messageFactory) {
		this.messageFactory = messageFactory;
	}

	/**
	 * Set the package start level
	 */
	public void setPackageStartLevel(int packageStartLevel) {
		this.packageStartLevel = packageStartLevel;
	}

	/**
	 * Returns the current message factory
	 */
	public MessageFactory<T> getMessageFactory() {
		return this.messageFactory;
	}

	/**
	 * Clears the cache, nothing new is read
	 */
	public void clear() {
		this.cachedMessages.clear();
	}

	/**
	 * Returns the content of the message with the specified code.
	 * 
	 * @param code
	 *            A case sensitive key for the message
	 * @return The found message or null if none was found
	 */
	public T getMessage(String code) {
		return getMessage(code, Locale.getDefault());
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 * 
	 * @param code
	 *            A case sensitive key for the message
	 * @param locale
	 *            The locale to look for.
	 * 
	 * @return The found message or null if none was found
	 */
	public T getMessage(String code, Locale locale) {
		return getMessageFromCache(code, locale);
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 * 
	 * @param code
	 *            A case sensitive key for the message
	 * @param locale
	 *            The locale to look for.
	 * 
	 * @return The found message or null if none was found
	 */
	protected T getMessageFromCache(String code, Locale locale) {
		List<String> localeKeys = calculateLocaleKeys(locale);
		for (String localeKey : localeKeys) {
			Map<String, T> map = this.cachedMessages.get(localeKey);
			if (map != null) {
				T message = map.get(code);
				if (message != null) {
					return this.messageFactory.createMessage(message, locale);
				}
			}
		}
		String list = this.cachedTree.getMessage(code, locale);
		if (list != null) {
			return this.messageFactory.createMessage(list, locale);
		}
		return null;
	}

	/**
	 * Add the specified message to the cache,
	 * 
	 * @param list
	 *            The code of the message
	 * @param locale
	 *            The locale of the message
	 * @param message
	 *            The content of the message
	 * 
	 * @return The message
	 */
	protected T addToCache(List<String> prePkgs, List<String> pkgs, String locale, T message) {
		String c = formatCode(prePkgs, pkgs);
		if (c != null) {
			Map<String, T> map = this.cachedMessages.get(locale);
			if (map == null) {
				map = new HashMap<String, T>();
				this.cachedMessages.put(locale, map);
			}
			map.put(c, message);
			this.cachedTree.addMessage(c, locale);
		}
		return message;
	}

	/**
	 * Calculates the locale keys accoring to the following prio list.
	 * 
	 * 1. language_country_variant <br>
	 * 2. language_country <br>
	 * 3. language <br>
	 * 4. language__variant <br>
	 * 5. _country_variant <br>
	 * 6. _country <br>
	 * 7. __variant <br>
	 * 8. <br>
	 * 
	 * @param locale
	 * @return
	 */
	protected List<String> calculateLocaleKeys(Locale locale) {
		List<String> result = new ArrayList<String>(4);
		String language = locale.getLanguage().toLowerCase();
		String country = locale.getCountry().toLowerCase();
		String variant = locale.getVariant().toLowerCase();
		boolean hasLanguage = language.length() > 0;
		boolean hasCountry = country.length() > 0;
		boolean hasVariant = variant.length() > 0;

		if (hasLanguage && hasCountry && hasVariant) {
			result.add(new StringBuilder().append(language).append("_").append(country).append("_").append(variant).toString());
		}
		if (hasLanguage && hasCountry) {
			result.add(new StringBuilder().append(language).append("_").append(country).toString());
		}
		if (hasLanguage && hasVariant) {
			result.add(new StringBuilder().append(language).append("__").append(variant).toString());
		}
		if (hasLanguage) {
			result.add(new StringBuilder().append(language).toString());
		}
		if (hasCountry && hasVariant) {
			result.add(new StringBuilder().append("_").append(country).append("_").append(variant).toString());
		}
		if (hasCountry) {
			result.add(new StringBuilder().append("_").append(country).toString());
		}
		if (hasVariant) {
			result.add(new StringBuilder().append("__").append(variant).toString());
		}
		result.add("");

		return result;
	}

	/**
	 * Creates a code key from the list of packages by separating them with a
	 * dot.
	 * 
	 * @param pkgs
	 *            The list of packages
	 * @param pkgs2
	 * 
	 * @return A dot separated string of the list
	 */
	protected String formatCode(List<String> prePkgs, List<String> pkgs) {
		StringBuilder b = new StringBuilder();
		formatCode(0, prePkgs, b);
		formatCode(this.packageStartLevel, pkgs, b);
		if (b.length() > 0) {
			return b.toString();
		}
		return null;
	}

	private void formatCode(int start, List<String> pkgs, StringBuilder b) {
		for (int i = start; i < pkgs.size(); i++) {
			if (b.length() > 0) {
				b.append('.');
			}
			b.append(pkgs.get(i));
		}
	}

	/**
	 * This factory class will create a message of type T from a string.
	 */
	public abstract static class MessageFactory<T> {
		/**
		 * Create a T from a String
		 * 
		 * @param message
		 *            The string
		 * @param locale
		 *            The current locale
		 * 
		 * @return The created T
		 */
		public abstract T createMessage(String message, Locale local);

		/**
		 * Create a T from a T. The same instance can be returned if it is
		 * thread safe.
		 * 
		 * @param message
		 *            The message to copy if needed
		 * @param locale
		 *            The current locale
		 * 
		 * @return The created T
		 */
		public abstract T createMessage(T message, Locale local);
	}

}
