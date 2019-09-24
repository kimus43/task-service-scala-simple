package models

import play.mvc.Http

case class ErrorData(message: String, errorCode: Int, httpCode: Int = Http.Status.INTERNAL_SERVER_ERROR)
