/**
 * Copyright (c) 2012, Mikael Svahn, Softhouse Consulting AB
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
package se.softhouse.garden.orchid.vaadin;

import se.softhouse.garden.orchid.vaadin.widgetset.client.ui.VOrchidNumberField;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;

import de.essendi.vaadin.ui.component.numberfield.NumberField;

/**
 * This is an html5 extension of the NumberField addon. It enables to set the
 * type, pattern and step attributes. Provides a numeric field with automatic
 * keystroke filtering and validation for integer (123) and decimal numbers
 * (12.3). The minus sign and user-definable grouping and decimal separators are
 * supported.
 * 
 * @author Mikael Svahn
 */
@SuppressWarnings("serial")
@ClientWidget(value = VOrchidNumberField.class, loadStyle = LoadStyle.EAGER)
public class OrchidNumberField extends NumberField {

	private String inputType = "text";
	private String inputPattern = null;
	private String inputStep = null;

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);

		// Paint any component specific content by setting attributes
		// These attributes can be read in updateFromUIDL in the widget.
		target.addAttribute("type", this.inputType);
		if (this.inputPattern != null) {
			target.addAttribute("pattern", this.inputPattern);
		}
		if (this.inputStep != null) {
			target.addAttribute("step", this.inputStep);
		}
	}

	/**
	 * Return the type attribute of the input tag
	 */
	public String getInputType() {
		return this.inputType;
	}

	/**
	 * Set the type attribute of the input tag.
	 * 
	 * @param type
	 *            As defined in html
	 */
	public void setInputType(String type) {
		this.inputType = type;
	}

	/**
	 * Return the pattern attribute of the input tag
	 */
	public String getInputPattern() {
		return this.inputPattern;
	}

	/**
	 * Set the pattern attribute of the input tag.
	 * 
	 * @param pattern
	 *            As defined in html
	 */
	public void setInputPattern(String pattern) {
		this.inputPattern = pattern;
	}

	/**
	 * Return the step attribute of the input tag
	 */
	public String getInputStep() {
		return this.inputStep;
	}

	/**
	 * Set the step attribute of the input tag.
	 * 
	 * @param step
	 *            As defined in html
	 */
	public void setInputStep(String step) {
		this.inputStep = step;
	}

}
