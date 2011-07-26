package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface OrchidMessageResource {

	OrchidMessageResource[] list();

	boolean isFile();

	boolean isDirectory();

	String getName();

	InputStream getInputStream() throws IOException;

	long getLength();

	File getFile();

	long getLastModified();

}
