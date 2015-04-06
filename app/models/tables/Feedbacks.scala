package models.tables

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape

class Feedbacks(tag: Tag) extends Table[(String, String)](tag, "feedback"){
  def email: Column[String] = column[String]("email")
  def content: Column[String] = column[String]("content")

  def * : ProvenShape[(String, String)] = (email, content)
}
