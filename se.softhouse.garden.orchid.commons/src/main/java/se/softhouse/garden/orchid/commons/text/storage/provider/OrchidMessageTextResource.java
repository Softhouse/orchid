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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

/**
 * A resource that loads a message from the content of a text file
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessageTextResource extends OrchidMessageResource {

	private final InputStream inputStream;

	/**
	 * @param resourceInfo
	 * @param openStream
	 */
	public OrchidMessageTextResource(OrchidMessageResourceInfo resourceInfo, String charsetName, InputStream inputStream) {
		super(resourceInfo, charsetName);
		this.inputStream = inputStream;
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
		String out = readFromStream(this.inputStream, this.charsetName);
		addToCache(cache, pkgs, messageFactory, this.resourceInfo.getCode(), out);
	}

	public static String readFromStream(InputStream inputStream, String charsetName) throws UnsupportedEncodingException, IOException {
		char[] buffer = new char[0x10000];
		StringBuilder out = new StringBuilder();
		InputStreamReader in = new InputStreamReader(inputStream, charsetName);
		int read;
		do {
			read = in.read(buffer, 0, buffer.length);
			if (read > 0) {
				out.append(buffer, 0, read);
			}
		} while (read >= 0);
		return out.toString();
	}
}
