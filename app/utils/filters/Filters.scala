package utils.filters

import java.time.Instant

import javax.inject.Inject
import akka.stream.Materializer
import akka.util.ByteString
import org.slf4j.LoggerFactory
import play.api.http.DefaultHttpFilters
import play.api.libs.streams.Accumulator
import play.api.mvc._
import play.filters.gzip.GzipFilter

import scala.concurrent.ExecutionContext

class LoggingFilter @Inject()(implicit ec: ExecutionContext, mat: Materializer) extends EssentialFilter {
  def apply(nextFilter: EssentialAction) = new EssentialAction {
    def apply(requestHeader: RequestHeader): Accumulator[ByteString, Result] = {
      val startTime = Instant.now.toEpochMilli
      val accumulator: Accumulator[ByteString, Result] = nextFilter(requestHeader)

      accumulator.map { result =>
      val endTime = Instant.now.toEpochMilli
      val requestTime = endTime - startTime
      val logMessage = LogMessage(
        method = requestHeader.method,
        endpoint = requestHeader.path,
        protocolVersion = requestHeader.version,
        responseCode = result.header.status,
        requestTime = requestTime,
        other = None
        )

        Response.log(requestHeader, result, logMessage)

        result.withHeaders("Request-Time" -> requestTime.toString)
      }
    }
  }

  private object Response {
    def log(implicit request: RequestHeader, result: Result, logMessage: LogMessage): Unit = {
      result.header.status match {
        case code if code >= 100 && code <= 199 => PlaySystemLogMessage.info(logMessage.copy(other = Some("Response information")).message)
        case code if code >= 200 && code <= 299 => PlaySystemLogMessage.info(logMessage.copy(other = Some("Data response is valid")).message)
        case code if code >= 300 && code <= 399 => PlaySystemLogMessage.info(logMessage.copy(other = Some("Redirect to another URL")).message)
        case code if code >= 400 && code <= 499 => PlaySystemLogMessage.error(logMessage.copy(other = Some("Reject request occur")).message)
        case code if code >= 500 && code <= 599 => PlaySystemLogMessage.error(logMessage.copy(other = Some("Something went wrong on this service")).message)
        case _ => PlaySystemLogMessage.error("Unknown HTTP status code")
      }
    }
  }

  object PlaySystemLogMessage {

    private val Logger = LoggerFactory.getLogger(this.getClass)

    def info(msg: String)(implicit request: RequestHeader) = Logger.info(makeMessage(request, msg))

    def makeMessage(req: RequestHeader, msg: String) = {
      val correlationId = req.headers.get("X-CorrelationId").getOrElse("n/a")
      s"$correlationId|$msg"
    }

    def warning(msg: String)(implicit request: RequestHeader) = Logger.warn(makeMessage(request, msg))

    def error(msg: String)(implicit request: RequestHeader) = Logger.error(makeMessage(request, msg))
  }
}

case class LogMessage(method: String, endpoint: String, protocolVersion: String, responseCode: Int, requestTime: Long, other: Option[String]) {
  def message: String = s"${method.toUpperCase} $endpoint $protocolVersion $responseCode $requestTime ${other.getOrElse("")}".trim
}
class Filters @Inject()(gzip: GzipFilter, log: LoggingFilter) extends DefaultHttpFilters(gzip, log)
