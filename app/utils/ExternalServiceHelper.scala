package utils

import models.ErrorData
import play.api.libs.ws._
import play.mvc.Http

import scala.util.Try

trait ExternalServiceHelper extends HelperService {

  def handleResponse[A](response: WSResponse, errorKey: String)(implicit mf: scala.reflect.Manifest[A]): Either[ErrorData, A] = response match {
    case result if result.status.equals(Http.Status.OK) => handleSuccessResponse[A](result, errorKey)
    case errors => Left(handleErrorResponse(errors, errorKey))
  }

  def handleErrorResponse(errors: WSResponse, errorKey: String): ErrorData = {
    val errorMessage: String = errors.body.toJValue.jValueOf("error").extractOpt[ErrorTypeA] match {
      case Some(errorTypeA) => errorTypeA.message
      case None => errors.body.toJValue.jValueOf("errors").children.map(_.toJsonString) match {
        case list if list.nonEmpty => list.mkString(", ")
        case _ => errors.body.toJValue.valueOf("error")
      }
    }
    ErrorData(errorMessage, ErrorCode(errorKey), Try(errors.body.toJValue.valueOf("code").toInt).getOrElse(Http.Status.INTERNAL_SERVER_ERROR))
  }

  def handleSuccessResponse[A](data: WSResponse, errorKey: String)(implicit mf: scala.reflect.Manifest[A]): Either[ErrorData, A] = {
    data.body.toJValue.jValueOf("data").extractOpt[A] match {
      case Some(obj: A) => Right(obj)
      case None => Left(ErrorData(Message(errorKey), ErrorCode(errorKey), Http.Status.INTERNAL_SERVER_ERROR))
    }
  }
}

case class ErrorTypeA(code: Int, message: String)
