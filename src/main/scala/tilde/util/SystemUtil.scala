package tilde.util

import tilde.entity.ComponentObject

import scala.collection.mutable

/**
 * Created by Toni on 26.12.14.
 */
object SystemUtil {
  def createAspect(comps: ComponentObject*): mutable.BitSet = {
    val bitset = new mutable.BitSet()
    for(c <- comps){
      bitset.+=(c.bitID)
    }
    bitset
  }
}
