package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class OrchidMessageZipResource implements OrchidMessageResource {

	private final ZipFile file;
	private final ZipEntry entry;

	public OrchidMessageZipResource(ZipFile file, ZipEntry entry) {
		this.file = file;
		this.entry = entry;
	}

	@Override
	public OrchidMessageResource[] list() {
		ArrayList<OrchidMessageResource> resources = new ArrayList<OrchidMessageResource>();
		File path = this.entry == null ? null : new File(this.entry.getName());
		Enumeration<? extends ZipEntry> entries = this.file.entries();
		while (entries.hasMoreElements()) {
			ZipEntry element = entries.nextElement();
			File parentFile = new File(element.getName()).getParentFile();
			if (path == parentFile || (path != null && path.equals(parentFile))) {
				resources.add(new OrchidMessageZipResource(this.file, element));
			}
		}
		return resources.toArray(new OrchidMessageResource[resources.size()]);
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
		File f = new File(this.entry.getName());
		return f.getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.file.getInputStream(this.entry);
	}

	@Override
	public long getLength() {
		return this.entry.getSize();
	}

	@Override
	public File getFile() {
		return null;
	}

	@Override
	public long getLastModified() {
		return this.entry.getTime();
	}

}
