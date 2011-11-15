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

package se.softhouse.garden.orchid.commons.util;

import junit.framework.Assert;

import org.junit.Test;

import se.softhouse.garden.orchid.commons.util.OrchidStringTree;

/**
 * @author mis
 * 
 */
public class TestOrchidStringTree {

	@Test
	public void testTree() {
		OrchidStringTree<String> tree = new OrchidStringTree<String>();

		tree.add("feature.group1.A");
		tree.add("feature.group1.B", "VB");
		tree.add("feature.group2.C");
		Assert.assertEquals(3, tree.getKeys("feature").size());
		Assert.assertEquals(2, tree.getKeys("feature.group1").size());
		Assert.assertEquals(1, tree.getKeys("feature.group2").size());
		Assert.assertEquals(0, tree.getKeys("feature.group4").size());
		Assert.assertEquals(0, tree.getKeys("features.group1").size());
		Assert.assertEquals(2, tree.getSubKeys("feature").size());
		Assert.assertEquals(2, tree.getSubKeys("feature.group1").size());
		Assert.assertEquals(1, tree.getSubKeys("feature.group2").size());
		Assert.assertEquals(0, tree.getSubKeys("feature.group4").size());
		Assert.assertEquals(0, tree.getSubKeys("features.group1").size());
		Assert.assertEquals("VB", tree.getValue("feature.group1.B"));
		Assert.assertNull(tree.getValue("feature.group1.A"));
		Assert.assertNull(tree.getValue("feature.group1"));
		tree.add("feature.group1", "G1");
		Assert.assertEquals("VB", tree.getValue("feature.group1.B"));
		Assert.assertEquals("G1", tree.getValue("feature.group1"));
	}
}
