package controllers

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent}
import services._
import models._
import net.liftweb.json.JObject
import play.api.libs.json.JsValue
import utils.{ErrorCode, HelperService, Message}

import scala.concurrent.{ExecutionContext, Future}

class TaskController @Inject() (taskService: TaskService)(implicit ec: ExecutionContext) extends HelperService {

  def create(): Action[AnyContent] = Action.async {implicit request =>
    request.body.asJson match {
      case Some(json: JsValue) => json.toJValue.extractOpt[TaskRequest] match {
        case Some(taskReq: TaskRequest) => taskService.create(taskReq)
        case None => Future.successful(errorsResponse(Message("errors.request.require.failed"),ErrorCode("errors.request.validation.failed")))
      }
      case None => Future.successful(errorsResponse(Message("status.400"),ErrorCode("errors.request.validation.failed")))
    }

  }

  def getOne(taskId: String): Action[AnyContent] = Action.async {implicit request =>
    if (taskId.nonEmpty)
      taskService.getById(taskId)
    else
      Future.successful(errorsResponse(Message("status.404"),ErrorCode("errors.response.notfound"), 404))
  }

  def getAll(): Action[AnyContent] = Action.async {implicit request =>
      taskService.getAll()
  }

  def delete(taskId: String): Action[AnyContent] = Action.async {implicit request =>
      taskService.delete(taskId)
  }


  def update(taskId: String): Action[AnyContent] = Action.async {implicit request =>
    request.body.asJson match {
      case Some(json: JsValue) => taskService.update(taskId, json.toJValue)
      case None => Future.successful(errorsResponse(Message("status.400"),ErrorCode("errors.request.validation.failed")))
    }
  }
}

