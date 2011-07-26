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

import java.io.IOException;
import java.util.Locale;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

/**
 * The abstract base class that a storage provider needs to extend from.
 * 
 * @author mis
 * 
 * @param <T>
 */
public abstract class OrchidMessageStorageProvider<T> {

	protected MessageFactory<T> messageFactory;

	/**
	 * Returns the content of the message with the specified code and locale
	 */
	public abstract T getMessage(String code, Locale locale);

	/**
	 * Inits and starts the connected storage provider.
	 */
	public abstract void start() throws IOException;

	/**
	 * Stops the connected storage provider
	 */
	public abstract void stop();

	/**
	 * Sets the message factory to use
	 */
	public void setMessageFactory(MessageFactory<T> messageFactory) {
		this.messageFactory = messageFactory;
	}

	/**
	 * Returns the current message factory
	 */
	public MessageFactory<T> getMessageFactory() {
		return this.messageFactory;
	}

	/**
	 * Create a package name by concat the pkg and the name.
	 */
	protected String getPackage(String pkg, String name) {
		return (pkg.length() > 0 ? pkg + "." : "") + name;

	}

}
