package tilde

import org.lwjgl.input.Keyboard
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

  private val kbState = Map[Int,KeyState.Value]().withDefault(f => KeyState.RELEASED)

  def update() = {
    updateState()
    while(Keyboard.next()){
      if(Keyboard.getEventKeyState()){
        Keyboard.getEventKey match{
          case Keyboard.KEY_W => kbState(Keyboard.KEY_W) = KeyState.JUST_PRESSED
          case Keyboard.KEY_A => kbState(Keyboard.KEY_A) = KeyState.JUST_PRESSED
          case Keyboard.KEY_S => kbState(Keyboard.KEY_S) = KeyState.JUST_PRESSED
          case Keyboard.KEY_D => kbState(Keyboard.KEY_D) = KeyState.JUST_PRESSED
          case Keyboard.KEY_UP => kbState(Keyboard.KEY_UP) = KeyState.JUST_PRESSED
          case Keyboard.KEY_DOWN => kbState(Keyboard.KEY_DOWN) = KeyState.JUST_PRESSED
          case Keyboard.KEY_RIGHT => kbState(Keyboard.KEY_RIGHT) = KeyState.JUST_PRESSED
          case Keyboard.KEY_LEFT => kbState(Keyboard.KEY_LEFT) = KeyState.JUST_PRESSED
          case Keyboard.KEY_Q=> kbState(Keyboard.KEY_Q) = KeyState.JUST_PRESSED
          case Keyboard.KEY_E => kbState(Keyboard.KEY_E) = KeyState.JUST_PRESSED
          case Keyboard.KEY_R=> kbState(Keyboard.KEY_R) = KeyState.JUST_PRESSED
          case Keyboard.KEY_F => kbState(Keyboard.KEY_F) = KeyState.JUST_PRESSED
          case Keyboard.KEY_LSHIFT => kbState(Keyboard.KEY_LSHIFT) = KeyState.JUST_PRESSED
          case _ => {}
        }
      } else {
        Keyboard.getEventKey match{
          case Keyboard.KEY_W => kbState(Keyboard.KEY_W) = KeyState.JUST_RELEASED
          case Keyboard.KEY_A => kbState(Keyboard.KEY_A) = KeyState.JUST_RELEASED
          case Keyboard.KEY_S => kbState(Keyboard.KEY_S) = KeyState.JUST_RELEASED
          case Keyboard.KEY_D => kbState(Keyboard.KEY_D) = KeyState.JUST_RELEASED
          case Keyboard.KEY_UP => kbState(Keyboard.KEY_UP) = KeyState.JUST_RELEASED
          case Keyboard.KEY_DOWN => kbState(Keyboard.KEY_DOWN) = KeyState.JUST_RELEASED
          case Keyboard.KEY_RIGHT => kbState(Keyboard.KEY_RIGHT) = KeyState.JUST_RELEASED
          case Keyboard.KEY_LEFT => kbState(Keyboard.KEY_LEFT) = KeyState.JUST_RELEASED
          case Keyboard.KEY_Q=> kbState(Keyboard.KEY_Q) = KeyState.JUST_RELEASED
          case Keyboard.KEY_E => kbState(Keyboard.KEY_E) = KeyState.JUST_RELEASED
          case Keyboard.KEY_R=> kbState(Keyboard.KEY_R) = KeyState.JUST_RELEASED
          case Keyboard.KEY_F => kbState(Keyboard.KEY_F) = KeyState.JUST_RELEASED
          case Keyboard.KEY_LSHIFT => kbState(Keyboard.KEY_LSHIFT) = KeyState.JUST_RELEASED
          case _ => {}
        }
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
