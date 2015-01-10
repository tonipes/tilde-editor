import sbt._
import Keys._

object Build extends Build {

    if (System.getProperty("os.name").startsWith("Windows")) setPaths("windows","C:/Program Files/Java/jdk1.7.0_71/")
    else if (System.getProperty("os.name").startsWith("Mac")) setPaths("macosx","")
    else setPaths("linux","")

	def setPaths(folderName: String, homePath: String) = {
		val libPath = Seq(s"lib/native/$folderName/").mkString(java.io.File.pathSeparator)
		javaOptions in (Test,run) += "-Djava.library.path=libPath"
		javaHome := Some(file(homePath)) 
		fork := true
	}
}