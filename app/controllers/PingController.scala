package controllers

import play.api.mvc.{Action, AnyContent, Controller}
import utils.Message

class PingController extends Controller {
  def ping(): Action[AnyContent] = Action(Ok(Message("ok")))
}
