package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

public class OrchidMessageZipResource extends OrchidMessageResource {

	private final InputStream inputStream;
	private final OrchidMessageDirectoryStorageProvider<?> provider;
	private final String subPath;

	/**
	 * @param resourceInfo
	 * @param inputStream
	 * @param subPath
	 */
	public OrchidMessageZipResource(OrchidMessageDirectoryStorageProvider<?> provider, OrchidMessageResourceInfo resourceInfo, InputStream inputStream,
	        String subPath) {
		super(resourceInfo);
		this.provider = provider;
		this.inputStream = inputStream;
		this.subPath = subPath;
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
				if (this.subPath == null || entry.getName().startsWith(this.subPath)) {
					File file = new File(entry.getName());
					String path = file.getParent();
					path = path == null || path.length() <= subPathLength ? null : file.getParent().substring(subPathLength);
					OrchidMessageResource resource = this.provider.createResourceFromName(file.getName(), zis);
					resource.loadMessages(cache, createPackageList(pkgs2, path, "/"), messageFactory);
				}
			}
		}
	}

}
