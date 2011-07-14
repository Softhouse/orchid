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

import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat;
import se.softhouse.garden.orchid.commons.text.storage.OrchidMessageFormatStorage;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageDirectoryStorageProvider;

/**
 * This is a MessageSource which uses a {@link OrchidMessageFormatStorage} to
 * load messages.
 * 
 * @author Mikael Svahn
 */
public class OrchidDirectoryMessageSource extends OrchidAbstractMessageSource {

	/**
	 * Constructor
	 */
	public OrchidDirectoryMessageSource() {
		this.messageLoader = new OrchidMessageFormatStorage(new OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>());
	}

	/**
	 * Sets the path to root directory to read messages from
	 */
	public void setRoot(String root) {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setRoot(root);
	}

	/**
	 * Sets the path to the watch file/directory check if the cache shall be
	 * reread.
	 */
	public void setWatch(String watchFile) {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setWatchFile(watchFile);
	}

	/**
	 * Sets the time for how often the watch file/dir shall be checked.
	 * 
	 * @param cacheSeconds
	 *            The time in seconds
	 */
	public void setCacheSeconds(int cacheSeconds) {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setCacheSeconds(cacheSeconds);
	}

	/**
	 * Sets the charset to use when reading the files
	 * 
	 * @param charsetName
	 *            The name of the {@linkplain java.nio.charset.Charset charset}.
	 */
	public void setCharsetName(String charsetName) {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setCharsetName(charsetName);
	}

}
