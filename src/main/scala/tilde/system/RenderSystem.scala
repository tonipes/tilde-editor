package tilde.system

import tilde.{ModelComponent, SpatialComponent, Component, Entity, ResourceManager}
import tilde.systems.EntitySystem
import tilde.graphics.ShaderProgram
import scala.collection.immutable.BitSet
import scala.collection.mutable

/**
 * Created by Toni on 17.1.2015.
 */
class RenderSystem extends EntitySystem{
  override var compStruct: BitSet =
    BitSet.empty +
      Component.getID(classOf[SpatialComponent]) +
      Component.getID(classOf[ModelComponent])

  lazy val shader: ShaderProgram = ResourceManager.shaderPrograms("default")

  override def systemBegin(): Unit = {

  }

  override protected def process(e: Entity, delta: Float): Unit = {

  }
  
  override def systemEnd(): Unit = { // Render
    val camera = world.getTagged("camera")
    println("Rendering end")

  }
}
