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
package se.softhouse.garden.orchid.commons.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores string keys and their values in a tree. The keys need to be
 * in the format x.y.z where each dot will create a node in the tree. Each node
 * can have a value
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidStringTree<T> {

	private final Map<String, Node> allNodes = new HashMap<String, OrchidStringTree<T>.Node>();
	private final List<Node> rootNodes = new ArrayList<OrchidStringTree<T>.Node>();

	public void add(String code) {
		add(code, null);
	}

	/**
	 * Add a key with a value to the tree.
	 * 
	 * @param key
	 *            The key in a dot format.
	 * @param value
	 *            The value to associate with the key.
	 */
	public void add(String key, T value) {
		String[] subkeys = key.split("\\.");
		Node node = createOrGetNode(subkeys[0], subkeys, 0, this.rootNodes);
		Node n = node.addNode(subkeys, 1);
		n.value = value;
	}

	/**
	 * Add a key with no value to the tree.
	 * 
	 * @param key
	 *            The key in a dot format.
	 */
	public T getValue(String code) {
		Node node = this.allNodes.get(code);
		return node != null ? node.value : null;
	}

	/**
	 * Return a list of keys starting with prefix
	 * 
	 * @param prefix
	 *            The prefix of the key in a x.y.z format
	 * @return A list of found keys
	 */
	public List<String> getKeys(String prefix) {
		Node node = this.allNodes.get(prefix);
		if (node != null) {
			return node.getKeys();
		}
		return new ArrayList<String>();
	}

	/**
	 * Return a list of sub keys starting with prefix, not that only the sub key
	 * of direct child nodes are returned.
	 * 
	 * @param prefix
	 *            The prefix of the key in a x.y.z format
	 * @return A list of found sub keys
	 */
	public List<String> getSubKeys(String code) {
		Node node = this.allNodes.get(code);
		if (node != null) {
			return node.getSubKeys();
		}
		return new ArrayList<String>();
	}

	private Node createOrGetNode(String fullName, String[] codes, int idx, List<Node> nodes) {
		Node node = this.allNodes.get(fullName);
		if (node == null) {
			node = new Node(fullName, codes, idx);
			nodes.add(node);
		}
		return node;
	}

	private class Node {
		String subkey;
		String key;
		T value;
		List<Node> nodes = new ArrayList<OrchidStringTree<T>.Node>();

		public Node(String fullName, String[] codes, int idx) {
			this.subkey = codes[idx];
			this.key = fullName;
			OrchidStringTree.this.allNodes.put(fullName, this);
		}

		public Node addNode(String[] codes, int idx) {
			if (idx < codes.length) {
				Node node = createOrGetNode(this.key + "." + codes[idx], codes, idx, this.nodes);
				return node.addNode(codes, idx + 1);
			}
			return this;
		}

		public List<String> getKeys() {
			List<String> list = new ArrayList<String>();
			for (Node node : this.nodes) {
				if (node.nodes.size() > 0) {
					list.addAll(node.getKeys());
				} else {
					list.add(node.key);
				}
			}
			return list;
		}

		public List<String> getSubKeys() {
			List<String> list = new ArrayList<String>();
			for (Node node : this.nodes) {
				list.add(node.subkey);
			}
			return list;
		}
	}

}
