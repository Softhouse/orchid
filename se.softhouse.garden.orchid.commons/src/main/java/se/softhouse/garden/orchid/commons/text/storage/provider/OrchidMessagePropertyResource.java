package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

public class OrchidMessagePropertyResource extends OrchidMessageResource {

	protected final InputStream inputStream;

	/**
	 * @param resourceInfo
	 * @param openStream
	 */
	public OrchidMessagePropertyResource(OrchidMessageResourceInfo resourceInfo, InputStream inputStream) {
		super(resourceInfo);
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
		props.load(this.inputStream);
		Set<Entry<Object, Object>> entrySet = props.entrySet();
		List<String> pkgs2 = createPackageList(pkgs, this.resourceInfo.getCode());
		for (Entry<Object, Object> entry : entrySet) {
			addToCache(cache, pkgs2, messageFactory, (String) entry.getKey(), (String) entry.getValue());
		}
	}

}
