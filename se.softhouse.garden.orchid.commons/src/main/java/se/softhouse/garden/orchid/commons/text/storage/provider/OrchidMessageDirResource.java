package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

public class OrchidMessageDirResource extends OrchidMessageResource {

	OrchidMessageDirectoryStorageProvider<?> provider;
	File file;

	public OrchidMessageDirResource(OrchidMessageDirectoryStorageProvider<?> provider, OrchidMessageResourceInfo resourceInfo, File file) {
		super(resourceInfo);
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
