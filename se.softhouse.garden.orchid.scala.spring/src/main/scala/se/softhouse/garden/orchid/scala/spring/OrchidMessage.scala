/**
 * Copyright (c) 2012, Mikael Svahn, Softhouse Consulting AB
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

package se.softhouse.garden.orchid.scala.spring

import org.springframework.context.MessageSourceResolvable
import se.softhouse.garden.orchid.spring.text.OrchidMessageSourceBuilder
import se.softhouse.garden.orchid.commons.text.OrchidDefaultMessageCode
import org.springframework.beans.factory.annotation.Configurable
import javax.annotation.Resource
import se.softhouse.garden.orchid.spring.text.OrchidLocalizedMesageSource
import se.softhouse.garden.orchid.spring.text.OrchidMessageSource
import java.util.Date

/**
 * This class is a factory class for creating orchid messages from text bundels using the current
 * locale set on the OrchidLocalizedMesageSource bean. The toString method uses the member variables of
 * the instance to format the message. Only the member variables of the concrete class is used, i.e. the members
 * of the extended classes can not be used in the message formatting.
 *
 * @author Mikael Svahn
 */
class OrchidMessage(val key: String, val default: Option[String] = None) {
  override def toString() = OrchidMessage.format(key, default, this.getClass().getDeclaredFields() map { f => f.setAccessible(true); (f.getName(), f.get(this)) }: _*)
}

/**
 * Help object for OrchidMessage class to create texts from the current locale bundle.
 */
object OrchidMessage extends {
  @transient @Resource val msgs: OrchidLocalizedMesageSource = null
} with DI {
  implicit def OrchidMessageToString(m: OrchidMessage): String = {
    m.toString
  }

  def format(code: String, default: Option[String], args: Tuple2[String, Any]*): String = {
    val msg = OrchidMessageSource.code(code, default.getOrElse(code))
    args.foreach(t => msg.arg(t._1, t._2))
    msgs.get(msg)
  }

  def apply(key: String) = new OrchidMessage(key)

}

