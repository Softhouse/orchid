package se.softhouse.garden.orchid.spring.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.util.ObjectUtils;
import org.springframework.web.util.ExpressionEvaluationUtils;

@SuppressWarnings("serial")
public class OrchidArgTag extends BodyTagSupport {

	private String name;
	private String value;

	// tag lifecycle

	@Override
	public int doEndTag() throws JspException {
		Object argument = null;

		if (this.value != null) {
			argument = resolveArgument(this.value);
		} else if (getBodyContent() != null) {
			// get the value from the tag body
			argument = resolveArgument(getBodyContent().getString().trim());
		}

		// find a arg aware ancestor
		OrchidArgAware argAwareTag = (OrchidArgAware) findAncestorWithClass(this, OrchidArgAware.class);
		if (argAwareTag == null) {
			throw new JspException("The arg tag must be a descendant of a tag that supports arguments");
		}

		argAwareTag.addArg(this.name, argument);

		return EVAL_PAGE;
	}

	// tag attribute accessors

	/**
	 * Sets the name of the argument
	 * <p>
	 * Required
	 * 
	 * @param name
	 *            the parameter name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the value of the parameter
	 * <p>
	 * Optional. If not set, the tag's body content is evaluated
	 * 
	 * @param value
	 *            the parameter value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Resolve the given arguments Object into an arguments array.
	 * 
	 * @param arguments
	 *            the specified arguments Object
	 * @return the resolved arguments as array
	 * @throws JspException
	 *             if argument conversion failed
	 * @see #setArguments
	 */
	protected Object resolveArgument(String argument) throws JspException {
		Object resolvedArgument = ExpressionEvaluationUtils.evaluate("argument", argument, this.pageContext);
		if (resolvedArgument != null && resolvedArgument.getClass().isArray()) {
			return ObjectUtils.toObjectArray(argument);
		}
		return argument;
	}
}
