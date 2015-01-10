name := "TildeEngine"

version := "1.0"

scalaVersion := "2.11.5"

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"

resolvers += "Typesafe Public Repo" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies += "io.argonaut" %% "argonaut" % "6.0.4"

libraryDependencies += "org.lwjgl.lwjgl" % "lwjgl" % "2.9.1"

libraryDependencies += "org.lwjgl.lwjgl" % "lwjgl_util" % "2.9.1"

libraryDependencies += "org.lwjgl.lwjgl" % "lwjgl-platform" % "2.9.1"

fork in (Test,run) := true

javaHome := Some(file("C:/Program Files/Java/jdk1.7.0_71/"))