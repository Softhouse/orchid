package se.softhouse.garden.orchid.spring.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;

public class OrchidLocalizedMesageSource implements Serializable {

	private static final long serialVersionUID = 1L;
	private Locale locale = Locale.ENGLISH;
	transient private final List<OrchidLocalizedMesageListener> listeners = new ArrayList<OrchidLocalizedMesageListener>();

	@Resource
	MessageSource messageSource;

	public OrchidLocalizedMesageSource() {
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
		notifyListeners();
	}

	public Locale getLocale() {
		return this.locale;
	}

	public String get(MessageSourceResolvable resolvable) {
		return this.messageSource.getMessage(resolvable, this.locale);
	}

	public String get(OrchidMessageCode code) {
		return this.messageSource.getMessage(OrchidMessageSource.code(code), this.locale);
	}

	public String get(String code) {
		return this.messageSource.getMessage(OrchidMessageSource.code(code), this.locale);
	}

	public void addListener(OrchidLocalizedMesageListener listener) {
		this.listeners.add(listener);
		listener.localeChanged(this);
	}

	public void removeListener(OrchidLocalizedMesageListener listener) {
		this.listeners.remove(listener);
	}

	private void notifyListeners() {
		for (OrchidLocalizedMesageListener listener : this.listeners) {
			listener.localeChanged(this);
		}
	}
}
