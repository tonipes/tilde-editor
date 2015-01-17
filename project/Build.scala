import sbt.Keys._
import sbt._

object MyBuild extends Build { 
  val buildOrganization  = "com.tilde"
  val buildVersion       = "0.13.5"
  val buildScalaVersion  = "2.11.5"

  val rootProjectId = "tilde"

  lazy val spray = "io.spray" %%  "spray-json" % "1.3.1"

  val lwjglVersion = "3.0"

  object Settings {

    lazy val lwjglNative = {
      def os = System.getProperty("os.name").toLowerCase match {
        case "linux" => ("linux", ":")
        case "mac os x" => ("macosx", ":")
        case "windows xp" | "windows vista" | "windows 7" | "windows 8.1" => ("windows", ";")
        case _ => ("unknown","")
      }
      ("lib/lwjgl/native/" + os._1 + "/x64", os._2)
    }

    val newPath = 
        System.getProperty("java.library.path") + lwjglNative._2 + lwjglNative._1

    lazy val rootProject = Defaults.defaultSettings ++ Seq(
      fork in run := true,
      unmanagedBase := baseDirectory.value / "lib",
      organization    := buildOrganization,
      version         := buildVersion,
      scalaVersion    := buildScalaVersion,
      libraryDependencies ++= Seq(spray),
      javaOptions += "-Djava.library.path=" + newPath,
      javaOptions += "-XX:MaxGCPauseMillis=4"
    )
  }
  lazy val root = Project(id=rootProjectId, base=file("."), settings=Settings.rootProject)
}