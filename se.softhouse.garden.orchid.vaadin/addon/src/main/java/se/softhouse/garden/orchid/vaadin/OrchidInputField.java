package se.softhouse.garden.orchid.vaadin;

import se.softhouse.garden.orchid.vaadin.widgetset.client.ui.VOrchidInputField;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.ClientWidget.LoadStyle;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
@ClientWidget(value = VOrchidInputField.class, loadStyle = LoadStyle.EAGER)
public class OrchidInputField extends TextField {

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

	public String getInputType() {
		return this.inputType;
	}

	public void setInputType(String type) {
		this.inputType = type;
	}

	public String getInputPattern() {
		return this.inputPattern;
	}

	public void setInputPattern(String pattern) {
		this.inputPattern = pattern;
	}

	public String getInputStep() {
		return this.inputStep;
	}

	public void setInputStep(String step) {
		this.inputStep = step;
	}

}
