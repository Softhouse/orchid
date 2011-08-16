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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;

/**
 * An abstract implementation of a cached storage provider
 * 
 * @author mis
 * 
 * @param <T>
 */
public abstract class OrchidMessageStorageCachedProvider<T> extends OrchidMessageStorageProvider<T> {

	protected AtomicReference<OrchidMessageStorageCache<T>> cachedMessages = new AtomicReference<OrchidMessageStorageCache<T>>();
	protected AtomicLong lastCacheTime = new AtomicLong(0);

	protected Timer timer;

	protected int cacheMillis = -1;
	protected String charsetName = "UTF-8";
	protected int packageStartLevel = 1;

	/**
	 * The constructor which creates an empty cache
	 * 
	 * @throws IOException
	 */
	public OrchidMessageStorageCachedProvider() {
		this.cachedMessages.set(createMessageCache());
	}

	/**
	 * Sets the charset to use when reading the files
	 * 
	 * @param charsetName
	 *            The name of the {@linkplain java.nio.charset.Charset charset}.
	 */
	public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	/**
	 * Returns the used charset.
	 */
	public String getCharsetName() {
		return this.charsetName;
	}

	/**
	 * Sets the time for how often the watch file/dir shall be checked.
	 * 
	 * @param cacheSeconds
	 *            The time in seconds
	 */
	public void setCacheSeconds(int cacheSeconds) {
		this.cacheMillis = (cacheSeconds * 1000);
	}

	/**
	 * Sets the time for how often the watch file/dir shall be checked.
	 * 
	 * @param cacheMillis
	 *            The time in milliseconds
	 */
	public void setCacheMillis(int cacheMillis) {
		this.cacheMillis = cacheMillis;
	}

	/**
	 * Returns the minimum cache time in seconds
	 */
	public int getCacheSeconds() {
		return this.cacheMillis / 1000;
	}

	/**
	 * Set the package level to skip in packages when mapping directories to
	 * message keys, eg. the path a/b/c/d.txt will be mapped to a.b.c.d with
	 * level=0 and c.d with level=2. All files will be read in the directory
	 * structure but if the level is to high so that the key will be empty, it
	 * will not be used.
	 */
	public void setPackageStartLevel(int packageStartLevel) {
		this.packageStartLevel = packageStartLevel;
	}

	/**
	 * Returns the content of the message with the specified code
	 */
	public T getMessage(String code) {
		return getMessage(code, Locale.getDefault());
	}

	/**
	 * Returns the content of the message with the specified code
	 */
	public T getMessage(OrchidMessageCode code) {
		return getMessage(code, Locale.getDefault());
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 */
	@Override
	public T getMessage(String code, Locale locale) {
		return getMessageFromCache(code, locale);
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 */
	public T getMessage(OrchidMessageCode code, Locale locale) {
		return getMessageFromCache(code.getRealName(), locale);
	}

	/**
	 * Starts the watch thread that will reload the cache if the watch file/dir
	 * is changed.
	 */
	@Override
	public void start() throws IOException {
		reload(true);
		startTimer();
	}

	/**
	 * Stops the watch thread.
	 */
	@Override
	public void stop() {
		if (this.timer != null) {
			this.timer.cancel();
		}
	}

	/**
	 * Returns the content of the message with the specified code and locale
	 * from the cache
	 */
	protected T getMessageFromCache(String code, Locale locale) {
		return this.cachedMessages.get().getMessage(code, locale);
	}

	/**
	 * Start the watch timer
	 */
	protected void startTimer() {
		if (this.cacheMillis > 0) {
			this.timer = new Timer(true);
			this.timer.schedule(timerTask(), this.cacheMillis, this.cacheMillis);
		}
	}

	/**
	 * Create a time task to call by the scheduler
	 */
	protected TimerTask timerTask() {
		return new TimerTask() {

			@Override
			public void run() {
				try {
					reload(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * Reloads the content of the cache.
	 * 
	 * @param force
	 *            Force a reload although the watch file/dir is changed or not.
	 * 
	 * @throws IOException
	 */
	public void reload(boolean force) throws IOException {
		long cacheTime = getLastModified();
		if (force || this.lastCacheTime.get() < cacheTime) {
			OrchidMessageStorageCache<T> cache = createMessageCache();
			cache.setMessageFactory(getMessageFactory());
			loadAllMessages(cache, new ArrayList<String>());

			this.lastCacheTime.set(cacheTime);
			this.cachedMessages.set(cache);
		}
	}

	/**
	 * Creates a new message cache
	 */
	protected abstract OrchidMessageStorageCache<T> createMessageCache();

	/**
	 * Return the timestamp when the storage was last updated
	 */
	protected abstract long getLastModified() throws IOException;

	/**
	 * Load all messages from the store into the package.
	 * 
	 * @param pkgs
	 *            The prefix to add to the code
	 * @throws IOException
	 */
	protected abstract void loadAllMessages(OrchidMessageStorageCache<T> cache, List<String> pkgs) throws IOException;

}
