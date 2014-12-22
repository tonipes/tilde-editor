package tilde

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import scala.io.Source

/**
 * Created by Toni on 17.12.14.
 */
/*
  Needed file(data) formats:
  - Mesh
  - Texture
  - Model (textured mesh)
  - String (and all other values)

  Also needed:
  - Way to handle all different ids(names) in development ( Resource map document?)
  - Way to handle resource releasing ( when resource is no longer needed)
  - Some kind of "loading request system"
  -
 */
object ResourceManager {

  def readFromFile(path:String): String = Source.fromFile(path).mkString
  def readImegeFromFile(path:String): BufferedImage = ImageIO.read(new File(path))
}
