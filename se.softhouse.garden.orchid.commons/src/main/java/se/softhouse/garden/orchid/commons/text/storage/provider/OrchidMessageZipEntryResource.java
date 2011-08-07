package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class OrchidMessageZipEntryResource implements OrchidMessageResource {

	private final OrchidMessageZipResource zip;
	private final ZipEntry entry;
	private final byte[] data;

	public OrchidMessageZipEntryResource(OrchidMessageZipResource zip, ZipEntry entry, byte[] data) {
		this.zip = zip;
		this.entry = entry;
		this.data = data;
	}

	@Override
	public OrchidMessageResource[] list() {
		return this.zip.list(this.entry.getName());
	}

	@Override
	public boolean isFile() {
		return !this.entry.isDirectory();
	}

	@Override
	public boolean isDirectory() {
		return this.entry.isDirectory();
	}

	@Override
	public String getName() {
		return new File(this.entry.getName()).getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.data);
	}

	@Override
	public long getLastModified() {
		return this.entry.getTime();
	}

	public ZipEntry getEntry() {
		return this.entry;
	}

}
