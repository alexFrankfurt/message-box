package controllers

import play.api.mvc.{AnyContent, Request}

class Helpers {

  var conversationId = 100

  def checkUser(implicit request: Request[AnyContent]) = {
    val trylogin = request.cookies.get("login")
    val trypassword = request.cookies.get("password")
    (trylogin, trypassword) match {
      case (Some(lo), Some(pa)) => (Some(lo), Some(pa))
      case _ => (None, None)
    }
  }

  def getConversationId = {
    val currentId = conversationId
    conversationId += 1
    currentId
  }

}
