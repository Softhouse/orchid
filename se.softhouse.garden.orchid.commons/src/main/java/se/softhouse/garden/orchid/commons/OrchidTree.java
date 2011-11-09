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
package se.softhouse.garden.orchid.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an internal class that holds the structure of the messages in order
 * to be able to return the content of a node.
 * 
 * @author Mikael Svahn
 * 
 */
public class OrchidTree {

	Map<String, Node> allNodes = new HashMap<String, OrchidTree.Node>();
	List<Node> nodes = new ArrayList<OrchidTree.Node>();

	private class Node {
		String name;
		String fullName;
		List<Node> nodes = new ArrayList<OrchidTree.Node>();

		public Node(String fullName, String[] codes, int idx) {
			this.name = codes[idx];
			this.fullName = fullName;
			OrchidTree.this.allNodes.put(fullName, this);
			if (idx + 1 < codes.length) {
				Node node = new Node(fullName + "." + codes[idx + 1], codes, idx + 1);
				this.nodes.add(node);
			}
		}

		public List<String> getFullNames() {
			List<String> list = new ArrayList<String>();
			for (Node node : this.nodes) {
				list.add(node.fullName);
				list.addAll(node.getFullNames());
			}
			return list;
		}

	}

	/**
	 * @param string
	 */
	public void add(String code) {
		String[] codes = code.split("\\.");

		Node node = new Node(codes[0], codes, 0);
		this.nodes.add(node);
	}

	/**
	 * @param string
	 * @return
	 */
	public List<String> getFullNames(String code) {
		Node node = this.allNodes.get(code);
		if (node != null) {
			return node.getFullNames();
		}
		return new ArrayList<String>();
	}
}
