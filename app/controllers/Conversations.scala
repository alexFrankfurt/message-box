package controllers

import models.tables.UserConversation
import play.api.mvc.{Action, Controller}
import controllers.Helpers.checkUser
import Helpers.getConversationId
import scala.slick.driver.MySQLDriver.simple._
import play.api.libs.json._


object Conversations extends Controller {

  def conversations = Action { implicit request =>
    checkUser match {
      case (None, None) => Redirect("/")
      case (Some(login), Some(password)) =>
        Ok(views.html.interfaces.conversation())
    }
  }

  def getConversations = Action { implicit request =>
    val login = checkUser match {
      case (Some(login), Some(password)) => login.value
      case (None, None) => ""
    }
    val userConversation = TableQuery[UserConversation]
    val messageBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox",driver = "com.mysql.jdbc.Driver", user = "root")
    val convs = messageBox withSession {implicit session =>
      userConversation.filter(_.login === login).list
    }
    val json = Json.arr(
      for {c <- convs} yield c._2
    )
    Ok(json)
  }

  def conversation(idm: String) = Action { implicit request =>
    Ok(views.html.conversations.conversation(idm))
  }

  def postMessage(id: String) = Action {implicit request =>
    Ok("posting message with id: " + id)
  }

  def create(that: String) = Action { implicit request =>
    val login = checkUser match {
      case (Some(login), Some(password)) => login.value
      case (None, None) => ""
    }
    val id = getConversationId
    val userConversation = TableQuery[UserConversation]
    val messageBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox",driver = "com.mysql.jdbc.Driver", user = "root")
    messageBox withSession {implicit session =>
      userConversation += (login, id.toString)
      userConversation += (that, id.toString)
    }
    Redirect("conversations")
  }

}
