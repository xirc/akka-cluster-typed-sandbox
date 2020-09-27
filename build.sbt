name := "akka-cluster-typed-sandbox"
version := "0.1"
scalaVersion := "2.13.3"
scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Xfatal-warnings"
)

val AkkaVersion = "2.6.9"
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"