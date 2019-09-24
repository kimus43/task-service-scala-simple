package utils

import scala.util.Try

object Message {
  lazy val msg: Map[String, String] = Map(
    "ok" -> "OK",
    "errors.token.incorrect" -> "Token is invalid.",
    "errors.request.validation.failed" -> "Request validation failed.",
    "errors.request.require.failed" -> "require: subject, detail",
    "status.200" -> "OK",
    "status.201" -> "Created",
    "status.400" -> "Bad Request",
    "status.401" -> "Unauthorized",
    "status.404" -> "Not Found",
    "status.500" -> "Internal Server Error",
    "status.503" -> "Service Unavailable",
    "status.unknown" -> "Unknown Error"
  )

  def apply(key: String, param: String = ""): String = {
    Try(msg(key)).getOrElse(s"The message key '$key' is not defined.").replaceAll("%s", param)
  }
}
