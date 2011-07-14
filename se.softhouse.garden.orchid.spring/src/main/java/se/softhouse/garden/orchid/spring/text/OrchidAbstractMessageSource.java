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
package se.softhouse.garden.orchid.spring.text;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.support.AbstractMessageSource;

import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat;
import se.softhouse.garden.orchid.commons.text.storage.OrchidMessageFormatStorage;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageProvider;

/**
 * This is a MessageSource which uses a {@link OrchidMessageFormatStorage} to
 * load messages.
 * 
 * @author Mikael Svahn
 */
public class OrchidAbstractMessageSource extends AbstractMessageSource {

	protected OrchidMessageFormatStorage messageLoader;

	/**
	 * Constructor
	 */
	public OrchidAbstractMessageSource() {
		this.messageLoader = new OrchidMessageFormatStorage();
	}

	/**
	 * Constructor
	 */
	public OrchidAbstractMessageSource(OrchidMessageStorageProvider<OrchidMessageFormat> provider) {
		this.messageLoader = new OrchidMessageFormatStorage(provider);
	}

	/**
	 * Set the storage provider to use
	 */
	public void setProvider(OrchidMessageStorageProvider<OrchidMessageFormat> provider) {
		this.messageLoader.setProvider(provider);
	}

	/**
	 * Set the storage provider to use
	 */
	public OrchidMessageStorageProvider<OrchidMessageFormat> getProvider() {
		return this.messageLoader.getProvider();
	}

	/**
	 * Starts the watch thread that will reload the cache if the watch file/dir
	 * is changed.
	 */
	@PostConstruct
	public void start() throws IOException {
		this.messageLoader.start();
	}

	/**
	 * Starts the watch thread that will reload the cache if the watch file/dir
	 * is changed.
	 */
	@PreDestroy
	public void stop() {
		this.messageLoader.stop();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.context.support.AbstractMessageSource#resolveCode
	 * (java.lang.String, java.util.Locale)
	 */
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		return this.messageLoader.getMessage(code, locale);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.context.support.MessageSourceSupport#createMessageFormat
	 * (java.lang.String, java.util.Locale)
	 */
	@Override
	protected MessageFormat createMessageFormat(String msg, Locale locale) {
		return new OrchidMessageFormat((msg != null ? msg : ""), locale);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.context.support.AbstractMessageSource#getMessageInternal
	 * (java.lang.String, java.lang.Object[], java.util.Locale)
	 */
	@Override
	protected String getMessageInternal(String code, Object[] args, Locale locale) {
		if (args != null && args.length == 1) {
			if (args[0] instanceof OrchidMessageArguments) {
				OrchidMessageFormat resolveCode = (OrchidMessageFormat) resolveCode(code, locale);
				if (resolveCode == null) {
					return getMessageFromParent(code, args, locale);
				}
				return super.getMessageInternal(code, resolveArguments((OrchidMessageArguments) args[0], resolveCode, locale), locale);
			} else if (args[0] instanceof OrchidMessageSourceBuilder) {
				OrchidMessageFormat resolveCode = (OrchidMessageFormat) resolveCode(code, locale);
				if (resolveCode == null) {
					return getMessageFromParent(code, args, locale);
				}
				return super.getMessageInternal(code, resolveArguments((OrchidMessageSourceBuilder) args[0], resolveCode, locale), locale);
			}
		}
		return super.getMessageInternal(code, args, locale);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.support.AbstractMessageSource#
	 * getMessageFromParent(java.lang.String, java.lang.Object[],
	 * java.util.Locale)
	 */
	@Override
	protected String getMessageFromParent(String code, Object[] args, Locale locale) {
		String message = super.getMessageFromParent(code, args, locale);
		if (message == null && args != null && args.length == 1 && args[0] instanceof OrchidMessageSourceBuilder) {
			OrchidMessageFormat format = new OrchidMessageFormat(((OrchidMessageSourceBuilder) args[0]).getCode().getPattern(), locale);
			return format.format(resolveArguments((OrchidMessageSourceBuilder) args[0], format, locale));
		}
		return message;
	}

	/**
	 * Creates an array of arguments from a OrchidMessageArgument according to
	 * names used in the OrchidMessageFormat.
	 * 
	 * @param arg
	 *            The arguments to put into the array
	 * @param resolveCode
	 *            The OrchidMessageFormat to use
	 * @param locale
	 *            The locale t
	 * @return
	 */
	protected Object[] resolveArguments(OrchidMessageArguments arg, OrchidMessageFormat resolveCode, Locale locale) {
		return resolveCode.createArgsArray(arg);
	}

	protected Object[] resolveArguments(OrchidMessageSourceBuilder arg, OrchidMessageFormat resolveCode, Locale locale) {
		return resolveCode.createArgsArray(arg.getArgs());
	}

}
