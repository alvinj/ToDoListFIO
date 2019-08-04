name := "FPToDoList"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
   "org.scalaz" %% "scalaz-core" % "7.2.9",
   "org.scalaz" %% "scalaz-effect" % "7.2.9",
   "org.scalaz.stream" %% "scalaz-stream" % "0.8.6",
   "com.propensive" %% "kaleidoscope" % "0.1.0"
)

scalacOptions += "-deprecation"


