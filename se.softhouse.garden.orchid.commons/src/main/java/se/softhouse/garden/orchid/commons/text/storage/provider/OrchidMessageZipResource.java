package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OrchidMessageZipResource implements OrchidMessageResource {

	private final List<OrchidMessageZipEntryResource> entries;
	private final String name;

	public OrchidMessageZipResource(String name, InputStream in) throws IOException {
		this.name = name;
		this.entries = new ArrayList<OrchidMessageZipEntryResource>();
		unzip(in);
	}

	private void unzip(InputStream in) throws IOException {
		final int BUFFER = 2048;
		ZipInputStream zis = new ZipInputStream(in);

		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			int count;
			byte data[] = new byte[BUFFER];
			ByteArrayOutputStream dest = new ByteArrayOutputStream();
			while ((count = zis.read(data, 0, BUFFER)) != -1) {
				dest.write(data, 0, count);
			}
			dest.close();
			this.entries.add(new OrchidMessageZipEntryResource(this, entry, dest.toByteArray()));
		}
		zis.close();
	}

	@Override
	public OrchidMessageResource[] list() {
		return list(null);
	}

	protected OrchidMessageResource[] list(String path) {
		ArrayList<OrchidMessageResource> resources = new ArrayList<OrchidMessageResource>();
		File filePath = path == null ? null : new File(path);
		for (OrchidMessageZipEntryResource entry : this.entries) {
			File parentFile = new File(entry.getEntry().getName()).getParentFile();
			if (filePath == parentFile || filePath != null && filePath.equals(parentFile)) {
				resources.add(entry);
			}
		}
		return resources.toArray(new OrchidMessageResource[resources.size()]);
	}

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return true;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public long getLastModified() {
		return 0;
	}

}
