package se.softhouse.garden.orchid.spring.text;

import javax.annotation.Resource;

import se.softhouse.garden.orchid.spring.DI;

public class OrchidMessageConstants {
	@SuppressWarnings("unused")
	private final Void di = DI.inject(this);

	@Resource
	protected OrchidLocalizedMesageSource msg;

}
