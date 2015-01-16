package tilde.entity.system

import tilde.entity.{Entity, EntitySystem, _}
import tilde.util.{Aspect, Direction}

/**
 * Created by Toni on 26.12.14.
 */

class PhysicsSystem extends EntitySystem() {
  this.aspect =  new Aspect(Vector(SpatialComponent,PhysicsComponent))

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
