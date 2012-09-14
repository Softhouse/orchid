/**
 * Copyright (c) 2012, Mikael Svahn, Softhouse Consulting AB
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
package se.softhouse.garden.orchid.spring;

/**
 * Provids methods to inject spring beans into an object. This is an altenative
 * to use the @Configurable annotation. To use it add following declaration to
 * the class
 * 
 * <pre>
 * &#064;SuppressWarnings(&quot;unused&quot;)
 * private final Void di = DI.inject(this);
 * </pre>
 * 
 * @author Mikael Svahn
 * 
 */
public class DI {
	/**
	 * Inject all beans annotated with @Resource or @Autowired
	 * 
	 * @param obj
	 *            The object to configure
	 * @return Void
	 */
	public static Void inject(Object obj) {
		SpringBeanConfigurer.inject(obj);
		return null;
	}

	/**
	 * Lookup a bean in the SpringContext
	 * 
	 * @param clazz
	 *            The class of the bean to lookup
	 * @return The found bean
	 */
	public static <T> T bean(Class<T> clazz) {
		return SpringBeanConfigurer.bean(clazz);
	}

}
