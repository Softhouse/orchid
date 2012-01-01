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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import se.softhouse.garden.orchid.bean.BeanPropertyUtil.PropertyNameBuilder.PropertyNameBuilderException;

/**
 * @author mis
 * 
 */
public class BeanPropertyUtil {

	@SuppressWarnings("unchecked")
	public static <T> T introspect(Class<T> klass) {
		PropertyNameBuilder builder = new PropertyNameBuilder();
		return (T) Enhancer.create(klass, builder.callback());
	}

	public static <T> PropertyNameBuilder bean(T o) {
		try {
			o.toString();
			throw new IllegalArgumentException("Not a introspect bean");
		} catch (PropertyNameBuilderException e) {
			return e.getBuilder();
		}
	}

	public static class PropertyNameBuilder {
		List<String> properties = new ArrayList<String>();

		public List<String> properties(Object... properties) {
			return this.properties;
		}

		public String property(Object property) {
			return this.properties.get(0);
		}

		private Callback callback() {
			return new MethodInterceptor() {

				@Override
				public Object intercept(Object object, Method method, Object[] arrayOfObject, MethodProxy methodProxy) throws Throwable {

					String name = method.getName();
					int idx = 0;
					if (name.startsWith("get") || name.startsWith("set") || name.startsWith("has")) {
						idx = 3;
					} else if (name.startsWith("is")) {
						idx = 2;
					}
					if (idx > 0) {
						PropertyNameBuilder.this.properties.add(name.substring(idx, idx + 1).toLowerCase() + name.substring(idx + 1));
					} else {
						throw new PropertyNameBuilderException();
					}
					return null;
				}
			};
		}

		@SuppressWarnings("serial")
		public class PropertyNameBuilderException extends RuntimeException {
			public PropertyNameBuilder getBuilder() {
				PropertyNameBuilder.this.properties = new ArrayList<String>();
				return PropertyNameBuilder.this;
			}
		}
	}

}
