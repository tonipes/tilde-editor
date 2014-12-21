package tilde.graphics

import java.nio.FloatBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import tilde.ResourceManager
import tilde.log.Log
/**
 * Created by Toni on 17.12.14.
 */
object ShaderProgram{
  def unbind() = glUseProgram(0)
}

class ShaderProgram() {
  val programID = glCreateProgram()
  var vertexShaderID = 0
  var fragmentShaderID = 0

  def attachVertexShader(name: String) = {
    // load shader source
    val shaderSource = ResourceManager.readFromFile(name)

    // create shader
    vertexShaderID = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vertexShaderID,shaderSource)

    glCompileShader(vertexShaderID)

    // Check for errors
    if(glGetShaderi(vertexShaderID,GL_COMPILE_STATUS) == GL_FALSE){
      Log.error("Couldn't create vertex shader: ",
        glGetShaderInfoLog(vertexShaderID, glGetShaderi(vertexShaderID, GL_INFO_LOG_LENGTH)))
    }

    // attach
    glAttachShader(programID, vertexShaderID)
  }

  def attachFragShader(name: String) = {
    // load shader source
    val shaderSource = ResourceManager.readFromFile(name)

    // create shader
    fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fragmentShaderID,shaderSource)

    glCompileShader(fragmentShaderID)

    // Check for errors
    if(glGetShaderi(fragmentShaderID,GL_COMPILE_STATUS) == GL_FALSE){
      Log.error("Couldn't create fragment shader: ",
        glGetShaderInfoLog(fragmentShaderID, glGetShaderi(fragmentShaderID, GL_INFO_LOG_LENGTH)))
      dispose()
    }

    // attach
    glAttachShader(programID, fragmentShaderID)
  }

  def link() = {
    glLinkProgram(programID)
    if(glGetShaderi(programID,GL_LINK_STATUS) == GL_FALSE) {
      Log.error("Couldn't link shader")
      dispose()
    }
  }

  def setUniform(name: String,data :Float*) = {
    val dataLoc = glGetUniformLocation(programID,name)
    if(data.length > 4){
      Log.error("Uniforms can't have mote than 4 values")
    }
    else{
      //Log.debug("Set uniform f", "name: " + name + ", loc: " + dataLoc)
      data.length match {
        case 1 => glUniform1f(dataLoc, data(0))
        case 2 => glUniform2f(dataLoc, data(0),data(1))
        case 3 => glUniform3f(dataLoc, data(0),data(1),data(2))
        case 4 => glUniform4f(dataLoc, data(0),data(1),data(2),data(3))
      }
    }
  }

  def setUniform(name: String, mat: FloatBuffer) = {
    val dataLoc = glGetUniformLocation(programID,name)
    //Log.debug("Set uniform matrix", "name: " + name + ", loc: " + dataLoc)
    glUniformMatrix4(dataLoc,false,mat)
  }

  def bind() = glUseProgram(programID)

  def unbind() = ShaderProgram.unbind()

  def dispose() = {
    unbind()
    glDetachShader(programID,vertexShaderID)
    glDetachShader(programID,fragmentShaderID)
    glDeleteShader(vertexShaderID)
    glDeleteShader(fragmentShaderID)
    glDeleteProgram(programID)
  }
}
