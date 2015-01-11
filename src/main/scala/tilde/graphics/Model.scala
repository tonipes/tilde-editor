package tilde.graphics

object Model{
  def parse(mesh: String, material: String) = new Model(mesh,material)
  def decode(model: Model) = (model.mesh,model.material)
}

class Model(val mesh: String, val material: String) {

}