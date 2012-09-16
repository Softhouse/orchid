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

package se.softhouse.garden.orchid.scala.orientdb

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import com.orientechnologies.orient.core.config.OGlobalConfiguration

/**
 * @author Mikael Svahn
 *
 */
trait DatabasePool {
  def acquire: ODatabaseDocumentTx
  def release(db: ODatabaseDocumentTx)
  def execute[T](f: ODatabaseDocumentTx => T)(implicit db: ODatabaseDocumentTx = null): T
  def txn[T](f: ODatabaseDocumentTx => T): T
}

@Component
@Profile(Array("production"))
class LocalDatabasePool extends DatabasePool {

  private val pool: ODatabaseDocumentPool = new ODatabaseDocumentPool()

  val path: String = "remote:localhost/db"

  @PostConstruct
  def postConstruct() {
    pool.setup()
    val db = new ODatabaseDocumentTx(path)
  }

  @PreDestroy
  def preDestroy() {
    pool.close()
  }

  override def acquire: ODatabaseDocumentTx = {
    pool.acquire(path, "admin", "admin")
  }

  override def release(db: ODatabaseDocumentTx) {
    db.close
  }

  override def execute[T](f: ODatabaseDocumentTx => T)(implicit db: ODatabaseDocumentTx = null): T = {
    if (db == null) {
      val db = acquire
      try {
        f(db)
      } finally {
        db.close()
      }
    } else {
      f(db)
    }
  }

  override def txn[T](f: ODatabaseDocumentTx => T): T = {
    implicit val db = acquire
    try {
      db.begin
      val r = f(db)
      db.commit
      r
    } finally {
      db.rollback
      db.close()
    }
  }
}
