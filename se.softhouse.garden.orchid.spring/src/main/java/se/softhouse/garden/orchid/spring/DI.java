package se.softhouse.garden.orchid.spring;

public class DI {
	public static Void inject(Object obj) {
		SpringBeanConfigurer.inject(obj);
		return null;
	}

	public static <T> T bean(Class<T> clazz) {
		return SpringBeanConfigurer.bean(clazz);
	}

}
