package models.forms

case class UserLogIn(login: String, email: String)

case class UserSignUp(name: String, login: String, password: String, email: String)

case class Feedback(email: String, content: String)