/**
 * 
 */
package se.softhouse.garden.orchid.demo.publisher;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author mis
 * 
 */
@Component("sessionBean")
@Scope(value = "session")
public class SessionBeanImpl implements SessionBean {

	private String name;

	/**
     * 
     */
	public SessionBeanImpl() {
		System.out.println("Session created");
	}

	/**
	 * @return the name
	 */
	@Override
	public String getId() {
		return this.toString();
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see se.softhouse.garden.orchid.demo.publisher.SessionBean#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

}
