package tilde.graphics

/**
 * Created by Toni on 14.12.2014.
 */
class Pixmap(val data: Array[Byte], val height: Int, val width: Int) {

  def this(length: Int, height: Int) = this(Array.fill[Byte](length*height)(0.toByte),height,length)

}
