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

package se.softhouse.garden.orchid.spring.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This is an alternative implementation to the <spring:url> tag which can be
 * accessed as follows ${url['/a/b/c']}
 * 
 * @author Mikael Svahn
 * 
 */
public abstract class AbstractStringMapBean implements Map<String, StringMap> {

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public MessageMap put(String key, StringMap value) {
		return null;
	}

	@Override
	public MessageMap remove(Object key) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends StringMap> m) {
	}

	@Override
	public void clear() {
	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public Collection<StringMap> values() {
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<String, StringMap>> entrySet() {
		return null;
	}
}
