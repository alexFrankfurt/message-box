name := "MessageBox"

version := "1.0"

lazy val `messagebox` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws )

routesGenerator := InjectedRoutesGenerator

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "2.1.0",
  //"org.slf4j" % "slf4j-nop" % "1.6.4"
  "com.sachinhandiekar" % "jInstagram" % "1.0.10"
)
