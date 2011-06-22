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
package se.softhouse.garden.orchid.text;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import se.softhouse.garden.orchid.commons.text.OrchidDefaultMessageCode;
import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat;
import se.softhouse.garden.orchid.commons.text.OrchidMessageFormatFunction;

/**
 * Extends the {@link ReloadableResourceBundleMessageSource} with functionality
 * to read properties using named arguments with the {@link OrchidMessageFormat}
 * 
 * @author mis
 * 
 */
public class OrchidReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	@Override
	protected MessageFormat createMessageFormat(String msg, Locale locale) {
		OrchidMessageFormat message = new OrchidMessageFormat("", locale);
		if (msg != null) {
			message.applyPattern(msg);
		}
		return message;
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
				OrchidMessageSourceBuilder builder = (arg).copy(new OrchidDefaultMessageCode(((OrchidMessageFormatFunction) result[i]).getValue()));
				result[i] = getMessage(builder, locale);
			}
		}
		return result;
	}

}
