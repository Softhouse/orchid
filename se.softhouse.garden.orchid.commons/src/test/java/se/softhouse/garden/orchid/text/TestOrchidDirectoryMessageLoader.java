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
package se.softhouse.garden.orchid.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;

import se.softhouse.garden.orchid.commons.text.OrchidMessage;
import se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryMessageFormatLoader;
import se.softhouse.garden.orchid.commons.text.loader.OrchidDirectoryStringMessageLoader;
import se.softhouse.garden.orchid.text.TestOrchidMessageFormat.TestMessages;

public class TestOrchidDirectoryMessageLoader {

	@Test
	public void testGetMessage() throws IOException {
		OrchidDirectoryStringMessageLoader dml = new OrchidDirectoryStringMessageLoader("texttest");
		dml.start();
		Assert.assertEquals("FileTest message", dml.getMessage("bundle1.msg.0"));
		Assert.assertEquals("FileTest message", dml.getMessage("bundle1.msg.0"));
		Assert.assertNull(dml.getMessage("bundle1.msg.0.txt"));
		Assert.assertEquals("FileTest message\nline 2", dml.getMessage("bundle1.msg.1"));
		Assert.assertEquals("svse", dml.getMessage("bundle1.msg.2", new Locale("sv", "SE")));
		Assert.assertEquals("ense", dml.getMessage("bundle1.msg.2", new Locale("en", "SE")));
		Assert.assertEquals("se", dml.getMessage("bundle1.msg.2", new Locale("de", "SE")));
	}

	@Test
	public void testGetMessageGroup() throws IOException {
		OrchidDirectoryStringMessageLoader dml = new OrchidDirectoryStringMessageLoader("texttest");
		dml.start();
		Assert.assertEquals("0,1", dml.getMessage("bundle1.msg"));
		Assert.assertEquals("0,1,2", dml.getMessage("bundle1.msg", new Locale("sv", "SE")));
		Assert.assertEquals("0,1,2", dml.getMessage("bundle1.msg", new Locale("en", "SE")));
		Assert.assertEquals("0,1,2", dml.getMessage("bundle1.msg", new Locale("de", "SE")));
	}

	@Test
	public void testGetMessageCache() throws IOException {
		Locale.setDefault(Locale.US);
		OrchidDirectoryStringMessageLoader dml = new OrchidDirectoryStringMessageLoader("texttest");
		dml.start();
		File file1 = new File("texttest/bundle1/cache/cache");
		File file2 = new File("texttest/bundle1/cache/cache_en_us");
		file1.delete();
		file2.delete();
		dml.reload(true);
		Assert.assertNull(dml.getMessage("bundle1.cache.cache"));
		writeFileContent("This is a test", file1);
		dml.reload(true);
		Assert.assertEquals("This is a test", dml.getMessage("bundle1.cache.cache"));
		writeFileContent("This is a test 2", file1);
		dml.reload(true);
		Assert.assertEquals("This is a test 2", dml.getMessage("bundle1.cache.cache"));
		writeFileContent("This is a test 3", file2);
		dml.setCacheSeconds(-1);
		dml.reload(false);
		Assert.assertEquals("This is a test 2", dml.getMessage("bundle1.cache.cache"));
		dml.stop();
		dml.start();
		Assert.assertEquals("This is a test 3", dml.getMessage("bundle1.cache.cache"));
		Assert.assertEquals("This is a test 2", dml.getMessage("bundle1.cache.cache", new Locale("se")));
		Assert.assertEquals("cache", dml.getMessage("bundle1.cache"));
		dml.stop();
	}

	@Test
	public void testReloadThread() throws IOException, InterruptedException {
		Locale.setDefault(Locale.US);
		OrchidDirectoryStringMessageLoader dml = new OrchidDirectoryStringMessageLoader("texttest");
		dml.setCacheMillis(500);
		File root = new File("texttest");
		File file1 = new File("texttest/bundle1/thread/1.txt");
		File file2 = new File("texttest/bundle1/thread/2.txt");
		file1.delete();
		file2.delete();
		Assert.assertNull(dml.getMessage("bundle1.cache.cache"));
		writeFileContent("Test1A", file1);
		writeFileContent("Test2A", file2);
		dml.start();
		Assert.assertEquals("Test1A", dml.getMessage("bundle1.thread.1"));
		Assert.assertEquals("Test2A", dml.getMessage("bundle1.thread.2"));
		writeFileContent("Test1B", file1);
		writeFileContent("Test2B", file2);
		root.setLastModified(System.currentTimeMillis());
		Thread.sleep(1000);
		Assert.assertEquals("Test1B", dml.getMessage("bundle1.thread.1"));
		Assert.assertEquals("Test2B", dml.getMessage("bundle1.thread.2"));
		writeFileContent("Test1C", file1);
		root.setLastModified(System.currentTimeMillis());
		Thread.sleep(1000);
		Assert.assertEquals("Test1C", dml.getMessage("bundle1.thread.1"));
		Assert.assertEquals("Test2B", dml.getMessage("bundle1.thread.2"));

	}

	@Test
	public void testFunctions() throws IOException {
		Locale.setDefault(Locale.US);
		OrchidDirectoryMessageFormatLoader dml = new OrchidDirectoryMessageFormatLoader("texttest");
		dml.start();
		Assert.assertEquals("The base text and the embedded: Default Hi", dml.getMessage("func.base").format(OrchidMessage.arg("msg", "Hi")));
		Assert.assertEquals("The base text and the embedded: SV Hi", dml.getMessage("func.base", new Locale("sv", "SE")).format(OrchidMessage.arg("msg", "Hi")));
	}

	@Test
	public void testChoice() throws IOException {
		Locale.setDefault(Locale.US);
		OrchidDirectoryMessageFormatLoader dml = new OrchidDirectoryMessageFormatLoader("texttest");
		dml.start();
		Assert.assertEquals("The base text and the choice: Type 1", dml.getMessage("choice.base").format(OrchidMessage.arg("type", 1)));
		Assert.assertEquals("The base text and the choice: Type 2", dml.getMessage("choice.base").format(OrchidMessage.arg("type", 2)));
		Assert.assertEquals("The base text and the choice: TYPE 5", dml.getMessage("choice.base").format(OrchidMessage.arg("type", 5)));
	}

	@Test
	public void testPopeties() throws IOException {
		Locale.setDefault(Locale.US);
		OrchidDirectoryMessageFormatLoader dml = new OrchidDirectoryMessageFormatLoader("texttest");
		dml.start();
		Assert.assertEquals("This is the title", dml.getMessage("props.b1.text.title").format(OrchidMessage.arg("type", 1)));
	}

	@Test
	public void testMessageSourceLocale() throws IOException {
		Locale.setDefault(Locale.US);
		OrchidDirectoryMessageFormatLoader dml = new OrchidDirectoryMessageFormatLoader();
		dml.setRoot("texttest/test");
		dml.start();
		Assert.assertEquals("default1", dml.getMessage(TestMessages.LOCAL_1, Locale.getDefault()).format(null));
		Assert.assertEquals("default2", dml.getMessage(TestMessages.LOCAL_2, Locale.getDefault()).format(null));
		Assert.assertEquals("default1", dml.getMessage(TestMessages.LOCAL_1, new Locale("se")).format(null));
		Assert.assertEquals("se2", dml.getMessage(TestMessages.LOCAL_2, new Locale("se")).format(null));
		Assert.assertEquals("se3", dml.getMessage(TestMessages.LOCAL_3, new Locale("se")).format(null));
		Assert.assertEquals("se4", dml.getMessage(TestMessages.LOCAL_4, new Locale("se")).format(null));
		Assert.assertEquals("default1", dml.getMessage(TestMessages.LOCAL_1, new Locale("se", "sv")).format(null));
		Assert.assertEquals("se2", dml.getMessage(TestMessages.LOCAL_2, new Locale("se", "sv")).format(null));
		Assert.assertEquals("se_sv3", dml.getMessage(TestMessages.LOCAL_3, new Locale("se", "sv")).format(null));
		Assert.assertEquals("se_sv4", dml.getMessage(TestMessages.LOCAL_4, new Locale("se", "sv")).format(null));
		Assert.assertEquals("default1", dml.getMessage(TestMessages.LOCAL_1, new Locale("se", "sv", "sms")).format(null));
		Assert.assertEquals("se2", dml.getMessage(TestMessages.LOCAL_2, new Locale("se", "sv", "sms")).format(null));
		Assert.assertEquals("se_sv3", dml.getMessage(TestMessages.LOCAL_3, new Locale("se", "sv", "sms")).format(null));
		Assert.assertEquals("se_sv_sms4", dml.getMessage(TestMessages.LOCAL_4, new Locale("se", "sv", "sms")).format(null));
		Assert.assertEquals("email", dml.getMessage(TestMessages.LOCAL_4, new Locale("se", "", "email")).format(null));
	}

	private void writeFileContent(String text, File file) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(text.getBytes());
		fos.close();
	}
}
