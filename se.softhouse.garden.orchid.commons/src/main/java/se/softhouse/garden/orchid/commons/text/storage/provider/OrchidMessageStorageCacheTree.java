/**
 * Copyright (c) 2011, Mikael Svahn, Softhouse Consulting AB
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so.
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

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is an internal class that holds the structure of the messages in order
 * to be able to return the content of a node.
 * 
 * @author Mikael Svahn
 * 
 */
class OrchidMessageStorageCacheTree {

	private static final Pattern LOCALE_PATTERN = Pattern.compile("([^_]*)_?([^_]*)_?([^_]*)");

	MessageNode root;

	/**
	 * The constructor
	 */
	public OrchidMessageStorageCacheTree() {
		this.root = new MessageGroupNode();
	}

	/**
	 * Add a message code to the tree
	 * 
	 * @param code
	 *            The code in a "dotted" format
	 * @param locale
	 *            The locale of the message in the format
	 *            <lang>_<country>_<variant>
	 */
	public void addMessage(String code, String locale) {
		String[] codes = code.split("\\.");
		this.root.addMessage(codes, 0, locale);
	}

	/**
	 * Add a message code to the tree
	 * 
	 * @param code
	 *            The code in a "dotted" format
	 * @param locale
	 *            The locale of the message
	 */
	public String getMessage(String code, Locale locale) {
		String[] codes = code.split("\\.");
		return this.root.getMessage(codes, 0, locale);
	}

	/**
	 * The abstract base class for all nodes in the tree
	 */
	public abstract static class MessageNode {

		/**
		 * Add a message as a child to this node.
		 * 
		 * @param codes
		 *            The splitted code of the message
		 * @param idx
		 *            The index of the code to add
		 * @param locale
		 *            The locale
		 */
		public abstract void addMessage(String[] codes, int idx, String locale);

		/**
		 * Add a message as a child to this node.
		 * 
		 * @param codes
		 *            The splitted code of the message
		 * @param idx
		 *            The index of the code to add
		 * @param locale
		 *            The locale
		 */
		public abstract String getMessage(String[] codes, int i, Locale locale);

		/**
		 * Checks of the node matches the specified locale
		 * 
		 * @param locale
		 *            The locale to match with
		 * 
		 * @return True if locale is a match
		 */
		public abstract boolean match(Locale locale);
	}

	/**
	 * A class which represents all non leaf nodes
	 */
	public static class MessageGroupNode extends MessageNode {
		TreeMap<String, MessageNode> children = new TreeMap<String, OrchidMessageStorageCacheTree.MessageNode>();

		/*
		 * (non-Javadoc)
		 * @see
		 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageTree
		 * .MessageNode#addMessage(java.lang.String[], int, java.lang.String)
		 */
		@Override
		public void addMessage(String[] codes, int i, String locale) {
			if (i + 1 < codes.length) {
				MessageNode childNode = this.children.get(codes[i]);
				if (childNode == null) {
					childNode = new MessageGroupNode();
					this.children.put(codes[i], childNode);
				}
				childNode.addMessage(codes, i + 1, locale);
			} else {
				MessageNode childNode = this.children.get(codes[i]);
				if (childNode == null) {
					childNode = new MessageTextNode();
					this.children.put(codes[i], childNode);
				}
				childNode.addMessage(codes, i, locale);
			}
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageTree
		 * .MessageNode#getMessage(java.lang.String[], int, java.util.Locale)
		 */
		@Override
		public String getMessage(String[] codes, int i, Locale locale) {
			if (i < codes.length) {
				MessageNode node = this.children.get(codes[i]);
				if (node != null) {
					return node.getMessage(codes, i + 1, locale);
				}
			} else if (i == codes.length) {
				StringBuilder sb = new StringBuilder();
				for (Entry<String, MessageNode> entry : this.children.entrySet()) {
					if (entry.getValue().match(locale)) {
						if (sb.length() > 0) {
							sb.append(",");
						}
						sb.append(entry.getKey());
					}
				}
				return sb.toString();
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageTree
		 * .MessageNode#match(java.util.Locale)
		 */
		@Override
		public boolean match(Locale locale) {
			// A non leaf node always matches the locale
			return true;
		}
	}

	/**
	 * A class which represent all text nodes (leafs)
	 */
	public static class MessageTextNode extends MessageNode {

		ArrayList<MessageLocale> locales = new ArrayList<OrchidMessageStorageCacheTree.MessageLocale>();

		/*
		 * (non-Javadoc)
		 * @see
		 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageTree
		 * .MessageNode#addMessage(java.lang.String[], int, java.lang.String)
		 */
		@Override
		public void addMessage(String[] codes, int i, String locale) {
			Matcher matcher = LOCALE_PATTERN.matcher(locale);
			if (matcher.matches()) {
				MessageLocale messageLocale = new MessageLocale();
				messageLocale.language = matcher.group(1).toLowerCase();
				messageLocale.country = matcher.group(2).toUpperCase();
				messageLocale.variant = matcher.group(3);
				this.locales.add(messageLocale);
			}
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageTree
		 * .MessageNode#getMessage(java.lang.String[], int, java.util.Locale)
		 */
		@Override
		public String getMessage(String[] codes, int i, Locale locale) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageTree
		 * .MessageNode#match(java.util.Locale)
		 */
		@Override
		public boolean match(Locale locale) {
			for (MessageLocale messageLocale : this.locales) {
				if (messageLocale.language == null || messageLocale.language.length() == 0 || messageLocale.language.equals(locale.getLanguage())) {
					if (messageLocale.country == null || messageLocale.country.length() == 0 || messageLocale.country.equals(locale.getCountry())) {
						if (messageLocale.variant == null || messageLocale.variant.length() == 0 || messageLocale.variant.equals(locale.getVariant())) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

	/**
	 * A data class for locales
	 * 
	 */
	public static class MessageLocale {
		String language;
		String country;
		String variant;
	}

}
