package tilde.graphics

import tilde.util.Vec4

/**
 * Created by Toni on 18.1.2015.
 */

case class Material(emission:  Vec4  = Vec4(0,0,0,0),
                    ambient:   Vec4  = Vec4(1,1,1,1),
                    diffuse:   Vec4  = Vec4(1,1,1,1),
                    specular:  Vec4  = Vec4(1,1,1,1),
                    shininess: Float = 1) {

}
