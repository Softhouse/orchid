package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;

public abstract class OrchidMessageStorageCachedProvider<T> extends OrchidMessageStorageProvider<T> {

	protected AtomicReference<OrchidMessageDirectoryStorageCache<T>> cachedMessages = new AtomicReference<OrchidMessageDirectoryStorageCache<T>>();
	protected AtomicLong lastCacheTime = new AtomicLong(0);

	protected Timer timer;

	protected int cacheMillis = -1;
	protected String charsetName = "UTF-8";

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
			OrchidMessageDirectoryStorageCache<T> cache = createMessageCache();
			cache.setCharsetName(this.charsetName);
			cache.setMessageFactory(getMessageFactory());
			cache.load();

			this.lastCacheTime.set(cacheTime);
			this.cachedMessages.set(cache);
		}
	}

	protected abstract OrchidMessageDirectoryStorageCache<T> createMessageCache();

	protected abstract long getLastModified();

}
