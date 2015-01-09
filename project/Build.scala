import sbt._
import Keys._

object Build extends Build {

  val folderName =
    if (System.getProperty("os.name").startsWith("Windows")) "windows"
    else if (System.getProperty("os.name").startsWith("Mac")) "macosx"
    else "linux"

  val libPath = Seq(s"lib/native/$folderName").mkString(java.io.File.pathSeparator)
  //javaOptions in (Test,run) += "-Xmx2G"
  //javaOptions in (Test,run) += "-Djava.library.path=lib/native/windows/"
  println("Setting up natives in " + libPath + "/")

}