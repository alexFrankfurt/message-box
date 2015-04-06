package models.actors


import akka.actor.{Identify, Terminated, ActorRef, Actor}
import akka.util.Timeout
import controllers.Application.system

import scala.util.{Failure, Success}

object UserActor {
  val managerActor = system.actorSelection("akka://MessageSystem/user/managerActor")
  case class RegisterOut(out: ActorRef)
}

class UserActor extends Actor {
  import UserActor._
  import models.{Message, Response}
  import ManagerActor.CreateServiceActor

  var client = context.system.deadLetters
  implicit val ec = context.dispatcher

  def receive = {
    case msg: Message =>

      val sAc = system.actorSelection("akka://MessageSystem/user/managerActor/conversation" + msg.id)
//      val serviceActor = system.actorFor("akka://MessageSystem/user/managerActor/serviceConversation" + msg.id)
      implicit val timeout: Timeout = 5000
      sAc.resolveOne().onComplete {
        case Success(actor) => client ! Response("actor running")
        case Failure(ex) =>
          client ! Response("actor stopping")
          managerActor ! CreateServiceActor(msg.id)
      }
//      if (serviceActor.isTerminated)
//        managerActor ! CreateServiceActor(serviceActor.path)

    case out: RegisterOut =>
      client = out.out
    case Response(inf) =>
      client ! Response(inf)
  }
}
