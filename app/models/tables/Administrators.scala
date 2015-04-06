package models.tables

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.lifted.ProvenShape


class Administrators(tag: Tag) extends Table[(String, Char)](tag, "administrators") {

  def login: Column[String] = column[String]("login")
  def permissions: Column[Char] = column[Char]("permissions")

  def * : ProvenShape[(String, Char)] = (login, permissions)
}
