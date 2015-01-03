package tilde.util

import tilde.entity.component.ComponentObject

import scala.collection.mutable

/**
 * Created by Toni on 1.1.15.
 */
class Aspect(variants: Array[ComponentObject]*) {
  private val variantBitsets = Array.tabulate[mutable.BitSet](variants.length)( i => {
    val bitset = new mutable.BitSet()
    for(c <- variants(i)){
      bitset.+=(c.bitID)
    }
    bitset
  })
  def machesWith(b: mutable.BitSet): Boolean = {
    variantBitsets.exists(a =>  (a & b) == a)
  }
}
