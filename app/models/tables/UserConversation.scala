package models.tables

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape

class UserConversation(tag: Tag) extends Table[(String, String)](tag, "user_conversation"){
  def login: Column[String] = column[String]("login")
  def conversationId: Column[String] = column[String]("conversation_id")

  def * : ProvenShape[(String, String)] = (login, conversationId)
}
