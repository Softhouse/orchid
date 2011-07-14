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
package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.File;
import java.io.IOException;

/**
 * An abstract message loader that loads strings from bare files in a directory
 * structure. The exact type that is returned is defined in the subclasses.
 * 
 * The content of the directory will be reread when the watch file have been
 * updated (default the content root dir). This is checked every cacheMillis.
 * 
 * This class is thread safe.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageDirectoryStorageProvider<T> extends OrchidMessageStorageCachedProvider<T> {

	private File root;
	private File watchFile;

	/**
	 * The constructor which creates an empty cache
	 */
	public OrchidMessageDirectoryStorageProvider() {
	}

	/**
	 * Creates an instance and loads the content from the specified root path.
	 * 
	 * @param root
	 *            The relative path
	 */
	public OrchidMessageDirectoryStorageProvider(String root) throws IOException {
		this();
		this.root = new File(root);
		this.watchFile = this.root;
	}

	/**
	 * Creates an instance and loads the content from the specified root path.
	 * 
	 * @param root
	 *            The relative path
	 */
	public OrchidMessageDirectoryStorageProvider(File root) throws IOException {
		this();
		this.root = root;
		this.watchFile = root;
	}

	/**
	 * Sets the path to root directory to read messages from
	 */
	public void setRoot(String root) {
		this.root = new File(root);
		if (this.watchFile == null) {
			this.watchFile = this.root;
		}
	}

	/**
	 * Sets the path to root directory to read messages from
	 */
	public void setRoot(File root) {
		this.root = root;
	}

	/**
	 * Sets the path to the watch file/directory check if the cache shall be
	 * reread.
	 */
	public void setWatchFile(String file) {
		this.watchFile = new File(file);
	}

	@Override
	protected OrchidMessageDirectoryStorageCache<T> createMessageCache() {
		OrchidMessageDirectoryStorageCache<T> cache = new OrchidMessageDirectoryStorageCache<T>();
		cache.setCharsetName(this.charsetName);
		cache.setMessageFactory(getMessageFactory());
		cache.setRootDir(this.root);
		return cache;
	}

	@Override
	protected long getLastModified() {
		if (this.watchFile != null) {
			return this.watchFile.lastModified();
		} else {
			return this.root.lastModified();
		}
	}

}
