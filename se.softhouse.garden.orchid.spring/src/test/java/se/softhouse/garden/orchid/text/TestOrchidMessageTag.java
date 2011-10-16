package se.softhouse.garden.orchid.text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.jsp.JspException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ContextLoader;

import se.softhouse.garden.orchid.spring.tags.OrchidArgTag;
import se.softhouse.garden.orchid.spring.tags.OrchidMessageTag;

public class TestOrchidMessageTag {

	private MockServletContext mockServletContext;
	private MockPageContext pageContext;

	@Before
	public void setup() throws IOException {
		this.mockServletContext = new MockServletContext();
		String configLocations = "/META-INF/spring/applicationContext-mock.xml";
		this.mockServletContext.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, configLocations);
		ContextLoader loader = new ContextLoader();
		loader.initWebApplicationContext(this.mockServletContext);
		this.pageContext = new MockPageContext(this.mockServletContext);
		((MockHttpServletRequest) this.pageContext.getRequest()).setContextPath("/test");
	}

	@Test
	public void testTag() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("msg0");
		messageTag.doStartTag();
		messageTag.doEndTag();
		Assert.assertEquals("FileTest message", getContentAsString());
	}

	@Test
	public void testArguments() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("msg1");
		messageTag.doStartTag();
		OrchidArgTag argTag = new OrchidArgTag();
		argTag.setParent(messageTag);
		argTag.setName("id");
		argTag.setValue("1");
		argTag.doStartTag();
		argTag.doEndTag();
		messageTag.doEndTag();
		Assert.assertEquals("FileTest message 1", getContentAsString());
	}

	@Test
	public void testMissingCode() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("missing");
		messageTag.doStartTag();
		messageTag.doEndTag();
		Assert.assertEquals("missing", getContentAsString());
	}

	@Test
	public void testLink() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("link.1");
		messageTag.doStartTag();
		messageTag.doEndTag();
		Assert.assertEquals("This is a link to /test/images", getContentAsString());
	}

	@Test
	public void testMarkdown() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("md.text1");
		messageTag.setAutoFormat("true");
		messageTag.doStartTag();
		messageTag.doEndTag();
		Assert.assertEquals("<h1>A First Level Header</h1><h2>A Second Level Header</h2>", getContentAsString());
	}

	@Test
	public void testMarkdownProperty() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("md.text3");
		messageTag.setAutoFormat("true");
		messageTag.doStartTag();
		messageTag.doEndTag();
		Assert.assertEquals("<p>This is a text</p>", getContentAsString());
	}

	@Test
	public void testMarkdownLink() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("md.text2");
		messageTag.setAutoFormat("true");
		messageTag.doStartTag();
		messageTag.doEndTag();
		Assert.assertEquals("<p>This is a link <a href=\"http://www.google.com\">http://www.google.com</a></p>", getContentAsString());
	}

	@Test
	public void testNoMarkdownLink() throws IOException, JspException {
		OrchidMessageTag messageTag = new OrchidMessageTag();
		messageTag.setPageContext(this.pageContext);
		messageTag.setCode("md.text2");
		messageTag.doStartTag();
		messageTag.doEndTag();
		Assert.assertEquals("This is a link <http://www.google.com>", getContentAsString());
	}

	private String getContentAsString() throws UnsupportedEncodingException {
		return ((MockHttpServletResponse) this.pageContext.getResponse()).getContentAsString();
	}
}
