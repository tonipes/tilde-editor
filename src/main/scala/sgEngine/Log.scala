package sgEngine

import java.io.PrintStream

import sgEngine.Level.Level

/**
 * Logging
 * Created by Toni on 5.12.2014.
 */

object Level extends Enumeration {
  type Level = Value
  val DEBUG = Value("DEBUG")
  val INFO = Value("INFO")
  val WARNING = Value("WARN ")
  val ERROR = Value("ERROR")


  def getColor(l: Level) = l match{
    case Level.DEBUG => Log.COLOR_PURPLE
    case Level.INFO => Log.COLOR_CYAN
    case Level.WARNING => Log.COLOR_YELLOW
    case Level.ERROR => Log.COLOR_RED
    case _ => ""
  }
}

object Log {
  private val PRINT_TAG: Boolean = true   // Toggles tag printing
  private val PRINT_COLOR: Boolean = true // Toggles color printing

  val COLOR_RESET = "\u001B[0m"
  val COLOR_BLACK = "\u001B[30m"
  val COLOR_RED = "\u001B[31m"
  val COLOR_GREEN = "\u001B[32m"
  val COLOR_YELLOW = "\u001B[33m"
  val COLOR_BLUE = "\u001B[34m"
  val COLOR_PURPLE = "\u001B[35m"
  val COLOR_CYAN = "\u001B[36m"
  val COLOR_WHITE = "\u001B[37m"

  private val logStream: Option[PrintStream] = Some(System.out)
  private var minimumLevel: Level = Level.DEBUG

  /**
   * Sets minimum logging level
   * @param level minimum level
   */
  def setMinimumLogLevel(level: Level) = minimumLevel = level

  /**
   * Returns minimum logging level
   * @return current minimum logging level
   */
  def getMinimumLogLevel(): Level = minimumLevel

  /**
   * Logs message
   * @param level   level of the message
   * @param name    name (title) of the message. Should be informative
   * @param message the body of the message. Should be informative
   */
  private def logMessage(level: Level, name:String, message:String)= {
    if(logStream.isDefined && level >= minimumLevel){

      if(PRINT_TAG){ // Prints tag
        logStream.get.print("[")
        if(PRINT_COLOR) logStream.get.print(Level.getColor(level))
        logStream.get.print(level.toString)
        if(PRINT_COLOR) logStream.get.print(COLOR_RESET)
        logStream.get.print("] ")
      }

      if(!name.isEmpty)     logStream.get.print(name)
      if(!message.isEmpty)  logStream.get.print(": " + message)
      logStream.get.print('\n')
    }
  }
  /**
   * Helper for logging
   * @param name    name (title) of the message. Should be informative
   * @param message the body of the message. Should be informative
   */
  def debug(name: String, message: String = "") = logMessage(Level.DEBUG, name, message)
  def error(name:String, message:String = "") = logMessage(Level.ERROR, name, message)
  def warning(name:String, message:String = "") = logMessage(Level.WARNING, name, message)
  def info(name:String, message:String = "") = logMessage(Level.INFO, name, message)

}


