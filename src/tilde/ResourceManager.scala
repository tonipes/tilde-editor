package tilde

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import scala.io.Source

/**
 * Created by Toni on 17.12.14.
 */
object ResourceManager {
  def readFromFile(path:String): String = Source.fromFile(path).mkString
  def readImegeFromFile(path:String): BufferedImage = ImageIO.read(new File(path))
}
