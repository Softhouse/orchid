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

import se.softhouse.garden.orchid.spring.SpringBeanConfigurer

/**
 * This trait provides functionality to inject spring beans into an object. This is an alternative
 * to use the @Configurable annotation.
 *
 * @author Mikael Svahn
 *
 */
trait DI {
  SpringBeanConfigurer.inject(this);
}

/**
 * Lookup a bean in the SpringContext
 *
 * @return The found bean
 */
object Bean {
  def bean[A]()(implicit man: Manifest[A]) = SpringBeanConfigurer.bean(man.erasure).asInstanceOf[A]
}