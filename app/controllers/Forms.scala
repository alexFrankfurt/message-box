package controllers

import javax.inject.Inject

import models.tables.Users
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import scala.slick.driver.MySQLDriver.simple._

class Forms @Inject()
  (queries: Queries, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  import queries._

  def login = Action {
    Ok(views.html.forms.login())
  }

  def signUp() = Action { implicit request =>
    userFormSingUp.bindFromRequest.fold(
      formWithErrors => Ok(views.html.interfaces.interface(views.html.forms.signup(formWithErrors))),
      value => {
        val mesBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox", driver = "com.mysql.jdbc.Driver", user = "root")
        val users = TableQuery[Users]
        val result = mesBox withSession { implicit session =>
          (users.filter(_.login === value.login) union
          users.filter(_.email === value.email) union
          users.filter(_.name === value.name)).list
        }
        if (result.isEmpty) {
          mesBox withSession { implicit session =>
            users += (value.login, value.name, value.email, value.password)
          }
          Ok(views.html.interfaces.accinterface(views.html.account(value.login, value.name))).withCookies(
            Cookie("login", value.login), Cookie("password", value.password)
          )
        }
        else Ok(views.html.interfaces.interface(views.html.index("Such user already exists")))
      }
    )
  }
}
