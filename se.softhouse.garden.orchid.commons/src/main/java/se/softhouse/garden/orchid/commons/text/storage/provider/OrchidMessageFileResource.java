package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OrchidMessageFileResource implements OrchidMessageResource {

	File file;

	public OrchidMessageFileResource(File file) {
		this.file = file;
	}

	@Override
	public OrchidMessageResource[] list() {
		File[] listFiles = this.file.listFiles();
		OrchidMessageResource[] resources = new OrchidMessageFileResource[listFiles.length];
		for (int i = 0; i < listFiles.length; i++) {
			resources[i] = new OrchidMessageFileResource(listFiles[i]);
		}
		return resources;
	}

	@Override
	public boolean isFile() {
		return this.file.isFile();
	}

	@Override
	public boolean isDirectory() {
		return this.file.isDirectory();
	}

	@Override
	public String getName() {
		return this.file.getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(this.file);
	}

	@Override
	public long getLength() {
		return this.file.length();
	}

	@Override
	public File getFile() {
		return this.file;
	}

	@Override
	public long getLastModified() {
		return this.file.lastModified();
	}

}
