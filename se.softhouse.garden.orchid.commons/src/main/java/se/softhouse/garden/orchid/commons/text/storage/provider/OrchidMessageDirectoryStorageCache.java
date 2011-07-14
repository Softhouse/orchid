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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A message cache that loads strings from bare files in a directory structure.
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
 * This class is not thread safe.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageDirectoryStorageCache<T> extends OrchidMessageStorageCache<T> {

	private static final Pattern FILE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)\\.?(.*)");
	private static final Pattern LOCALE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)_?([^.]*)");

	private File rootDir;

	/**
	 * Creates a cache instance
	 */
	public OrchidMessageDirectoryStorageCache() {
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
	protected String readFileAsString(File file) throws IOException {
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		try {
			long len = file.length();
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

	/*
	 * (non-Javadoc)
	 * @see
	 * se.softhouse.garden.orchid.commons.text.loader.OrchidMessageStoreCache
	 * #loadAllMessages(java.lang.String)
	 */
	@Override
	protected void loadAllMessages(String pkg) throws IOException {
		ArrayList<File> dirs = new ArrayList<File>();
		File[] listFiles = this.getRootDir().listFiles();
		if (listFiles != null) {
			for (File file : listFiles) {
				if (file.isFile()) {
					loadMessageFromFile(pkg, file);
				} else if (file.isDirectory()) {
					dirs.add(file);
				}
			}
		}
		for (File file : dirs) {
			loadAllMessages(getPackage(pkg, file.getName()), file);
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
	protected void loadAllMessages(String pkg, File dir) throws IOException {
		ArrayList<File> dirs = new ArrayList<File>();
		File[] listFiles = dir.listFiles();
		if (listFiles != null) {
			for (File file : listFiles) {
				if (file.isFile()) {
					loadMessageFromFile(pkg, file);
				} else if (file.isDirectory()) {
					dirs.add(file);
				}
			}
		}
		for (File file : dirs) {
			loadAllMessages(getPackage(pkg, file.getName()), file);
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
	protected void loadMessageFromFile(String pkg, File file) throws IOException {
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
				loadMessageFromPropertyFile(getPackage(pkg, code), file, localeCode, locale);
			} else {
				String fileAsString = readFileAsString(file);
				addToCache(getPackage(pkg, code), localeCode, this.messageFactory.createMessage(fileAsString, locale));
			}
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
	protected void loadMessageFromPropertyFile(String pkg, File file, String localeCode, Locale locale) throws IOException {
		Properties props = new Properties();
		FileInputStream in = new FileInputStream(file);
		props.load(in);
		in.close();
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		for (Entry<Object, Object> entry : entrySet) {
			addToCache(getPackage(pkg, (String) entry.getKey()), localeCode, this.messageFactory.createMessage((String) entry.getValue(), locale));
		}
	}

	/**
	 * Set the root dir to read fils from
	 */
	public void setRootDir(File rootDir) {
		this.rootDir = rootDir;
	}

	/**
	 * Return the root dir of this cache
	 */
	public File getRootDir() {
		return this.rootDir;
	}

}
