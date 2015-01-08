package tilde.entity.system

import tilde.entity.Entity
import tilde.entity.component.{PhysicsComponent, SpatialComponent}
import tilde.log.Log
import tilde.util.{Aspect, Direction, SystemUtil}

/**
 * Created by Toni on 26.12.14.
 */

class PhysicsSystem extends EntitySystem() {
  this.aspect =  new Aspect(Array(SpatialComponent,PhysicsComponent))

  override def process(e: Entity, delta: Float): Unit = {
    //Log.debug("Physics process", "" + e)
    val spatial = e.getComponent(SpatialComponent.id).get
    val phys = e.getComponent(PhysicsComponent.id).get
    val speed = phys.speed
    val ang = phys.angularSpeed

    spatial.rotate(ang.x,ang.y,ang.z)
    spatial.move(Direction.AXIS_X, speed.x)
    spatial.move(Direction.AXIS_Y, speed.y)
    spatial.move(Direction.AXIS_Z, speed.z)
  }
}
