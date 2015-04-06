package controllers

import models.forms.{Feedback, UserSignUp, UserLogIn}
import models.tables.{Administrators, Feedbacks, Users}
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import models._
import scala.slick.driver.MySQLDriver.simple._
import Helpers.checkUser
import play.api.libs.json._

object Queries extends Controller {

  def getUsers = Action { implicit request =>
    val mesBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox", driver = "com.mysql.jdbc.Driver", user = "root")
    val users = TableQuery[Users]
    val administrators = TableQuery[Administrators]
    val user = checkUser
    user match {
      case (Some(login), Some(password)) =>
        val admin = mesBox withSession { implicit session =>
          administrators.filter(_.login === login.value).list
        }
        admin match {
          case List(_) =>
            val result = mesBox withSession {
              implicit session =>
                //        users += ("Nick92", "Nick", "nicl@mail.com")
                //        users.filter(_.name === "Michael").delete
                //        (for {
                //          user <- users
                //        } yield (user.login, user.name, user.email)).list
                users.run
            }
            print(result)
            Ok(result.toString)
          case List() => Redirect("/")
        }
      case (None, None) => Redirect("/")
    }

  }

  def users = {
    val messageBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox", driver = "com.mysql.jdbc.Driver", user = "root")
    val users = TableQuery[Users]
    messageBox withSession {implicit session =>
      (for {u <- users} yield (u.login, u.name, u.email)).list
    }
  }

  def account = Action { implicit request =>
//    Ok("Request: " + request.headers)
    userForm.bindFromRequest.fold(
      formWithErrors => BadRequest,
      value => {
        val mesBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox", driver = "com.mysql.jdbc.Driver", user = "root")
        val users = TableQuery[Users]
        val result = mesBox withSession { implicit session =>
          users.filter(_.login === value.login).filter(_.email === value.email).list
        }
        if (result.isEmpty) Ok(views.html.interfaces.interface(views.html.index("There is no such user")))
        else {
          val login = result match {
            case List((l, n, e, p)) => l
          }
          val password = result match {
            case List((l, n, e, p)) => p
          }
          val name = result match {
            case List((l, n, e, p)) => n
          }
          Ok(views.html.interfaces.accinterface(views.html.account(login, name))).withCookies(
            Cookie("login", login), Cookie("password", password)
          )
        }
      }
    )
  }

  def accCreate = Action {
    Ok(views.html.forms.signup(userFormSingUp))
  }

  val userFormSingUp = Form(
    mapping(
      "name" -> text,
      "login" -> text,
      "password" -> text,
      "email" -> email
    )(UserSignUp.apply)(UserSignUp.unapply)
  )

  val userForm = Form(
    mapping(
      "login" -> text,
      "email" -> text
    )(UserLogIn.apply)(UserLogIn.unapply)
  )

  val feedbackForm = Form(
    mapping(
      "email" -> text,
      "content" -> text
    )(Feedback.apply)(Feedback.unapply)
  )

  def feedback = Action { implicit request =>
    val user = checkUser
    feedbackForm.bindFromRequest.fold(
      formWithErrors => {
        user match {
          case (Some(login), Some(password)) => Ok(views.html.interfaces.accinterface(views.html.forms.feedback(feedbackForm)))
          case _ => Ok(views.html.interfaces.interface(views.html.forms.feedback(feedbackForm)))
        }
      },
      value => {
        val mesBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox",driver = "com.mysql.jdbc.Driver", user = "root")
        val feedbacks = TableQuery[Feedbacks]
        mesBox withSession { implicit session =>
          feedbacks += (value.email, value.content)
        }
        user match {
          case (Some(login), Some(password)) => Ok(views.html.interfaces.accinterface(views.html.account(login.value, "Your name")))
          case _ => Ok(views.html.interfaces.interface(views.html.index("Feedback sented")))
        }
      }
    )

  }

  def searchUsers = Action { implicit request =>
    val login = checkUser match {
      case (Some(login), Some(password)) => login.value
      case (None, None) => "None"
    }
    val json: JsValue = Json.obj(
      "users" -> Json.arr(
        for {u <- users.filter( _._1 != login)} yield Json.obj(
          "login" -> u._1,
          "name" -> u._2,
          "email" -> u._3
        )
      )
    )
    Ok(json.toString)
  }

}






