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
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

/**
 * A resoource that will load messages from a property file.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidMessagePropertyResource extends OrchidMessageResource {

	protected final InputStream inputStream;

	/**
	 * @param resourceInfo
	 * @param openStream
	 */
	public OrchidMessagePropertyResource(OrchidMessageResourceInfo resourceInfo, String charsetName, InputStream inputStream) {
		super(resourceInfo, charsetName);
		this.inputStream = inputStream;
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
		Properties props = new Properties();
		props.load(new InputStreamReader(this.inputStream, this.charsetName));
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		List<String> pkgs2 = createPackageList(pkgs, this.resourceInfo.getCode());
		for (Entry<Object, Object> entry : entrySet) {
			addToCache(cache, pkgs2, messageFactory, (String) entry.getKey(), (String) entry.getValue());
		}
	}

}
