package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.IOException;
import java.util.Locale;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

public abstract class OrchidMessageStorageProvider<T> {

	protected MessageFactory<T> messageFactory;

	/**
	 * Returns the content of the message with the specified code and locale
	 */
	public abstract T getMessage(String code, Locale locale);

	/**
	 * Inits and starts the connected storage provider.
	 */
	public abstract void start() throws IOException;

	/**
	 * Stops the connected storage provider
	 */
	public abstract void stop();

	/**
	 * Sets the message factory to use
	 */
	public void setMessageFactory(MessageFactory<T> messageFactory) {
		this.messageFactory = messageFactory;
	}

	/**
	 * Returns the current message factory
	 */
	public MessageFactory<T> getMessageFactory() {
		return this.messageFactory;
	}
}
