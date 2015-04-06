package models.actors

import akka.actor.{Props, ActorPath, ActorLogging, Actor}
import models.Response

object ManagerActor {
  case class CreateServiceActor(id: String)
}

class ManagerActor extends Actor with ActorLogging{
  import ManagerActor._
  import models.Message
  def receive = {
    case Message =>
      log.info("ManagerActor received message, " + self.path.toString)
    case CreateServiceActor(id) =>
      context.actorOf(Props[ServiceActor], "conversation" + id)
      sender() ! Response("creating actor")
  }
}
