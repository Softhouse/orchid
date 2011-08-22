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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A storage provider that loads strings from bare files in directory
 * structures, zip files or property files.
 * 
 * The file names have the following format
 * key_[language]_[country]_[variant].[ext]
 * 
 * If the [ext] equals properties the files is read as a property and insert
 * into the property tree. Note that the location and name of the property file
 * will be inserted into the message key. This is the same for zip files.
 * 
 * The getMessage operations returns the found content or if a non leaf node is
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

	protected static final Pattern URL_PATTERN = Pattern.compile("([^:.]*):(.*)");
	protected static final Pattern FILE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)\\.?(.*)");
	protected static final Pattern LOCALE_PATTERN = Pattern.compile("([^_.]*)_?([^.]*)_?([^.]*)");
	protected static final Pattern ZIP_PATTERN = Pattern.compile("([^!]*)!(.+)");

	protected OrchidMessageResource[] dirs;
	protected URL watchFile;

	/**
	 * The constructor which creates an empty cache
	 */
	public OrchidMessageDirectoryStorageProvider() {
	}

	/**
	 * Creates an instance and loads the content from the specified root path.
	 * 
	 * @param spec
	 *            The relative path
	 */
	public OrchidMessageDirectoryStorageProvider(String spec) throws IOException {
		this();
		this.dirs = new OrchidMessageResource[] { createResourceFromSpec(spec) };
	}

	/**
	 * Sets the path to the URL to read messages from. The URL might have the
	 * following protocols: <br>
	 * - file: The URL points to a directory. <br>
	 * - zip: The URL points to a zip file. <br>
	 * - text: The URL points to a text file containing one propery.<br>
	 * - prop: The URL points to a property file. <br>
	 * <br>
	 * Example: <br>
	 * file://somedir/lalal <br>
	 * zip:http://host.se/lalal.zip <br>
	 * prop:file:///lalal.properties <br>
	 * 
	 * @throws IOException
	 */
	public void setUrl(String spec) throws IOException {
		setUrls(new String[] { spec });
	}

	/**
	 * Sets the URLss to read messages from. See {@link #setUrl(String)} for URL
	 * syntax.
	 * 
	 * @throws IOException
	 */
	public void setUrls(String[] dirs) throws IOException {
		this.dirs = new OrchidMessageResource[dirs.length];
		for (int i = 0; i < dirs.length; i++) {
			this.dirs[i] = createResourceFromSpec(dirs[i]);
		}
	}

	/**
	 * Sets the path to the watch file/directory check if the cache shall be
	 * reread.
	 * 
	 * @throws IOException
	 */
	public void setWatchUrl(String spec) throws IOException {
		this.watchFile = new URL(spec);
	}

	/**
	 * Creates a {@link OrchidMessageResource} from the specified spec
	 */
	protected OrchidMessageResource createResourceFromSpec(String spec) throws IOException {
		Matcher matcher = URL_PATTERN.matcher(spec);
		if (matcher.matches()) {
			String protocol = matcher.group(1);
			String path = matcher.group(2);
			File file = new File(path);
			OrchidMessageResourceInfo resourceInfo = createResourceInfo(file.getName());

			if ("file".equals(protocol) && file.isDirectory()) {
				return new OrchidMessageDirResource(this, resourceInfo, this.charsetName, file);
			} else if ("zip".equals(protocol)) {
				String subPath = null;
				Matcher zipMatcher = ZIP_PATTERN.matcher(path);
				if (zipMatcher.matches()) {
					path = zipMatcher.group(1);
					subPath = zipMatcher.group(2);
				}
				return new OrchidMessageZipResource(this, resourceInfo, this.charsetName, new URL(path).openStream(), subPath);
			} else if ("text".equals(protocol)) {
				return new OrchidMessageTextResource(resourceInfo, this.charsetName, new URL(path).openStream());
			} else if ("prop".equals(protocol)) {
				return new OrchidMessagePropertyResource(resourceInfo, this.charsetName, new URL(path).openStream());
			}
		}
		throw new MalformedURLException("Invalid format: " + spec);
	}

	/**
	 * Creates a {@link OrchidMessageResource} from the extension of the
	 * specified name.
	 * 
	 * @param name
	 *            The name of the file
	 * @param inputStream
	 *            The stream to read from
	 * @return The created resource
	 */
	protected OrchidMessageResource createResourceFromName(String name, InputStream inputStream) {
		OrchidMessageResourceInfo resourceInfo = createResourceInfo(name);

		if ("properties".equals(resourceInfo.getExt())) {
			return new OrchidMessagePropertyResource(resourceInfo, this.charsetName, inputStream);
		} else if ("zip".equals(resourceInfo.getExt())) {
			return new OrchidMessageZipResource(this, resourceInfo, this.charsetName, inputStream, null);
		} else {
			return new OrchidMessageTextResource(resourceInfo, this.charsetName, inputStream);
		}
	}

	/**
	 * Creates a {@link OrchidMessageResourceInfo} from the specified file name.
	 * 
	 * @param name
	 *            The name to parse
	 * 
	 * @return The parsed {@link OrchidMessageResourceInfo}
	 */
	protected OrchidMessageResourceInfo createResourceInfo(String name) {
		Matcher matcher = FILE_PATTERN.matcher(name);
		if (matcher.matches()) {
			String code = matcher.group(1);
			String localeCode = matcher.group(2).toLowerCase();
			String ext = matcher.group(3);

			Locale locale = Locale.getDefault();
			Matcher localeMatcher = LOCALE_PATTERN.matcher(localeCode);
			if (localeMatcher.matches()) {
				locale = new Locale(localeMatcher.group(1), localeMatcher.group(2), localeMatcher.group(3));
			}

			return new OrchidMessageResourceInfo(code, localeCode, ext, locale);
		}
		return null;
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
		cache.setPackageStartLevel(this.packageStartLevel);
		return cache;
	}

	/*
	 * (non-Javadoc)
	 * @see se.softhouse.garden.orchid.commons.text.storage.provider.
	 * OrchidMessageStorageCachedProvider#getLastModified()
	 */
	@Override
	protected long getLastModified() throws IOException {
		if (this.watchFile != null) {
			return this.watchFile.openConnection().getLastModified();
		}
		return 0;
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
		if (this.dirs != null) {
			for (OrchidMessageResource dir : this.dirs) {
				dir.loadMessages(cache, pkg, this.messageFactory);
			}
		}
	}

}
