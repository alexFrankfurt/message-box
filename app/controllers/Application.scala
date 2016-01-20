package controllers

import javax.inject.Inject

import akka.actor.{Props, ActorSystem}
import models.actors.{ManagerActor, ClientActor, UserActor, ServiceActor}
import models.tables.Tmp
import org.jinstagram.Instagram
import org.jinstagram.auth.InstagramAuthService
import org.jinstagram.auth.model.{Verifier, Token}
import org.jinstagram.entity.users.basicinfo.{UserInfoData, UserInfo}
import org.jinstagram.auth.oauth._
import org.jinstagram.entity.users.feed.MediaFeed
import play.api._
import play.api.mvc._
import play.api.Play.current
import scala.slick.driver.MySQLDriver.simple._

class Application @Inject()
  (helpers: Helpers) extends Controller {
  import helpers._

  def index = Action { implicit request =>
//    val cookies = request.cookies
//    if (cookies.isEmpty)
//      Ok(views.html.interfaces.interface(views.html.index("Ready!")))
//    val lAndP = checkUser
//    val trylogin = request.cookies.get("login")
//    val trypassword = request.cookies.get("password")
//    val logAndPassword = (trylogin, trypassword) match {
//      case (Some(lo), Some(pa)) => (Some(lo), Some(pa))
//      case _ => (None, None)
//    }

//    val login = trylogin match {
//      case Some(lo) => Some(lo.value)
//      case None => None
//    }
//    val password = trypassword match {
//      case Some(ps) => Some(ps.value)
//      case None => None
//    }
    checkUser match {
      case (Some(lo), Some(pa)) => Ok(views.html.interfaces.accinterface(views.html.account(lo.value, "Your name")))
      case (None, None) => Ok(views.html.interfaces.interface(views.html.index("Ready!")))
    }
//    login match {
//      case Some(login) => password match {
//        case Some(password) => Ok(views.html.interfaces.accinterface(login, "Your name"))
//        case None => Ok(views.html.interfaces.interface(views.html.index("Ready!")))
//      }
//      case None => Ok(views.html.interfaces.interface(views.html.index("Ready!")))
//    }
  }
//    val messageBox = Database.forURL("jdbc:mysql://localhost:3306/messagebox", driver = "com.mysql.jdbc.Driver", user = "root")
  val system = ActorSystem("MessageSystem")
  val managerActor = system.actorOf(Props[ManagerActor], "managerActor")
  def test = Action { implicit request =>
//    val conversation = new TableQuery(Tmp(_, "HI"))
//    messageBox withSession { implicit session =>
//      conversation += ("lo", "la")
//      Ok("Ok" + conversation.run)
//    }
    val tA = system.actorOf(Props[models.Act], "myTestableActor")
    tA ! "hi"
    Ok( "Ok" )
  }

  def clearCookie = Action { implicit request =>
    Redirect("/").discardingCookies(DiscardingCookie("login"), DiscardingCookie("password"))
  }

  def socket = WebSocket.acceptWithActor[String, String] { request => out =>
    lazy val userActor = system.actorOf(Props[UserActor],request.cookies.get("login") match {
      case Some(login) => login.value
      case None => ""
    })
    ClientActor.props(out, userActor)
  }

  val inServ = new InstagramAuthService()
    .apiKey("4b3bf947e41d4bb493635920e5b34a1e")
    .apiSecret("17325634d77c4f9ba380c9c26ce07dc3")
    .callback("http://localhost:8080/handleInstagramToken/")
    .scope("likes")
    .build()
  def jinstagram = Action {


//    val instagram: Instagram.getUserInfo(userId)
//
//    val userData: UserInfoData = userInfo.getData()
//    System.out.println("id : " + userData.getId())
//    System.out.println("first_name : " + userData.getFirst_name())
//    System.out.println("last_name : " + userData.getLast_name())
//    System.out.println("profile_picture : " + userData.getProfile_picture())
//    System.out.println("website : " + userData.getWebsite())
    Ok(views.html.inst())
  }

  def listenInsta(code: String) = Action { implicit request =>

    val EMPTY_TOKEN: Token = null
    val authorizationUrl: String = inServ.getAuthorizationUrl(EMPTY_TOKEN)
    val verifier: Verifier = new Verifier(code)
    val accessToken: Token  = inServ.getAccessToken(EMPTY_TOKEN, verifier)
    val instagram = new Instagram(accessToken.getToken(), "17325634d77c4f9ba380c9c26ce07dc3", "127.0.0.1")
    val userId = "336907767"
    val myId = "469208761"
    val feeds = instagram.getUserFeeds.getData
    val at = instagram.getAccessToken
    val userInfo: UserInfo = instagram.getUserInfo(userId)
    val userData: UserInfoData = userInfo.getData
    val info =
      s"""${userData.getBio}
         |${userData.getFirst_name}
         |${userData.getLast_name}
         |${userData.getFullName}
         |${userData.getCounts.getFollows}
       """.stripMargin
    val mi = instagram.getMediaInfo(feeds.get(0).getId)
    instagram.setUserLike(feeds.get(0).getId)
    Ok(views.html.instresp(userData.getProfile_picture,info))
  }

  def testPath(path: String) = Action {
    Ok(path)
  }

}