package tilde.util

import tilde.Entity

import scala.reflect.ClassTag

class ObjectList[T:ClassTag](val cap: Int = 32){
  private var data = Array.ofDim[T](cap)
  private var size = 0

  def get(index: Int): T = data(index)

  def remove(index: Int): Unit = {
    data(index) = data.last
    data = data.dropRight(1)
  }

  def remove(obj: T): Unit =
    remove(data.indexOf(obj))

  def contains(obj: T): Boolean =
    data.contains(obj)

  def capacity = data.length

  def getSize = size

  private def grow(size: Int): Unit = {
    //println("Growing list to " + size)
    data ++= Array.ofDim[T](size - data.length)
  }

  private def grow(): Unit = grow((data.length * 2))

  def add(obj: T) = {
    //println(s"Adding object. Size: $size, cap: $capacity")
    if(capacity <= size) grow()
    data(size) = obj
    size += 1
  }

  def +=(obj: T) = add(obj)

  def clear() = {
    data = Array.ofDim[T](cap)
    size = 0
  }

  override def toString(): String = {
    var s = "["
    s += data.mkString(", ")
    s += "]"
    s
  }

  def foreach[U](f: T => U): Unit = data.foreach(f)
}
