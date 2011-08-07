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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	protected static final Pattern FILE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)\\.?(.*)");
	protected static final Pattern LOCALE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)_?([^.]*)");

	protected OrchidMessageResource[] dirs;
	protected OrchidMessageResource watchFile;
	private int packageStartLevel = 0;

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
		this.dirs = new OrchidMessageResource[] { new OrchidMessageFileResource(new File(root)) };
		this.watchFile = this.dirs[0];
	}

	/**
	 * Sets the path to the directory to read messages from
	 */
	public void setDir(String dir) {
		this.dirs = new OrchidMessageResource[] { new OrchidMessageFileResource(new File(dir)) };
		if (this.watchFile == null) {
			this.watchFile = this.dirs[0];
		}
	}

	/**
	 * Sets the paths to the directories to read messages from
	 */
	public void setDirs(String[] dirs) {
		this.dirs = new OrchidMessageResource[dirs.length];
		for (int i = 0; i < dirs.length; i++) {
			this.dirs[i] = new OrchidMessageFileResource(new File(dirs[i]));
		}
		if (this.watchFile == null && this.dirs.length > 0) {
			this.watchFile = this.dirs[0];
		}
	}

	/**
	 * Sets the path to the watch file/directory check if the cache shall be
	 * reread.
	 */
	public void setWatchFile(String file) {
		this.watchFile = new OrchidMessageFileResource(new File(file));
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
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see se.softhouse.garden.orchid.commons.text.storage.provider.
	 * OrchidMessageStorageCachedProvider
	 * #loadAllMessages(se.softhouse.garden.orchid
	 * .commons.text.storage.provider.OrchidMessageStorageCache,
	 * java.lang.String)
	 */
	@Override
	protected void loadAllMessages(OrchidMessageStorageCache<T> cache, List<String> pkg) throws IOException {
		for (OrchidMessageResource dir : this.dirs) {
			loadMessages(cache, pkg, dir);
		}
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
	protected void loadMessages(OrchidMessageStorageCache<T> cache, List<String> pkgs, OrchidMessageResource dir) throws IOException {
		ArrayList<OrchidMessageResource> dirs = new ArrayList<OrchidMessageResource>();
		OrchidMessageResource[] listFiles = dir.list();
		if (listFiles != null) {
			for (OrchidMessageResource file : listFiles) {
				if (file.isFile()) {
					loadMessagesFromFile(cache, pkgs, file);
				} else if (file.isDirectory()) {
					dirs.add(file);
				}
			}
		}
		for (OrchidMessageResource file : dirs) {
			loadMessages(cache, createPackageList(pkgs, file.getName()), file);
		}
	}

	/**
	 * Create a new package list by copying the pkgs list and adding the code.
	 * 
	 * @param pkgs
	 *            The current list to copy
	 * @param code
	 *            The code to add to the end of the list
	 * @return The newly created list
	 */
	protected List<String> createPackageList(List<String> pkgs, String code) {
		List<String> pkg = new ArrayList<String>(pkgs);
		pkg.add(code);
		return pkg;
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
	protected void loadMessagesFromFile(OrchidMessageStorageCache<T> cache, List<String> pkgs, OrchidMessageResource file) throws IOException {
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
				loadMessagesFromPropertyFile(cache, createPackageList(pkgs, code), file, localeCode, locale);
			} else if ("zip".equals(ext)) {
				loadMessagesFromZipFile(cache, createPackageList(pkgs, code), file, localeCode, locale);
			} else {
				String fileAsString = readFileAsString(file);
				String c = formatCode(createPackageList(pkgs, code));
				if (c != null) {
					cache.addToCache(c, localeCode, this.messageFactory.createMessage(fileAsString, locale));
				}
			}
		}
	}

	protected String formatCode(List<String> pkgs) {
		StringBuilder b = new StringBuilder();
		for (int i = this.packageStartLevel; i < pkgs.size(); i++) {
			if (b.length() > 0) {
				b.append('.');
			}
			b.append(pkgs.get(i));
		}
		if (b.length() > 0) {
			return b.toString();
		} else {
			return null;
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

		char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();
		InputStreamReader in = new InputStreamReader(file.getInputStream());
		int read;
		try {
			do {
				read = in.read(buffer, 0, buffer.length);
				if (read > 0) {
					out.append(buffer, 0, read);
				}
			} while (read >= 0);
		} finally {
			in.close();
		}
		return out.toString();
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
	protected void loadMessagesFromPropertyFile(OrchidMessageStorageCache<T> cache, List<String> pkg, OrchidMessageResource file, String localeCode,
	        Locale locale) throws IOException {
		Properties props = new Properties();
		InputStream in = file.getInputStream();
		props.load(in);
		in.close();
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			String c = formatCode(createPackageList(pkg, (String) entry.getKey()));
			if (c != null) {
				cache.addToCache(c, localeCode, this.messageFactory.createMessage((String) entry.getValue(), locale));
			}
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
	protected void loadMessagesFromZipFile(OrchidMessageStorageCache<T> cache, List<String> pkg, OrchidMessageResource file, String localeCode, Locale locale)
	        throws IOException {
		OrchidMessageZipResource zip = new OrchidMessageZipResource(file.getName(), file.getInputStream());
		loadMessages(cache, pkg, zip);
	}

}
