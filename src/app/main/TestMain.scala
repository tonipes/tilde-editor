package app.main

import tilde.log.Log

import scala.collection.mutable.BitSet

/**
 * Created by Toni on 26.12.14.
 */
object TestMain {
  def main(args:Array[String]): Unit = {
    val bitset = new BitSet()
    bitset.add(1)
    bitset.add(2)
    bitset.add(6)
    bitset.add(8)
    Log.debug("1",bitset.contains(1).toString)
    Log.debug("2",bitset.contains(2).toString)
    Log.debug("3",bitset.contains(3).toString)
    Log.debug("4",bitset.contains(4).toString)
    Log.debug("5",bitset.contains(5).toString)
    Log.debug("6",bitset.contains(6).toString)
    Log.debug("8",bitset.contains(8).toString)
    Log.debug("12",bitset.contains(12).toString)

    val bitset2 = new BitSet()
    bitset2.add(1)
    bitset2.add(2)
    bitset2.add(6)

    Log.debug("==",((bitset & bitset2) == bitset2).toString)

  }
}
