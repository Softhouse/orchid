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

import java.util.Locale;

import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat.OrchidMessageFormatFunctionExecutorResolver;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunctionExecutor;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatLookup;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageProvider;

/**
 * A message loader that load OrchidMessageFormats from bare files in a
 * directory structure.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageFormatStorage extends OrchidMessageStorage<OrchidMessageFormat> implements OrchidMessageFormatLookup {

	/**
	 * The constructor which creates an empty cache
	 */
	public OrchidMessageFormatStorage() {
		super();
	}

	/**
	 * The constructor which creates an empty cache
	 * 
	 * @param provider
	 *            The storage provider to use
	 */
	public OrchidMessageFormatStorage(OrchidMessageStorageProvider<OrchidMessageFormat> provider) {
		super(provider);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageLoader
	 * #createMessage()
	 */
	@Override
	protected MessageFactory<OrchidMessageFormat> createMessageFactory() {
		return new MessageFactory<OrchidMessageFormat>() {

			@Override
			public OrchidMessageFormat createMessage(String message, Locale locale) {
				OrchidMessageFormat messageFormat = new OrchidMessageFormat("", locale);
				messageFormat.setOrchidMessageFormatLookup(OrchidMessageFormatStorage.this);
				messageFormat.setFunctionResolver(createFunctionResolvern());
				messageFormat.applyPattern(message);
				return messageFormat;
			}

			@Override
			public OrchidMessageFormat createMessage(OrchidMessageFormat message, Locale locale) {
				OrchidMessageFormat messageFormat = (OrchidMessageFormat) message.clone();
				messageFormat.setLocale(locale);
				return messageFormat;
			}

		};
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageLoader
	 * #getMessageFromCache(java.lang.String, java.util.Locale)
	 */
	@Override
	protected OrchidMessageFormat getMessageFromProvider(String code, Locale locale) {
		return super.getMessageFromProvider(code, locale);
	}

	/**
	 * Returns a "function" for resolving functions in a Message
	 * 
	 * @return The function resolver
	 */
	protected OrchidMessageFormatFunctionExecutorResolver createFunctionResolvern() {
		return new OrchidMessageFormatFunctionExecutorResolver() {
			@Override
			protected Object resolveFunction(String function, String value, OrchidMessageFormatLookup lookup) {
				return new OrchidMessageFormatFunctionExecutor(function, value, lookup);
			}
		};
	}
}
