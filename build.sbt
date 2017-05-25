import com.typesafe.sbt.SbtAspectj.AspectjKeys.weaverOptions
import sbt.Keys._
import sbt._

name := "future-instrumentation"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  "org.apache.logging.log4j" % "log4j-core" % "2.8.2",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.8.2",
  "org.apache.logging.log4j" % "log4j-api" % "2.8.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.8.1"
)

aspectjSettings

fork in run := true
fork in test := true

products in Compile <++= products in Aspectj
javaOptions in run <++= weaverOptions in Aspectj
