package tilde.entity.system

import java.nio.FloatBuffer

import tilde.entity.Entity
import tilde.entity.aspect.Aspect
import tilde.entity.component.{ModelComponent, SpatialComponent}

import scala.collection.mutable.HashMap
import scala.collection.mutable

/**
 * Created by Toni on 25.12.14.
 */
class RenderSystem extends EntitySystem(new Aspect(SpatialComponent.id, ModelComponent.id)) {

  // Multimap for all different instances to render
  val batches = new HashMap[ModelComponent, Set[FloatBuffer]] with mutable.MultiMap[ModelComponent, FloatBuffer]

  override def process(e: Entity): Unit = {
    // Adding entity to batch
    val spatial = e.getComponent(SpatialComponent.id).get
    val model = e.getComponent(ModelComponent.id).get
    batches.addBinding(model,spatial.getFloatBuffer)
  }

  override def begin(): Unit = {

  }

  override def end(): Unit = {
    // Render all batches
    for(batch <- batches){
      val model = batch._1
      val transformations = batch._2

      //
    }
  }
}
