package se.softhouse.garden.orchid.text;

import static se.softhouse.garden.orchid.commons.text.OrchidMessage.arg;
import static se.softhouse.garden.orchid.spring.text.OrchidMessageSource.code;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import se.softhouse.garden.orchid.commons.text.OrchidMessageArgumentCode;
import se.softhouse.garden.orchid.commons.text.OrchidMessageCode;
import se.softhouse.garden.orchid.spring.text.OrchidReloadableResourceBundleMessageSource;

public class TestOrchidMessageSource {

	@Before
	public void setup() {
		Locale.setDefault(Locale.US);
	}

	@Test
	public void testMessageSource() {
		OrchidReloadableResourceBundleMessageSource ms = new OrchidReloadableResourceBundleMessageSource();
		ms.setBasename("test");
		ms.setUseCodeAsDefaultMessage(true);
		Assert.assertEquals("FileTest message", ms.getMessage("msg0", (Object[]) null, Locale.getDefault()));
		Assert.assertEquals("FileTest message 1", ms.getMessage("msg1", new Object[] { 1 }, Locale.getDefault()));
		Assert.assertEquals("FileTest message 002 with name Micke",
		        ms.getMessage("msg2", new Object[] { arg(TestArguments.NAME, "Micke").arg(TestArguments.ID, 2) }, Locale.getDefault()));
		Assert.assertEquals("FileTest message 02 using name Micke",
		        ms.getMessage("msg.3", new Object[] { arg(TestArguments.NAME, "Micke").arg(TestArguments.ID, 2) }, Locale.getDefault()));
	}

	@Test
	public void testMessageSourceResolvable() {
		OrchidReloadableResourceBundleMessageSource ms = new OrchidReloadableResourceBundleMessageSource();
		ms.setBasename("test");
		ms.setUseCodeAsDefaultMessage(true);
		Assert.assertEquals("x", ms.getMessage(code("x"), Locale.getDefault()));
		Assert.assertEquals("y", ms.getMessage(code("x", "y"), Locale.getDefault()));
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
	public void testMessageSourceLocale() {
		OrchidReloadableResourceBundleMessageSource ms = new OrchidReloadableResourceBundleMessageSource();
		ms.setBasename("test");
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
	public void testMessageSourceEmbedded() {
		OrchidReloadableResourceBundleMessageSource ms = new OrchidReloadableResourceBundleMessageSource();
		ms.setBasename("test");
		ms.setUseCodeAsDefaultMessage(true);
		Assert.assertEquals("Message with an embedded message EmbeddedMessage", ms.getMessage(code(TestMessages.EMBEDDED_0), Locale.getDefault()));
		Assert.assertEquals("Message with an embedded message {embedded.11}", ms.getMessage(code(TestMessages.EMBEDDED_10), Locale.getDefault()));
	}

	@Test
	public void testMessageSourceEmbeddedWithArgs() {
		OrchidReloadableResourceBundleMessageSource ms = new OrchidReloadableResourceBundleMessageSource();
		ms.setBasename("test");
		ms.setUseCodeAsDefaultMessage(true);
		Assert.assertEquals("Message with an embedded message Embedded Wille",
		        ms.getMessage(code(TestMessages.EMBEDDED_20).arg(TestArguments.NAME, "Wille"), Locale.getDefault()));
		Assert.assertEquals("Message with an embedded message Embedded Wille",
		        ms.getMessage("embedded.20", new Object[] { arg(TestArguments.NAME, "Wille") }, Locale.getDefault()));

	}

	public enum TestMessages implements OrchidMessageCode {
		MSG0("Test message"), //
		MSG1("Test message {id}"), //
		MSG2("Test message {id,number,000} with name {name}"), //
		MSG_3("Test message {id,number,000} with name {name}"), //
		MSG4("Missed code"), //
		LOCAL_1("code1"), //
		LOCAL_2("code2"), //
		LOCAL_3("code3"), //
		LOCAL_4("code3"), //
		EMBEDDED_0("embedded message"), //
		EMBEDDED_10("XXX"), //
		EMBEDDED_20("embedded message"), //
		;

		private final String realName;
		private final String pattern;

		private TestMessages(String pattern) {
			this.pattern = pattern;
			this.realName = name().toLowerCase().replaceAll("_", ".");
		}

		@Override
		public String getPattern() {
			return this.pattern;
		}

		@Override
		public String getRealName() {
			return this.realName;
		}
	}

	public enum TestArguments implements OrchidMessageArgumentCode {
		ID, NAME;

		@Override
		public String getRealName() {
			return name().toLowerCase();
		}
	}
}
