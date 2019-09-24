package services


import java.util.Date

import javax.inject.Inject
import org.mongodb.scala._

import scala.concurrent.{Await, ExecutionContext, Future}
import models._
import net.liftweb.json.JValue
import org.mongodb.scala.bson.BsonObjectId

import scala.concurrent.duration._
import utils.{ErrorCode, HelperService, Message}
import play.api.mvc._


class TaskService @Inject() (db: MongodbClient)(implicit ec: ExecutionContext) extends HelperService{

  lazy val taskCollection: MongoCollection[TaskDAO] = db.taskCollection()

  def create(task: TaskRequest): Future[Result] = Future {
    val bsonId: BsonObjectId = new BsonObjectId
//    val taskId: String = db.Collection.tasks + "_" + bsonId.getValue.toString
    val record: TaskDAO = TaskDAO(bsonId, task.subject, task.detail, 0, new Date, new Date)
    val resData = record.toJsValue()
    try {
      Await.result(taskCollection.insertOne(record).toFuture, 0.5 second)
      dataResponse(resData.toJValue)
    } catch {
      case _: Exception => errorsResponse(Message("status.500"),ErrorCode("errors.create.task"), 500)
    }
  }

  def getById(taskId: String): Future[Result] = {
    val query = Document("_id"-> BsonObjectId(taskId))
    taskCollection.find(query).head.map { result =>
      if (result.isInstanceOf[TaskDAO])
        dataResponse(result.toJsValue().toJValue)
      else
        errorsResponse(Message("status.404"), ErrorCode("errors.response.notfound"), 404)
    }
  }

  def getAll(): Future[Result] = {
    taskCollection.find().toFuture.map { resultList =>
      val responseList: List[JValue] = resultList.map(r=>r.toJsValue().toJValue).toList
      dataResponse(responseList)
    }
  }

  def delete(taskId: String): Future[Result] = {
    val query = Document("_id"-> BsonObjectId(taskId))
    taskCollection.findOneAndDelete(query).toFuture.map { result =>
      if (result.isInstanceOf[TaskDAO])
        dataResponseWithMsg(result.toJsValue().toJValue,"deleted")
      else
        errorsResponse(Message("status.404"), ErrorCode("errors.response.notfound"), 404)
    }
  }


  def update(taskId: String, request: JValue): Future[Result] = {

    if (request.valueOf("subject").nonEmpty |
        request.valueOf("detail").nonEmpty  |
        request.valueOf("status").nonEmpty) {

      if(request.valueOf("status").nonEmpty){
        request.extractOpt[StatusTask] match {
          case Some(s: StatusTask) =>
            if (s.statusValidate()) updateFiled(taskId, request)
            else Future.successful(errorsResponse("status require: [0,1]", ErrorCode("errors.request.validation.failed"), 400))
          case None => Future.successful(errorsResponse("validate status failed", ErrorCode("errors.request.validation.failed"), 400))
        }
      } else updateFiled(taskId, request)

    } else Future.successful(errorsResponse("require: [subject, detail, status]", ErrorCode("errors.request.validation.failed"), 400))
  }

  private def updateFiled(taskId: String, request: JValue): Future[Result] = {
    val exceptKey: List[String] = List("subject","detail","status","create_at","update_at")
    val query = Document("_id"-> BsonObjectId(taskId))
    val updateData = updateValidate(request)
    val updateQuery = Document("$set" -> updateData)
    taskCollection.findOneAndUpdate(query, updateQuery).head.map { result =>
      if (result.isInstanceOf[TaskDAO])
        dataResponseWithMsg(result.toJsValue().toJValue.except(exceptKey),"updated")
      else
        errorsResponse(Message("status.404"), ErrorCode("errors.response.notfound"), 404)
    }
  }

  private def updateValidate(request: JValue): Document = {
    val subject = request.valueOf("subject")
    val detail = request.valueOf("detail")
    val status = request.valueOf("status")
    val subDoc = if(subject.nonEmpty) Document("subject" -> subject) else Document()
    val detailDoc = if(detail.nonEmpty) Document("detail" -> detail) else Document()
    val statusDoc = if(status.nonEmpty) Document("status" -> status.toInt) else Document()
    subDoc ++ detailDoc ++ statusDoc ++ Document("update_at" -> new Date())
  }

}

