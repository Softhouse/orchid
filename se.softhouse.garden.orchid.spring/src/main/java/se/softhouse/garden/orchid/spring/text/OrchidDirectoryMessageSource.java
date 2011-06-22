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

import se.softhouse.garden.orchid.commons.text.OrchidDefaultMessageCode;
import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunction;
import se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageFormatLoader;

/**
 * This is a MessageSource which uses a
 * {@link OrchidDirectoryMessageFormatLoader} to load messages.
 * 
 * @author Mikael Svahn
 */
public class OrchidDirectoryMessageSource extends AbstractMessageSource {

	protected OrchidDirectoryMessageFormatLoader messageLoader;

	/**
	 * Constructor
	 */
	public OrchidDirectoryMessageSource() {
		this.messageLoader = new OrchidDirectoryMessageFormatLoader();
	}

	/**
	 * Sets the path to root directory to read messages from
	 */
	public void setRoot(String root) {
		this.messageLoader.setRoot(root);
	}

	/**
	 * Sets the path to the watch file/directory check if the cache shall be
	 * reread.
	 */
	public void setWatch(String watchFile) {
		this.messageLoader.setWatchFile(watchFile);
	}

	/**
	 * Sets the time for how often the watch file/dir shall be checked.
	 * 
	 * @param cacheSeconds
	 *            The time in seconds
	 */
	public void setCacheSeconds(int cacheSeconds) {
		this.messageLoader.setCacheSeconds(cacheSeconds);
	}

	/**
	 * Sets the charset to use when reading the files
	 * 
	 * @param charsetName
	 *            The name of the {@linkplain java.nio.charset.Charset charset}.
	 */
	public void setCharsetName(String charsetName) {
		this.messageLoader.setCharsetName(charsetName);
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

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		return this.messageLoader.getMessage(code, locale);
	}

	@Override
	protected MessageFormat createMessageFormat(String msg, Locale locale) {
		return new OrchidMessageFormat((msg != null ? msg : ""), locale);
	}

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

	@Override
	protected String getMessageFromParent(String code, Object[] args, Locale locale) {
		String message = super.getMessageFromParent(code, args, locale);
		if (message == null && args != null && args.length == 1 && args[0] instanceof OrchidMessageSourceBuilder) {
			OrchidMessageFormat format = new OrchidMessageFormat(((OrchidMessageSourceBuilder) args[0]).getCode().getPattern(), locale);
			return format.format(resolveArguments((OrchidMessageSourceBuilder) args[0], format, locale));
		}
		return message;
	}

	protected Object[] resolveArguments(OrchidMessageArguments arg, OrchidMessageFormat resolveCode, Locale locale) {
		Object[] result = resolveCode.createArgsArray(arg);
		for (int i = 0; i < result.length; i++) {
			if (result[i] instanceof OrchidMessageFormatFunction) {
				result[i] = getMessage(((OrchidMessageFormatFunction) result[i]).getValue(), new Object[] { arg }, locale);
			}
		}
		return result;
	}

	protected Object[] resolveArguments(OrchidMessageSourceBuilder arg, OrchidMessageFormat resolveCode, Locale locale) {
		Object[] result = resolveCode.createArgsArray(arg.getArgs());
		for (int i = 0; i < result.length; i++) {
			if (result[i] instanceof OrchidMessageFormatFunction) {
				OrchidMessageSourceBuilder builder = (arg).copy(new OrchidDefaultMessageCode(((OrchidMessageFormatFunction) result[i]).getFunction()));
				result[i] = getMessage(builder, locale);
			}
		}
		return result;
	}

}
