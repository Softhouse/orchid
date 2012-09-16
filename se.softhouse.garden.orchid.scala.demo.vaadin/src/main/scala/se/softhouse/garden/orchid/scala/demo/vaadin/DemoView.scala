package se.softhouse.garden.orchid.scala.demo.vaadin;

import vaadin.scala._

class DemoView extends VerticalLayout {
  add(Label(MSG.HELLO_MESSAGE("Smith")))
  add(Label(MSG.COLOR_MESSAGE("10")))
  add(Label(MSG.COLOR_MESSAGE("20")))
  add(Label(MSG.COLOR_MESSAGE("")))
}
