package helper

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.typesafe.config._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time._
import org.scalatest._
import com.github.fakemongo.Fongo

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient

trait MockServer extends AsyncWordSpec with Matchers with MockResponse with OneInstancePerTest with ScalaFutures with BeforeAndAfterAll {
  implicit val defaultPatience = PatienceConfig(timeout = Span(2, Seconds), interval = Span(5, Millis))

  val config: Config = ConfigFactory.load("test.conf")

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val wsClient: AhcWSClient = AhcWSClient()

  val fongo: Fongo = new Fongo("myInMemoryTestServer")
  val dbName: String = config.getString("mongo.database")
  val mongoCollTask: String = config.getString("mongo.collection.task")

  val Port: Int = 3333
  val Host: String = "localhost"
  val wireMockServer: WireMockServer = new WireMockServer(wireMockConfig().port(Port))

  val defaultHeader: Seq[(String, String)] = Seq(("Content-Type", "application/json"))

  def startWireMockServer(): Unit = {
    wireMockServer.start()
    WireMock.configureFor(Host, Port)
  }

  def stopWireMockServer(): Unit = wireMockServer.stop()

  override def beforeAll(): Unit = {
    startWireMockServer()
  }

  override def afterAll(): Unit = {
    stopWireMockServer()
  }

  def closeWSClient(): Unit = {
    wsClient.close()
    system.terminate()
  }
}
