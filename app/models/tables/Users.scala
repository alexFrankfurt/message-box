package models.tables

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape

class Users(tag: Tag) extends Table[(String, String, String, String)](tag, "users") {

  def login: Column[String] = column[String]("login")
  def name: Column[String] = column[String]("name")
  def email: Column[String] = column[String]("email")
  def password: Column[String] = column[String]("password")

  def * : ProvenShape[(String, String, String, String)] = (login, name, email, password)
}
