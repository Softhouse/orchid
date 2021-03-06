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

import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Used by the DI class to lookup beans in the SpringContext This bean must be
 * instanciated in the applicationContext.
 * 
 * @author Mikael Svahn
 */
public class SpringBeanConfigurer implements ApplicationListener<ContextRefreshedEvent> {

	private static ApplicationContext context = null;
	private static BeanConfigurerSupport beanConfigurer = null;

	public static void inject(Object obj) {
		beanConfigurer.configureBean(obj);
	}

	public static <T> T bean(Class<T> clazz) {
		return SpringBeanConfigurer.context.getBean(clazz);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		context = event.getApplicationContext();
		beanConfigurer = (BeanConfigurerSupport) event.getApplicationContext().getBean("beanConfigurer");
	}
}
