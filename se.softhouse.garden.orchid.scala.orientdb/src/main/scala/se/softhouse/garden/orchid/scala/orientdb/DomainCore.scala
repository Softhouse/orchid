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

package se.softhouse.garden.orchid.scala.orientdb

import java.lang.reflect.Modifier
import java.util.List

import scala.annotation.target.field
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import com.orientechnologies.orient.core.command.OCommandRequest
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
import com.orientechnologies.orient.core.id.ORID
import com.orientechnologies.orient.core.metadata.schema.OClass
import com.orientechnologies.orient.core.metadata.schema.OType
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery

import javax.annotation.PostConstruct
import javax.annotation.Resource
import javax.jdo.annotations.Index

import DomainConversions._
import scalaj.collection.Imports._

/**
 * @author Mikael Svahn
 *
 */
class DomainObject(val className: String, val orid: ORID) {
  type IndexField = Index @field

  def toDoc: ODocument = {
    val doc = if (orid == null) new ODocument(className) else new ODocument(className, orid)
    val fields = this.getClass().getDeclaredFields()
    for (f <- fields if ((f.getModifiers & Modifier.STATIC) == 0)) {
      val method = this.getClass().getMethod(f.getName())
      if (method != null) {
        val otype = if (classOf[EmbeddedDomainObject].isAssignableFrom(method.getReturnType)) OType.EMBEDDED else OType.getTypeByClass(method.getReturnType)
        val value = method.invoke(this) match {
          case v: java.util.List[AnyRef] => v.map({ case o: DomainObject => o.toDoc; case o => o }).asJava
          case v: EmbeddedDomainObject => v.encode()
          case v => v
        }
        doc.field(f.getName(), value, otype)
      }
    }
    doc
  }
}

abstract class EmbeddedDomainObject() extends DomainObject(null, null) {
  def encode(): AnyRef = toDoc
  def decode(data: AnyRef): Unit = data match {
    case doc: ODocument => doc.decodeDocument(this)
    case _ => this
  }
}

class DomainManager[T <: DomainObject](val schemaName: String)(implicit man: Manifest[T]) {

  @transient @Resource val dbpool: DatabasePool = null

  @PostConstruct
  def init() {
    try {
      dbpool.execute(db => {

        // Create the schema in the database if it do not exist
        val oclass = db.getMetadata.getSchema.getClass(schemaName) match {
          case null => db.getMetadata.getSchema.createClass(schemaName)
          case c => c
        }

        // Add index in database for annotated fields
        val fields = man.erasure.getDeclaredFields
        fields.foreach(f => {
          val a = f.getAnnotation(classOf[Index])
          val areIndexed = oclass.areIndexed(f.getName)

          if (a != null && areIndexed == false) {
            (oclass.getProperty(f.getName) match {
              case null => oclass.createProperty(f.getName, OType.getTypeByClass(f.getType))
              case p => p
            }).createIndex(if (!a.unique.isEmpty && a.unique.toBoolean) OClass.INDEX_TYPE.UNIQUE else OClass.INDEX_TYPE.NOTUNIQUE)
          } else if (a == null && areIndexed) {
            val indexes = oclass.getInvolvedIndexes(f.getName)
            indexes.foreach(f => f.delete)
            oclass.dropProperty(f.getName)
          }
        })

        schemaCreated()
      })
    } catch {
      case e => e.printStackTrace
    }
  }

  protected def schemaCreated() {}

  def loadRecordIds(condition: String, orid: Option[String], select: Option[String => String], args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): List[ORID] = {
    dbpool.execute(db => {
      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument](select.getOrElse(selectStatement(_))(condition)))
      val ids = for (doc <- (cmd.execute(args: _*): java.util.List[ODocument])) yield {
        try {
          doc.field(orid.getOrElse("rid")).asInstanceOf[ODocument].getIdentity
        } catch {
          case _ => null
        }
      }
      ids.filter(i => i != null)
    })
  }

  def selectStatement(condition: String) = "select @rid from " + schemaName + " " + condition

  def list()(implicit db: ODatabaseDocumentTx = null): List[T] = {
    dbpool.execute(db => {
      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument]("select * from " + schemaName))
      for (doc <- (cmd.execute(): java.util.List[ODocument])) yield doc.asDomainObject[T]
    })
  }

  def listWithCondition(condition: String, args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): List[T] = {
    listWithSql("select * from " + schemaName + " where " + condition, args: _*)
  }

  def findWithCondition(condition: String, args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): Option[T] = {
    val res = listWithCondition(condition, args: _*)
    res.size match {
      case 0 => None
      case _ => Some(res.get(0))
    }
  }

  def findWithSql(sql: String, args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): Option[T] = {
    val res = listWithSql(sql, args: _*)
    res.size match {
      case 0 => None
      case _ => Some(res.get(0))
    }
  }

  def listWithSql(sql: String, args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): List[T] = {
    dbpool.execute(db => {
      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument](sql))
      for (doc <- (cmd.execute(args: _*): java.util.List[ODocument])) yield doc.asDomainObject[T]
    })
  }

  def findDocumentWithSql(sql: String, args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): Option[ODocument] = {
    val res = listDocumentWithSql(sql, args: _*)
    res.size match {
      case 0 => None
      case _ => Some(res.get(0))
    }
  }

  def listDocumentWithSql(sql: String, args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): java.util.List[ODocument] = {
    dbpool.execute(db => {
      val cmd: OCommandRequest = db.command(new OSQLSynchQuery[ODocument](sql))
      cmd.execute(args: _*).asInstanceOf[java.util.List[ODocument]]
    })
  }

  def sql(sql: String, args: AnyRef*)(implicit db: ODatabaseDocumentTx = null): Int = {
    dbpool.execute(db => {
      val cmd: OCommandRequest = db.command(new OCommandSQL(sql))
      cmd.execute(args: _*)
      1
    })
  }

  def load(id: ORID)(implicit db: ODatabaseDocumentTx = null): T = dbpool.execute(db => db.load[ODocument](id).asDomainObject[T])

  def loadMany(ids: Seq[ORID])(implicit db: ODatabaseDocumentTx = null): Seq[T] = dbpool.execute(db => ids.map(id => db.load[ODocument](id).asDomainObject[T]))

  def save(t: T): T = dbpool.execute(_ => t.toDoc.save().asDomainObject[T])

  def delete(t: T): Unit = dbpool.execute(_ => t.toDoc.delete())

  def constructBean(orid: ORID): T = man.erasure.getConstructor(classOf[ORID]).newInstance(orid).asInstanceOf[T]

}

class ExtendedODocument(val doc: ODocument) {

  def asDomainObject[T <: DomainObject](implicit man: Manifest[T]): T = {
    if (doc != null) {
      val to = man.erasure.getConstructor(classOf[ORID]).newInstance(doc.getIdentity).asInstanceOf[T]
      to match {
        case o: EmbeddedDomainObject => o.decode(doc)
        case _ => decodeDocument(to)
      }
      to
    } else {
      null.asInstanceOf[T]
    }
  }

  def decodeDocument[T <: DomainObject](obj: T = null)(implicit man: Manifest[T]): T = {
    val to = if (obj != null) obj else man.erasure.getConstructor(classOf[ORID]).newInstance(doc.getIdentity).asInstanceOf[T]
    val fields = to.getClass().getDeclaredFields()
    val methods = to.getClass().getMethods()
    doc.setLazyLoad(false)
    for (f <- fields if ((f.getModifiers & Modifier.STATIC) == 0)) {
      val method = to.getClass().getMethod(f.getName() + "_$eq", f.getType())
      val field: AnyRef = if (classOf[EmbeddedDomainObject].isAssignableFrom(f.getType)) {
        val fv = f.getType.getConstructor(classOf[ORID]).newInstance(doc.getIdentity).asInstanceOf[EmbeddedDomainObject]
        fv.decode(doc.field(f.getName()))
        fv
      } else {
        doc.field(f.getName())
      }
      if (method != null && field != null) method.invoke(to, field)
    }
    to
  }
}

object DomainConversions {

  implicit def toExtended(doc: ODocument): ExtendedODocument = new ExtendedODocument(doc)
  implicit def fromExtended(doc: ExtendedODocument): ODocument = doc.doc

}
