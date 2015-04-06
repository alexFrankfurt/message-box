package models.actors

import akka.actor.{ActorLogging, ActorRef, Props, Actor}
import play.api.libs.json.{JsPath, Reads, Json}
import play.api.libs.functional.syntax._
import models.Message

object ClientActor {
  def props(out: ActorRef, userActor: ActorRef) = Props(new ClientActor(out, userActor))
  implicit val messageReads: Reads[Message] = (
    (JsPath \ "id").read[String] and
    (JsPath \ "message").read[String]
  )(Message.apply _)
}

class ClientActor(out: ActorRef, userActor: ActorRef) extends Actor with ActorLogging{
  import ClientActor._
  import UserActor.RegisterOut
  import models.Response

  override def preStart() = {
    userActor ! RegisterOut(self)
  }

  def receive = {
    case msg: String =>
      val json = Json.parse(msg)
      val message = json.validate[Message].get
      userActor ! message
      out ! ("I received: " + message)
    case Response(msg: String) =>
      out ! ("checked termination" + msg)
  }
}
