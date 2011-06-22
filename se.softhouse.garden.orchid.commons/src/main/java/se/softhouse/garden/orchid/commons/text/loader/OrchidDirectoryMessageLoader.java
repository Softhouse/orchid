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
package se.softhouse.garden.orchid.commons.text.loader;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;
import se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageCache.MessageFactory;

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
public abstract class OrchidDirectoryMessageLoader<T> {

	private OrchidDirectoryMessageCache<T> cachedMessages;
	private long lastCacheTime = 0;
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	private Timer timer;

	private File root;
	private File watchFile;
	private int cacheMillis = -1;
	private String charsetName = "UTF-8";

	/**
	 * The constructor which creates an empty cache
	 */
	public OrchidDirectoryMessageLoader() {
		this.cachedMessages = new OrchidDirectoryMessageCache<T>();
	}

	/**
	 * Creates an instance and loads the content from the specified root path.
	 * 
	 * @param root
	 *            The relative path
	 */
	public OrchidDirectoryMessageLoader(String root) {
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
	public OrchidDirectoryMessageLoader(File root) {
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
	public void start() throws IOException {
		reload(true);
		startTimer();
	}

	/**
	 * Stops the watch thread.
	 */
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
		this.rwl.readLock().lock();
		try {
			return this.cachedMessages.getMessage(code, locale);
		} finally {
			this.rwl.readLock().unlock();
		}
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
		this.rwl.readLock().lock();
		try {
			long cacheTime = this.watchFile.lastModified();
			if (force || this.lastCacheTime < cacheTime) {
				OrchidDirectoryMessageCache<T> cache = new OrchidDirectoryMessageCache<T>();
				cache.setCharsetName(this.charsetName);
				cache.setMessageFactory(createMessage());
				cache.load(this.root);

				this.rwl.readLock().unlock();
				this.rwl.writeLock().lock();

				try {
					this.lastCacheTime = cacheTime;
					this.cachedMessages = cache;
				} finally {
					this.rwl.readLock().lock();
					this.rwl.writeLock().unlock();
				}
			}
		} finally {
			this.rwl.readLock().unlock();
		}
	}

	/**
	 * Create a message factory.
	 */
	protected abstract MessageFactory<T> createMessage();

}
