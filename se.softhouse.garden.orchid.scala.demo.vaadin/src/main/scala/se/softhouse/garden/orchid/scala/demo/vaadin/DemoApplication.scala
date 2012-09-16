package se.softhouse.garden.orchid.scala.demo.vaadin

import javax.annotation.Resource
import se.softhouse.garden.orchid.scala.spring.DI
import se.softhouse.garden.orchid.scala.spring.OrchidMessage.OrchidMessageToString
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import vaadin.scala.Application

class DemoApplication extends {
  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
} with Application with DI {
  override def main = {
    msgs.setLocale(getLocale())
    mainWindow.caption = MSG.WINDOW_TITLE
    new DemoView()
  }
}
