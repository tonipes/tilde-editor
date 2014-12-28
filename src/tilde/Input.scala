package tilde

import org.lwjgl.input.{Mouse, Keyboard}
import scala.collection.mutable
import scala.collection.mutable.Map

/**
 * Created by Toni on 25.12.14.
 */
object KeyState extends Enumeration {
  type KeyState = Value
  val PRESSED,
      JUST_PRESSED,
      RELEASED,
      JUST_RELEASED = Value
}
object Input {

  private val kbState = mutable.Map[Int,KeyState.Value]().withDefault(f => KeyState.RELEASED)

  def update() = {
    updateState()
    while(Keyboard.next()){
      if(Keyboard.getEventKeyState()){
        kbState(Keyboard.getEventKey) = KeyState.JUST_PRESSED
      } else {
        kbState(Keyboard.getEventKey) = KeyState.JUST_RELEASED
      }
    }
  }

  def isPressed(key: Int):Boolean =
    kbState(key) == KeyState.JUST_PRESSED || kbState(key) == KeyState.PRESSED

  def isJustPressed(key: Int):Boolean =
    kbState(key) == KeyState.JUST_PRESSED

  def isJustReleased(key: Int):Boolean =
    kbState(key) == KeyState.JUST_RELEASED

  // Makes JUST_RELEASED be RELEASED and JUST_PRESSED be PRESSED
  private def updateState(): Unit ={
    for(k <- kbState.keys){
      if(kbState(k) == KeyState.JUST_PRESSED) kbState(k) = KeyState.PRESSED
      if(kbState(k) == KeyState.JUST_RELEASED) kbState(k) = KeyState.RELEASED
    }
  }
}
