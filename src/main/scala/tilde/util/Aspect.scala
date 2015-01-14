package tilde.util

import tilde.entity.ComponentObject

import scala.collection.mutable

/**
 * Created by Toni on 1.1.15.
 */
case class Aspect(variants: Vector[ComponentObject]*) {

  private val variantBitsets = Vector.tabulate[mutable.BitSet](variants.length)( i => {

    val bitset = new mutable.BitSet()
    for(c <- variants(i)){
      bitset.+=(c.bitID)
    }
    bitset
  })

  def matchesWith(b: mutable.BitSet): Boolean = {
    variantBitsets.exists(a =>  (a & b) == a)
  }
}
