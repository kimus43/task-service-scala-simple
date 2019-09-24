package utils

import org.scalatest.AsyncWordSpec

class MessageSpec extends AsyncWordSpec {
  "Message" should {
    "return all messages is correct" in {
      val expect = Map(
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

      assert(Message.msg == expect)
    }
  }
}
