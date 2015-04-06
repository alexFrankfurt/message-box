package models.tables

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape

case class Tmp(tag: Tag, id: String) extends Table[(String, String)](tag, id){
  def user: Column[String] = column[String]("user")
  def message: Column[String] = column[String]("message")

  def * : ProvenShape[(String, String)] = (user, message)
}