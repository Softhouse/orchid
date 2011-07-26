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

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
 * An abstract message loader that loads strings from bare files in a directory
 * structure. The exact type that is returned is defined in the subclasses.
 * 
 * The file names have the following format
 * key_[language]_[country]_[variant].[ext]
 * 
 * If the [ext] equals properties the files is read as a property and insert
 * into the property tree. Note that the location and name of the property file
 * will be inserted into the message key.
 * 
 * The getMessage operations returns the found content or if a none leaf node is
 * queried the children of that node is returned.
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

	private static final Pattern FILE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)\\.?(.*)");
	private static final Pattern LOCALE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)_?([^.]*)");

	private OrchidMessageResource root;
	private OrchidMessageResource watchFile;

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
		this.root = new OrchidMessageFileResource(new File(root));
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
		this.root = new OrchidMessageFileResource(root);
		this.watchFile = new OrchidMessageFileResource(root);
	}

	/**
	 * Sets the path to root directory to read messages from
	 */
	public void setRoot(String root) {
		this.root = new OrchidMessageFileResource(new File(root));
		if (this.watchFile == null) {
			this.watchFile = this.root;
		}
	}

	/**
	 * Sets the path to root directory to read messages from
	 */
	public void setRoot(File root) {
		this.root = new OrchidMessageFileResource(root);
	}

	/**
	 * Sets the path to the watch file/directory check if the cache shall be
	 * reread.
	 */
	public void setWatchFile(String file) {
		this.watchFile = new OrchidMessageFileResource(new File(file));
	}

	/*
	 * (non-Javadoc)
	 * @see se.softhouse.garden.orchid.commons.text.storage.provider.
	 * OrchidMessageStorageCachedProvider#createMessageCache()
	 */
	@Override
	protected OrchidMessageStorageCache<T> createMessageCache() {
		OrchidMessageStorageCache<T> cache = new OrchidMessageStorageCache<T>();
		cache.setMessageFactory(getMessageFactory());
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * @see se.softhouse.garden.orchid.commons.text.storage.provider.
	 * OrchidMessageStorageCachedProvider#getLastModified()
	 */
	@Override
	protected long getLastModified() {
		if (this.watchFile != null) {
			return this.watchFile.getLastModified();
		} else {
			return this.root.getLastModified();
		}
	}

	@Override
	protected void loadAllMessages(OrchidMessageStorageCache<T> cache, String pkg) throws IOException {
		loadAllMessages(cache, pkg, this.root);
	}

	/**
	 * Load all messages in the specified dir and sub dirs into the package.
	 * 
	 * @param pkg
	 *            The prefix to add to the code
	 * @param dir
	 *            The dir to read
	 * @throws IOException
	 */
	protected void loadAllMessages(OrchidMessageStorageCache<T> cache, String pkg, OrchidMessageResource dir) throws IOException {
		ArrayList<OrchidMessageResource> dirs = new ArrayList<OrchidMessageResource>();
		OrchidMessageResource[] listFiles = dir.list();
		if (listFiles != null) {
			for (OrchidMessageResource file : listFiles) {
				if (file.isFile()) {
					loadMessageFromFile(cache, pkg, file);
				} else if (file.isDirectory()) {
					dirs.add(file);
				}
			}
		}
		for (OrchidMessageResource file : dirs) {
			loadAllMessages(cache, getPackage(pkg, file.getName()), file);
		}
	}

	/**
	 * Load a message from the specified file into the package.
	 * 
	 * @param pkg
	 *            The prefix to add to the code
	 * @param file
	 *            The file to read
	 * @throws IOException
	 */
	protected void loadMessageFromFile(OrchidMessageStorageCache<T> cache, String pkg, OrchidMessageResource file) throws IOException {
		Matcher matcher = FILE_PATTERN.matcher(file.getName());
		if (matcher.matches()) {
			String code = matcher.group(1);
			String localeCode = matcher.group(2).toLowerCase();
			String ext = matcher.group(3);

			Locale locale = Locale.getDefault();
			Matcher localeMatcher = LOCALE_PATTERN.matcher(localeCode);
			if (localeMatcher.matches()) {
				locale = new Locale(localeMatcher.group(1), localeMatcher.group(2), localeMatcher.group(3));
			}

			if ("properties".equals(ext)) {
				loadMessageFromPropertyFile(cache, getPackage(pkg, code), file, localeCode, locale);
			} else if ("zip".equals(ext)) {
				loadAllMessagesFromZipFile(cache, getPackage(pkg, code), file, localeCode, locale);
			} else {
				String fileAsString = readFileAsString(file);
				cache.addToCache(getPackage(pkg, code), localeCode, this.messageFactory.createMessage(fileAsString, locale));
			}
		}
	}

	/**
	 * Reads the specified file and returns its content as a string.
	 * 
	 * @param file
	 *            The file to read
	 * 
	 * @return The read string
	 * @throws IOException
	 */
	protected String readFileAsString(OrchidMessageResource file) throws IOException {
		DataInputStream dis = new DataInputStream(file.getInputStream());
		try {
			long len = file.getLength();
			if (len > Integer.MAX_VALUE) {
				throw new IOException("File " + file + " too large, was " + len + " bytes.");
			}
			byte[] bytes = new byte[(int) len];
			dis.readFully(bytes);
			return new String(bytes, this.charsetName);
		} finally {
			dis.close();
		}
	}

	/**
	 * Load all messages from the specified property file into the package.
	 * 
	 * @param pkg
	 *            The prefix to add to the code
	 * @param file
	 *            The file to read
	 * @param localeCode
	 *            The locale to load the messages into
	 * @throws IOException
	 */
	protected void loadMessageFromPropertyFile(OrchidMessageStorageCache<T> cache, String pkg, OrchidMessageResource file, String localeCode, Locale locale)
	        throws IOException {
		Properties props = new Properties();
		InputStream in = file.getInputStream();
		props.load(in);
		in.close();
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			cache.addToCache(getPackage(pkg, (String) entry.getKey()), localeCode, this.messageFactory.createMessage((String) entry.getValue(), locale));
		}
	}

	/**
	 * Load all messages from the specified zip file into the package.
	 * 
	 * @param pkg
	 *            The prefix to add to the code
	 * @param file
	 *            The file to read
	 * @param localeCode
	 *            The locale to load the messages into
	 * @throws IOException
	 */
	protected void loadAllMessagesFromZipFile(OrchidMessageStorageCache<T> cache, String pkg, OrchidMessageResource file, String localeCode, Locale locale)
	        throws IOException {
		ZipFile zipFile = new ZipFile(file.getFile());
		OrchidMessageZipResource zip = new OrchidMessageZipResource(zipFile, null);
		loadAllMessages(cache, pkg, zip);
	}
}
