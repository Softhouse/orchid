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

package se.softhouse.garden.orchid.spring.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This is an alternative implementation to the <spring:url> tag which can be
 * accessed as follows ${url['/a/b/c']} <br>
 * <br>
 * Note that this bean requires that you have defined the following two beans in
 * applicationContext.xml<br>
 * <code>
 * 	<bean id="httpServletResponse" class="se.softhouse.garden.orchid.spring.context.HttpServletResponseFactoryBean" scope="request">
 * 		<aop:scoped-proxy/>
 * 	</bean>
 * 	<bean id="responseInScopeFilter" class="se.softhouse.garden.orchid.spring.context.ResponseInScopeFilter" />
 * </code>
 * 
 * @author Mikael Svahn
 * 
 */
@Component("url")
@Scope("request")
public class UrlBean extends AbstractStringMapBean {

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Override
	public StringMap get(Object link) {
		return new UrlMap(link.toString(), this.request, this.response);
	}
}
