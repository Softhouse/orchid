package se.softhouse.garden.orchid.text;

import java.io.IOException;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import se.softhouse.garden.orchid.spring.text.OrchidDirectoryMessageSource;
import se.softhouse.garden.orchid.spring.text.OrchidReloadableResourceBundleMessageSource;

public class TestOrchidMessageSourceParent {

	@Test
	public void testMessageSource() throws IOException {
		OrchidReloadableResourceBundleMessageSource ms = new OrchidReloadableResourceBundleMessageSource();
		ms.setBasename("test");
		ms.setUseCodeAsDefaultMessage(true);
		OrchidDirectoryMessageSource ms2 = new OrchidDirectoryMessageSource();
		ms2.setRoot("texttest/test");
		ms2.setParentMessageSource(ms);
		ms2.start();
		Assert.assertEquals("parent1", ms2.getMessage("parent.1", (Object[]) null, Locale.getDefault()));
		Assert.assertEquals("parent2", ms2.getMessage("parent.2", (Object[]) null, Locale.getDefault()));
	}
}
