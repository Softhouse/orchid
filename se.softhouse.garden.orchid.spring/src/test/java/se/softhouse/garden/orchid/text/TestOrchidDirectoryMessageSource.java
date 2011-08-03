package se.softhouse.garden.orchid.text;

import static se.softhouse.garden.orchid.commons.text.OrchidMessage.arg;
import static se.softhouse.garden.orchid.spring.text.OrchidMessageSource.code;

import java.io.IOException;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import se.softhouse.garden.orchid.spring.text.OrchidDirectoryMessageSource;
import se.softhouse.garden.orchid.text.TestOrchidMessageSource.TestArguments;
import se.softhouse.garden.orchid.text.TestOrchidMessageSource.TestMessages;

public class TestOrchidDirectoryMessageSource {

	@Test
	public void testMessageSource() throws IOException {
		OrchidDirectoryMessageSource ms = new OrchidDirectoryMessageSource();
		ms.setDir("texttest/test");
		ms.setUseCodeAsDefaultMessage(true);
		ms.start();
		Assert.assertEquals("FileTest message", ms.getMessage("msg0", (Object[]) null, Locale.getDefault()));
		Assert.assertEquals("FileTest message 1", ms.getMessage("msg1", new Object[] { 1 }, Locale.getDefault()));
		Assert.assertEquals("FileTest message 002 with name Micke",
		        ms.getMessage("msg2", new Object[] { arg(TestArguments.NAME, "Micke").arg(TestArguments.ID, 2) }, Locale.getDefault()));
		Assert.assertEquals("FileTest message 02 using name Micke",
		        ms.getMessage("msg.3", new Object[] { arg(TestArguments.NAME, "Micke").arg(TestArguments.ID, 2) }, Locale.getDefault()));
	}

	@Test
	public void testMessageSourceResolvable() throws IOException {
		OrchidDirectoryMessageSource ms = new OrchidDirectoryMessageSource();
		ms.setDir("texttest/test");
		ms.setUseCodeAsDefaultMessage(true);
		ms.start();
		Assert.assertEquals("FileTest message", ms.getMessage(code(TestMessages.MSG0), Locale.getDefault()));
		Assert.assertEquals("FileTest message 1", ms.getMessage(code(TestMessages.MSG1).arg(TestArguments.ID, 1), Locale.getDefault()));
		Assert.assertEquals("FileTest message 002 with name Micke",
		        ms.getMessage(code(TestMessages.MSG2).arg(TestArguments.NAME, "Micke").arg(TestArguments.ID, 2), Locale.getDefault()));
		Assert.assertEquals("FileTest message 02 using name Micke",
		        ms.getMessage(code(TestMessages.MSG_3).arg(TestArguments.NAME, "Micke").arg(TestArguments.ID, 2), Locale.getDefault()));
		Assert.assertEquals("FileTest message 02 using name null", ms.getMessage(code(TestMessages.MSG_3).arg(TestArguments.ID, 2), Locale.getDefault()));
		Assert.assertEquals("Missed code", ms.getMessage(code(TestMessages.MSG4), Locale.getDefault()));

	}

	@Test
	public void testMessageSourceLocale() throws IOException {
		OrchidDirectoryMessageSource ms = new OrchidDirectoryMessageSource();
		ms.setDir("texttest/test");
		ms.start();
		Assert.assertEquals("default1", ms.getMessage(code(TestMessages.LOCAL_1), Locale.getDefault()));
		Assert.assertEquals("default2", ms.getMessage(code(TestMessages.LOCAL_2), Locale.getDefault()));
		Assert.assertEquals("default1", ms.getMessage(code(TestMessages.LOCAL_1), new Locale("se")));
		Assert.assertEquals("se2", ms.getMessage(code(TestMessages.LOCAL_2), new Locale("se")));
		Assert.assertEquals("se3", ms.getMessage(code(TestMessages.LOCAL_3), new Locale("se")));
		Assert.assertEquals("se4", ms.getMessage(code(TestMessages.LOCAL_4), new Locale("se")));
		Assert.assertEquals("default1", ms.getMessage(code(TestMessages.LOCAL_1), new Locale("se", "sv")));
		Assert.assertEquals("se2", ms.getMessage(code(TestMessages.LOCAL_2), new Locale("se", "sv")));
		Assert.assertEquals("se_sv3", ms.getMessage(code(TestMessages.LOCAL_3), new Locale("se", "sv")));
		Assert.assertEquals("se_sv4", ms.getMessage(code(TestMessages.LOCAL_4), new Locale("se", "sv")));
		Assert.assertEquals("default1", ms.getMessage(code(TestMessages.LOCAL_1), new Locale("se", "sv", "sms")));
		Assert.assertEquals("se2", ms.getMessage(code(TestMessages.LOCAL_2), new Locale("se", "sv", "sms")));
		Assert.assertEquals("se_sv3", ms.getMessage(code(TestMessages.LOCAL_3), new Locale("se", "sv", "sms")));
		Assert.assertEquals("se_sv_sms4", ms.getMessage(code(TestMessages.LOCAL_4), new Locale("se", "sv", "sms")));
		Assert.assertEquals("email", ms.getMessage(code(TestMessages.LOCAL_4), new Locale("se", "", "email")));
	}

	@Test
	public void testMessageSourceEmbedded() throws IOException {
		OrchidDirectoryMessageSource ms = new OrchidDirectoryMessageSource();
		ms.setDir("texttest/test");
		ms.start();
		Assert.assertEquals("Message with an embedded message EmbeddedMessage", ms.getMessage(code(TestMessages.EMBEDDED_0), Locale.getDefault()));
		Assert.assertEquals("Message with an embedded message {embedded.11}", ms.getMessage(code(TestMessages.EMBEDDED_10), Locale.getDefault()));
	}

	@Test
	public void testMessageSourceEmbeddedWithArgs() throws IOException {
		OrchidDirectoryMessageSource ms = new OrchidDirectoryMessageSource();
		ms.setDir("texttest/test");
		ms.setUseCodeAsDefaultMessage(true);
		ms.start();
		Assert.assertEquals("Message with an embedded message Embedded Wille",
		        ms.getMessage(code(TestMessages.EMBEDDED_20).arg(TestArguments.NAME, "Wille"), Locale.getDefault()));
		Assert.assertEquals("Message with an embedded message Embedded Wille",
		        ms.getMessage("embedded.20", new Object[] { arg(TestArguments.NAME, "Wille") }, Locale.getDefault()));
	}
}
