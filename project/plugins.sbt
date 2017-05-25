addSbtPlugin("com.typesafe.sbt" % "sbt-aspectj" % "0.10.6")

// https://github.com/sbt/sbt/issues/1931
libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.25"
