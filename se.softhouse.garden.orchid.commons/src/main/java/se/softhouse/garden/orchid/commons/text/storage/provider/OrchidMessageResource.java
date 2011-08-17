package se.softhouse.garden.orchid.commons.text.storage.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.softhouse.garden.orchid.commons.text.storage.provider.OrchidMessageStorageCache.MessageFactory;

public abstract class OrchidMessageResource {

	protected final OrchidMessageResourceInfo resourceInfo;
	protected final String charsetName;

	public OrchidMessageResource(OrchidMessageResourceInfo resourceInfo, String charsetName) {
		this.resourceInfo = resourceInfo;
		this.charsetName = charsetName;
	}

	/**
	 * Load all messages in the specified dir and sub dirs into the package.
	 * 
	 * @param cache
	 *            The cache to load the messages into
	 * @param pkg
	 *            The prefix to add to the code
	 * @throws IOException
	 */
	public abstract <T> void loadMessages(OrchidMessageStorageCache<T> cache, List<String> pkgs, MessageFactory<T> messageFactory) throws IOException;

	/**
	 * Create a new package list by copying the pkgs list and adding the code.
	 * 
	 * @param pkgs
	 *            The current list to copy
	 * @param code
	 *            The code to add to the end of the list
	 * @return The newly created list
	 */
	protected List<String> createPackageList(List<String> pkgs, String code) {
		List<String> pkg = new ArrayList<String>(pkgs);
		pkg.add(code);
		return pkg;
	}

	protected List<String> createPackageList(List<String> pkgs, String code, String delim) {
		List<String> pkg = new ArrayList<String>(pkgs);
		if (code != null) {
			String[] codes = code.split(delim);
			for (String c : codes) {
				pkg.add(c);
			}
		}
		return pkg;
	}

	protected <T> void addToCache(OrchidMessageStorageCache<T> cache, List<String> pkgs, MessageFactory<T> messageFactory, String code, String value) {
		cache.addToCache(createPackageList(pkgs, code), this.resourceInfo.getLocaleCode(), messageFactory.createMessage(value, this.resourceInfo.getLocale()));
	}

}
