import sbt.Keys._
import sbt.Settings
import sbt._

object MyBuild extends Build { 
  val buildOrganization  = "com.tilde"
  val buildVersion       = "0.13.5"
  val buildScalaVersion  = "2.11.5"

  val rootProjectId = "tilde"

  lazy val spray = "io.spray" %% "spray-json" % "1.3.1"

  val lwjglVersion = "3.0"
  var syst = ""

  object Settings {
    lazy val lwjglNative = {
      def os = System.getProperty("os.name").toLowerCase match {
        case "linux" => ("linux", ":");
        case "mac os x" => ("macosx", ":");
        case "windows xp" | "windows vista" | "windows 7" | "windows 8.1" => ("windows", ";");
        case _ => ("unknown","")
      }
      syst = os._1
      ("lib/lwjgl/native/" + os._1 + "/x64", os._2)
    }

    val newPath = 
        System.getProperty("java.library.path") + lwjglNative._2 + lwjglNative._1

    var rootProject = Defaults.defaultSettings ++ Seq(
      fork in run := true,
      unmanagedBase := baseDirectory.value / "lib",
      organization    := buildOrganization,
      version         := buildVersion,
      scalaVersion    := buildScalaVersion,
      libraryDependencies ++= Seq(spray),
      javaOptions += "-Djava.library.path=" + newPath,
      javaOptions += "-XX:MaxGCPauseMillis=4"
      //javaOptions += "-XX:+UseConcMarkSweepGC",
      //javaOptions += "-XX:MaxHeapFreeRatio=70",
      //javaOptions += "-Xms1g",
      //javaOptions += "-Xmx1g"

      //javaOptions += "-verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetail"
      //javaOptions += "-XstartOnFirstThread"
      //javaHome := Some(file("C:/Program Files/Java/jdk1.7.0_71/"))
    )
    println(syst)
    if(syst == "macosx")
      rootProject = rootProject ++ Seq(javaOptions += "-XstartOnFirstThread")
    else if(syst== "windows")
      rootProject = rootProject ++ Seq(javaHome := Some(file("C:/Program Files/Java/jdk1.7.0_71/")))
  }

  //println(System.getProperty("java.library.path"))
  lazy val root = Project(id=rootProjectId, base=file("."), settings=Settings.rootProject)
}