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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

/**
 * A Resource that will load files from the zip file and its sub directories.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageZipResource extends OrchidMessageResource {

	private final InputStream inputStream;
	private final OrchidMessageDirectoryStorageProvider<?> provider;
	private final String subPath;
	private final Pattern regexp;

	/**
	 * @param resourceInfo
	 * @param inputStream
	 * @param subPath
	 */
	public OrchidMessageZipResource(OrchidMessageDirectoryStorageProvider<?> provider, OrchidMessageResourceInfo resourceInfo, String charsetName,
	        InputStream inputStream, String subPath) {
		super(resourceInfo, charsetName);
		this.provider = provider;
		this.inputStream = inputStream;
		if (subPath != null && subPath.startsWith("~")) {
			this.subPath = null;
			this.regexp = Pattern.compile("(" + subPath.substring(1) + ")" + "(.*)");
		} else {
			this.subPath = subPath;
			this.regexp = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see se.softhouse.garden.orchid.commons.text.storage.provider.
	 * OrchidMessageResource
	 * #loadMessages(se.softhouse.garden.orchid.commons.text
	 * .storage.provider.OrchidMessageStorageCache, java.util.List,
	 * se.softhouse.
	 * garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache
	 * .MessageFactory)
	 */
	@Override
	public <T> void loadMessages(OrchidMessageStorageCache<T> cache, List<String> pkgs, MessageFactory<T> messageFactory) throws IOException {
		List<String> pkgs2 = createPackageList(pkgs, this.resourceInfo.getCode());
		ZipInputStream zis = new ZipInputStream(this.inputStream);
		ZipEntry entry;
		int subPathLength = this.subPath == null ? 0 : this.subPath.length();
		while ((entry = zis.getNextEntry()) != null) {
			if (!entry.isDirectory()) {
				if (this.regexp == null) {
					if (this.subPath == null || entry.getName().startsWith(this.subPath)) {
						File file = new File(entry.getName());
						String path = file.getParent();
						path = path == null || path.length() <= subPathLength ? null : file.getParent().substring(subPathLength);
						loadResource(cache, messageFactory, pkgs2, zis, file, path);
					}
				} else {
					Matcher matcher = this.regexp.matcher(entry.getName());
					if (matcher.matches()) {
						String path = matcher.group(2);
						File file = new File(path);
						path = file.getParent();
						loadResource(cache, messageFactory, pkgs2, zis, file, path);
					}
				}
			}
		}
	}

	/**
	 * Load messages from a zip file
	 */
	protected <T> void loadResource(OrchidMessageStorageCache<T> cache, MessageFactory<T> messageFactory, List<String> pkgs2, ZipInputStream zis, File file,
	        String path) throws IOException {
		OrchidMessageResource resource = this.provider.createResourceFromName(file.getName(), zis);
		resource.loadMessages(cache, createPackageList(pkgs2, path, "/"), messageFactory);
	}
}
