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
package se.softhouse.garden.orchid.commons.text.storage;

import java.io.IOException;
import java.util.Locale;

import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageProvider;

/**
 * An abstract message loader that loads strings from bare files in a directory
 * structure. The exact type that is returned is defined in the subclasses.
 * 
 * The content of the directory will be reread when the watch file have been
 * updated (default the content root dir). This is checked every cacheMillis.
 * 
 * This class is thread safe.
 * 
 * @author Mikael Svahn
 * 
 */
public abstract class OrchidMessageStorage<T> {

	private OrchidMessageStorageProvider<T> provider;

	/**
	 * The constructor which creates an empty cache
	 */
	public OrchidMessageStorage() {
	}

	/**
	 * The constructor which creates an empty cache
	 * 
	 * @param provider
	 *            The storage provider to user
	 */
	public OrchidMessageStorage(OrchidMessageStorageProvider<T> provider) {
		this.provider = provider;
	}

	/**
	 * Set the storage provider to use
	 */
	public void setProvider(OrchidMessageStorageProvider<T> provider) {
		this.provider = provider;
	}

	/**
	 * Set the storage provider to use
	 */
	public OrchidMessageStorageProvider<T> getProvider() {
		return this.provider;
	}

	/**
	 * Returns the content of the message with the specified code
	 */
	public T getMessage(String code) {
		return getMessage(code, Locale.getDefault());
	}

	/**
	 * Returns the content of the message with the specified code
	 */
	public T getMessage(OrchidMessageCode code) {
		return getMessage(code, Locale.getDefault());
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 */
	public T getMessage(String code, Locale locale) {
		return getMessageFromProvider(code, locale);
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 */
	public T getMessage(OrchidMessageCode code, Locale locale) {
		return getMessageFromProvider(code.getRealName(), locale);
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 * from the cache
	 */
	protected T getMessageFromProvider(String code, Locale locale) {
		return this.provider.getMessage(code, locale);
	}

	/**
	 * Inits and starts the connected storage provider.
	 */
	public void start() throws IOException {
		this.provider.setMessageFactory(createMessageFactory());
		this.provider.start();
	}

	/**
	 * Stops the connected storage provider
	 */
	public void stop() {
		this.provider.stop();
	}

	/**
	 * Create a message factory.
	 */
	protected abstract MessageFactory<T> createMessageFactory();

}
