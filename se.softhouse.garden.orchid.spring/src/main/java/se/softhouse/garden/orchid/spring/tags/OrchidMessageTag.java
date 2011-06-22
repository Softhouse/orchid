package se.softhouse.garden.orchid.spring.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;
import org.springframework.web.util.ExpressionEvaluationUtils;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;
import org.springframework.web.util.TagUtils;

import se.softhouse.garden.orchid.commons.text.OrchidMessage;
import se.softhouse.garden.orchid.commons.text.OrchidMessageArguments;

@SuppressWarnings("serial")
public class OrchidMessageTag extends HtmlEscapingAwareTag implements OrchidArgAware {

	private String code;
	private OrchidMessageArguments arguments;
	private String var;
	private String scope = TagUtils.SCOPE_PAGE;
	private boolean javaScriptEscape = false;

	@Override
	protected int doStartTagInternal() throws Exception {
		this.arguments = OrchidMessage.args();
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {

		// Resolve the unescaped message.
		String msg = resolveMessage();

		// HTML and/or JavaScript escape, if demanded.
		msg = isHtmlEscape() ? HtmlUtils.htmlEscape(msg) : msg;
		msg = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(msg) : msg;

		// Expose as variable, if demanded, else write to the page.
		String resolvedVar = ExpressionEvaluationUtils.evaluateString("var", this.var, this.pageContext);
		if (resolvedVar != null) {
			String resolvedScope = ExpressionEvaluationUtils.evaluateString("scope", this.scope, this.pageContext);
			this.pageContext.setAttribute(resolvedVar, msg, TagUtils.getScope(resolvedScope));
		} else {
			writeMessage(msg);
		}

		return EVAL_PAGE;
	}

	/**
	 * Set the message code for this tag.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Add an argument to the message
	 */
	@Override
	public void addArg(String name, Object value) {
		this.arguments.arg(name, value);

	}

	/**
	 * Set PageContext attribute name under which to expose a variable that
	 * contains the resolved message.
	 * 
	 * @see #setScope
	 * @see javax.servlet.jsp.PageContext#setAttribute
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * Set the scope to export the variable to. Default is SCOPE_PAGE ("page").
	 * 
	 * @see #setVar
	 * @see org.springframework.web.util.TagUtils#SCOPE_PAGE
	 * @see javax.servlet.jsp.PageContext#setAttribute
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Set JavaScript escaping for this tag, as boolean value. Default is
	 * "false".
	 */
	public void setJavaScriptEscape(String javaScriptEscape) throws JspException {
		this.javaScriptEscape = ExpressionEvaluationUtils.evaluateBoolean("javaScriptEscape", javaScriptEscape, this.pageContext);
	}

	/**
	 * Write the message to the page.
	 * <p>
	 * Can be overridden in subclasses, e.g. for testing purposes.
	 * 
	 * @param msg
	 *            the message to write
	 * @throws IOException
	 *             if writing failed
	 */
	protected void writeMessage(String msg) throws JspException {
		try {
			this.pageContext.getOut().print(String.valueOf(msg));
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	/**
	 * Use the current RequestContext's application context as MessageSource.
	 */
	protected MessageSource getMessageSource() {
		return getRequestContext().getMessageSource();
	}

	/**
	 * Return default exception message.
	 */
	protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex) {
		return ex.getMessage();
	}

	/**
	 * Resolve the specified message into a concrete message String. The
	 * returned message String should be unescaped.
	 */
	protected String resolveMessage() throws JspException, NoSuchMessageException {
		MessageSource messageSource = getMessageSource();
		if (messageSource == null) {
			throw new JspTagException("No corresponding MessageSource found");
		}

		String resolvedCode = ExpressionEvaluationUtils.evaluateString("code", this.code, this.pageContext);

		if (resolvedCode != null) {
			// We have a code or default text that we need to resolve.
			Object[] argumentsArray = new Object[] { this.arguments };
			return messageSource.getMessage(resolvedCode, argumentsArray, getRequestContext().getLocale());
		}

		// All we have is a specified literal text.
		return this.code;
	}
}
