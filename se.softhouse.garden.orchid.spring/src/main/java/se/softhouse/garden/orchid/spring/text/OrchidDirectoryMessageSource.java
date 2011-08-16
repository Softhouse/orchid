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

import se.softhouse.garden.orchid.commons.text.OrchidMessageFormat;
import se.softhouse.garden.orchid.commons.text.storage.OrchidMessageFormatStorage;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageDirectoryStorageProvider;
import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCachedProvider;

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
	 * Sets the path to the directory to read messages from
	 * 
	 * @throws IOException
	 */
	public void setUrl(String spec) throws IOException {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setUrl(spec);
	}

	/**
	 * Sets the path to the directory to read messages from
	 * 
	 * @throws IOException
	 */
	public void setUrls(String[] specs) throws IOException {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setUrls(specs);
	}

	/**
	 * Sets the path to the watch file/directory check if the cache shall be
	 * reread.
	 * 
	 * @throws IOException
	 */
	public void setWatchURL(String spec) throws IOException {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setWatchUrl(spec);
	}

	/**
	 * Sets the time for how often the watch file/dir shall be checked.
	 * 
	 * @param cacheSeconds
	 *            The time in seconds
	 */
	public void setCacheSeconds(int cacheSeconds) {
		((OrchidMessageStorageCachedProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setCacheSeconds(cacheSeconds);
	}

	/**
	 * Sets the charset to use when reading the files
	 * 
	 * @param charsetName
	 *            The name of the {@linkplain java.nio.charset.Charset charset}.
	 */
	public void setCharsetName(String charsetName) {
		((OrchidMessageStorageCachedProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setCharsetName(charsetName);
	}

	/**
	 * Set the package level to skip in packages when mapping directories to
	 * message keys, eg. the path a/b/c/d.txt will be mapped to a.b.c.d with
	 * level=0 and c.d with level=2. All files will be read in the directory
	 * structure but if the level is to high so that the key will be empty, it
	 * will not be used.
	 */
	public void setPackageStartLevel(int packageStartLevel) {
		((OrchidMessageDirectoryStorageProvider<OrchidMessageFormat>) this.messageLoader.getProvider()).setPackageStartLevel(packageStartLevel);
	}

}
