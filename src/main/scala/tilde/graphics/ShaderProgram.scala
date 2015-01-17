package tilde.graphics

import java.nio.FloatBuffer

import org.lwjgl.opengl.GL20._
import tilde.Log

/**
 * Created by Toni on 17.12.14.
 */

class ShaderProgram(val programID: Int,
                    val vertexShaderID: Int,
                    val fragmentShaderID: Int) {

  def setUniform(name: String,data :Float*): Unit = {
    val dataLoc = glGetUniformLocation(programID,name)
    if(data.length > 4){
      Log.error("Uniforms can't have mote than 4 values")
    }
    else {
      data.length match {
        case 1 => glUniform1f(dataLoc, data(0))
        case 2 => glUniform2f(dataLoc, data(0),data(1))
        case 3 => glUniform3f(dataLoc, data(0),data(1),data(2))
        case 4 => glUniform4f(dataLoc, data(0),data(1),data(2),data(3))
      }
    }
  }

  def setUniform(name: String, mat: FloatBuffer): Unit = {
    val dataLoc = glGetUniformLocation(programID,name)
    glUniformMatrix4(dataLoc,false,mat)
  }

  def bind() = glUseProgram(programID)

  def unbind() = glUseProgram(0)

  def dispose() = {
    unbind()
    glDetachShader(programID,vertexShaderID)
    glDetachShader(programID,fragmentShaderID)
    glDeleteShader(vertexShaderID)
    glDeleteShader(fragmentShaderID)
    glDeleteProgram(programID)
  }
}
