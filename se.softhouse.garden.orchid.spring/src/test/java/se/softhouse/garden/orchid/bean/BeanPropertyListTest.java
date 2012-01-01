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

package se.softhouse.garden.orchid.bean;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author mis
 * 
 */
public class BeanPropertyListTest {

	@Test
	public void listProperties() {

		TestBean bean = BeanPropertyUtil.introspect(TestBean.class);

		List<String> propList = BeanPropertyUtil.bean(bean).properties(bean.getName(), bean.getAge());
		Assert.assertEquals("name", propList.get(0));
		Assert.assertEquals("age", propList.get(1));

		propList = BeanPropertyUtil.bean(bean).properties(bean.getAddress());
		Assert.assertEquals("address", propList.get(0));

		Assert.assertEquals("address", BeanPropertyUtil.bean(bean).property(bean.getAddress()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidArgument() {
		TestBean bean = new TestBean();
		BeanPropertyUtil.bean(bean).property(bean.getAddress());
	}

	public static class TestBean {
		String name;
		String address;
		int age;

		public String getName() {
			return this.name;
		}

		public String getAddress() {
			return this.address;
		}

		public int getAge() {
			return this.age;
		}
	}
}
