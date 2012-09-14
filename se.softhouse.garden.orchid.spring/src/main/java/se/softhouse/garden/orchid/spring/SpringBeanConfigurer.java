package se.softhouse.garden.orchid.spring;

import org.springframework.beans.factory.wiring.BeanConfigurerSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

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
