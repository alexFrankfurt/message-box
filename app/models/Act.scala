package models

import akka.actor.{Actor, ActorLogging}


class Act extends Actor with ActorLogging {
  def receive = {
    case "hi" => log.info("My system works!")
  }
}
