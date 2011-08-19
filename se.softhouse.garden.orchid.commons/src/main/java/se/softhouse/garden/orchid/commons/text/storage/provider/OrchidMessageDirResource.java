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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

/**
 * A Resource that will load files from the directory and its sub directories.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageDirResource extends OrchidMessageResource {

	OrchidMessageDirectoryStorageProvider<?> provider;
	File file;

	/**
	 * Constructor
	 * 
	 * @param provider
	 *            The provider that this resource is connected to.
	 * @param resourceInfo
	 *            The information about the resource
	 * @param charsetName
	 *            The charset name to use when reading this or any sub resource
	 * @param file
	 *            The file object pointing to the directory to read
	 */
	public OrchidMessageDirResource(OrchidMessageDirectoryStorageProvider<?> provider, OrchidMessageResourceInfo resourceInfo, String charsetName, File file) {
		super(resourceInfo, charsetName);
		this.provider = provider;
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * @see se.softhouse.garden.orchid.commons.text.storage.provider.
	 * OrchidMessageResource
	 * #loadMessages(se.softhouse.garden.orchid.commons.text
	 * .storage.provider.OrchidMessageStorageCache, java.util.List)
	 */
	@Override
	public <T> void loadMessages(OrchidMessageStorageCache<T> cache, List<String> pkgs, MessageFactory<T> messageFactory) throws IOException {
		loadMessages(this.file, cache, createPackageList(pkgs, this.resourceInfo.getCode()), messageFactory);
	}

	/**
	 * Load all messages in the specified dir and sub dirs into the package.
	 * 
	 * @param dir
	 *            The directory to scan for files and subdirectories
	 * @param cache
	 *            The cache to load the messages into
	 * @param pkg
	 *            The prefix to add to the code
	 * @param messageFactory
	 *            The message factory to use when creating a message
	 * @throws IOException
	 */
	public <T> void loadMessages(File dir, OrchidMessageStorageCache<T> cache, List<String> pkgs, MessageFactory<T> messageFactory) throws IOException {
		ArrayList<File> dirs = new ArrayList<File>();
		ArrayList<File> files = new ArrayList<File>();
		File[] listFiles = dir.listFiles();

		for (File file : listFiles) {
			if (file.isFile()) {
				files.add(file);
			} else {
				dirs.add(file);
			}
		}

		Collections.sort(files);
		Collections.sort(dirs);

		for (File file : files) {
			OrchidMessageResource resource = this.provider.createResourceFromName(file.getName(), new FileInputStream(file));
			resource.loadMessages(cache, pkgs, messageFactory);
		}
		for (File file : dirs) {
			loadMessages(file, cache, createPackageList(pkgs, file.getName()), messageFactory);
		}
	}
}
