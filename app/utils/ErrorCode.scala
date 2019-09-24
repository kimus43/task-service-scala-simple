package utils

import scala.util.Try

object ErrorCode {
  lazy val codes: Map[String, Int] = Map(
    "errors.token.incorrect" -> 10001,
    "errors.request.validation.failed" -> 10002,
    "errors.response.notfound" -> 10003,
    "errors.create.task" -> 10004
  )

  def apply(key: String, param: String = ""): Int = {
    Try(codes(key)).getOrElse(10000)
  }
}
