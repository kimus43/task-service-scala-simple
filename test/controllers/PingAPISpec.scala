package controllers

import akka.util.ByteString
import org.scalatest._
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import scala.concurrent.Future

class PingAPISpec extends AsyncWordSpec with Matchers {
  val controller = new PingController()

  def bodyContent(result: Future[Result]): String = {
    val bodyAsBytes: ByteString = contentAsBytes(result)
    bodyAsBytes.decodeString("UTF-8")
  }

  "Invoke API Ping" should {
    "response OK" in {
      val result: Future[Result] = controller.ping().apply(FakeRequest("GET", "/ping"))
      val response: String = bodyContent(result)

      assert(response == "OK")
    }
  }
}
