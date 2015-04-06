package models.actors

import akka.actor.{Actor}

class ServiceActor extends Actor {
  def receive = {
    case msg: String =>

  }
}
