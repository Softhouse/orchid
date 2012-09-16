package se.softhouse.garden.orchid.scala.demo.vaadin;

import javax.annotation.Resource
import se.softhouse.garden.orchid.spring.DI
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource;
import se.softhouse.garden.orchid.scala.spring.OrchidMessage

object MSG {

  case class HELLO_MESSAGE(name: String) extends OrchidMessage("hello.message")
  case class COLOR_MESSAGE(colorId: String) extends OrchidMessage("color.message")
  val WINDOW_TITLE = OrchidMessage("window.title")
}
