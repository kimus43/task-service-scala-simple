package utils

import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import smk.helper.lift.JSONHelper
import smk.helper.string.StringHelper
import smk.utils.http.HttpCaller
import smk.utils.jwt.JwtCodec
import smk.validator.lift.JSONValidator
import java.util.UUID.randomUUID

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Success, Try}

trait HelperService extends Controller with JSONHelper with StringHelper with JSONValidator {
  lazy val httpCaller: HttpCaller = new HttpCaller {}

  implicit def formats = DefaultFormats

  implicit def convertListOfJValueToJValue(list: List[JValue]): JValue = JArray(list)

  implicit def injectChildren(json: JValue): List[JValue] = json.children

  implicit class ExtendRequest(req: Request[AnyContent]) {

    def getCorrelationId: String = req.headers.get("X-CorrelationId").getOrElse("")

    def json: JValue = Try(req.body.asJson.get.toJValue).getOrElse(JObject(Nil))

    def tokenStringValue: String = {
      val token: String = req.headers.get("Authorization").getOrElse("")
      Try(JwtCodec.decodeAndGetClaimSet(token).get.name.value).getOrElse("")
    }

    def tokenJSONValue: JValue = Try(net.liftweb.json.parse(req.tokenStringValue)).getOrElse(JNothing)
  }

  implicit class ExtendJsValue(json: JsValue) {
    def toJValue: JValue = Try(net.liftweb.json.parse(json.toString())).getOrElse(JObject(Nil))
  }

  implicit class MethodsOf(json: JValue) {
    def toJsValue: JsValue = Try(Json.parse(compactRender(json))).getOrElse(Json.obj())
  }

  def convertToInt(num: String): Int = Try(num.toInt).getOrElse(0)

  def dataJSON(data: JValue, code: Int = 200): JValue = JObject(Nil) ~ ("data" -> data) merge defaultJSON(code)

  def errorsJSON(messages: List[String], code: Int = 400): JValue = JObject(Nil) ~ ("errors" -> messages) merge defaultJSON(code)

  def dataResponse(data: JValue, code: String): Result = dataResponse(data, convertToInt(code))

  def dataResponse(data: JValue, code: Int = 200): Result = data match {
    case JObject(List()) => buildJsonResponse("data" -> JObject(Nil), code)
    case _ => buildJsonResponse("data" -> data, code)
  }
  def dataResponseWithMsg(data: JValue, msg: String, code: Int = 200): Result = data match {
    case JObject(List()) => buildJsonResponse("data" -> JObject(Nil), code)
    case _ => buildJsonResponse(("data" -> data) ~ ("message" -> msg), code)
  }

  def errorsResponse(message: String, errorCode: Int, httpCode: Int = 400): Result = buildJsonResponse(("error" -> (("message" -> message) ~ ("code" -> errorCode))), httpCode)

  def buildJsonResponse(data: JObject, code: Int): Result = {
    codeToResponse((data ~ ("code" -> code)).toJsValue, code)
  }

  def generateID(data: String): String =  data +"_"+ randomUUID.toString.replace("-","")

  def codeToResponse(jsValue: JsValue, code: Int): Result = code match {
    case 200 => Ok(jsValue)
    case 201 => Created(jsValue)
    case 400 => BadRequest(jsValue)
    case 401 => Unauthorized(jsValue)
    case 404 => NotFound(jsValue)
    case 500 => InternalServerError(jsValue)
    case 503 => ServiceUnavailable(jsValue)
    case _ => InternalServerError(jsValue)
  }

  private def defaultJSON(code: Int): JValue = ("code" -> code)
}

object Block extends HelperService {
  def async(action: Request[AnyContent] => Future[Result]): Action[AnyContent] = Action.async(action)

  def authorization(action: Request[AnyContent] => Future[Result]): Action[AnyContent] = Action.async { req =>
    val errorKey: String = "errors.token.incorrect"
    JwtCodec.decode(req.headers.get("Authorization").getOrElse("")) match {
      case Success(_) => action(req)
      case _ => Future(errorsResponse(Message(errorKey), ErrorCode(errorKey), 401))
    }
  }
}
