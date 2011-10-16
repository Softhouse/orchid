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
import javax.servlet.jsp.JspException;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import se.softhouse.garden.orchid.commons.text.OrchidMessage;
import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;
import se.softhouse.garden.orchid.spring.text.OrchidMessageFormatLinkFunction;

import com.github.rjeschke.txtmark.Processor;

/**
 * This class makes it possible to get messages from the messageSource using the
 * $message variable as follows ${message['/a/b']['/c']}
 * 
 * @author Mikael Svahn
 * 
 */
public class MessageMap extends StringMap {

	private final MessageSource messageSource;
	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public MessageMap(String string, MessageSource messageSource, HttpServletRequest request, HttpServletResponse response) {
		super(string);
		this.messageSource = messageSource;
		this.request = request;
		this.response = response;
	}

	@Override
	public MessageMap get(Object link) {
		return new MessageMap(this.string + "." + link, this.messageSource, this.request, this.response);
	}

	@Override
	public String toString() {
		OrchidMessageArguments args = OrchidMessage.args();
		args.arg(OrchidMessageFormatLinkFunction.LINK_FUNC, new OrchidMessageFormatLinkFunction(this.request, this.response));
		String message = this.messageSource.getMessage(this.string, new Object[] { args }, this.string, LocaleContextHolder.getLocale());
		String type = this.messageSource.getMessage("+.type" + this.string, new Object[] {}, null, LocaleContextHolder.getLocale());
		return formatMessage(message, type);
	}

	/**
	 * Format the message before it is printed
	 * 
	 * @param message
	 *            The message to format
	 * @return The formatted message
	 * @throws JspException
	 */
	protected String formatMessage(String message, String type) {
		if (type != null) {
			if (type.equals("md")) {
				return Processor.process(message);
			}
		}
		return message;
	}
}
