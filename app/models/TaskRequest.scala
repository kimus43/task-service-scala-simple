package models

import java.util.Date

import org.mongodb.scala._
import org.mongodb.scala.bson.BsonObjectId
import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue}

object StatusTasks {
  val pending: String = "0"
  val done: String = "1"
}

case class TaskRequest(
   subject: String,
   detail: String
) {
  def toDocument() = Document(
    "subject" -> this.subject,
    "detail" -> this.detail,
    "update_at" -> new Date
  )
}

case class StatusTask(status: String) {
  def statusValidate(): Boolean = List(StatusTasks.done,StatusTasks.pending).contains(this.status)
}

case class TaskDAO(
                    _id: BsonObjectId,
                    subject: String,
                    detail: String,
                    status: Int,
                    create_at: Date,
                    update_at: Date
) {
  def toJsValue(): JsValue =
    JsObject(Seq(
        "id" -> JsString(this._id.getValue.toString),
        "subject" -> JsString(this.subject),
        "detail" -> JsString(this.detail),
        "status" -> JsNumber(this.status),
        "create_at" -> JsString(this.create_at.toString),
        "update_at" -> JsString(this.update_at.toString)
      )
    )
}