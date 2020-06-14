package com.example.wbudy_apka.math

import java.lang.Math.cos
import java.lang.Math.sin

class Vector3(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0) {
    fun rotate(angles: Vector3): Vector3  {
        val rotX = MatrixDouble(3,3);
        rotX.data = arrayOf(
                arrayOf(1.0,0.0,0.0),
                arrayOf(0.0,cos(angles.x),-sin(angles.x)),
                arrayOf(0.0,sin(angles.x),cos(angles.x))
        )
        val rotY = MatrixDouble(3,3);
        rotY.data = arrayOf(
                arrayOf(cos(angles.y),0.0,-sin(angles.y)),
                arrayOf(0.0,1.0,0.0),
                arrayOf(sin(angles.y),0.0,cos(angles.x))
        )
        val rotZ = MatrixDouble(3,3);
        rotZ.data = arrayOf(
                arrayOf(cos(angles.z),-sin(angles.z),0.0),
                arrayOf(sin(angles.z),cos(angles.z),0.0),
                arrayOf(0.0,0.0,1.0)
        )
        val rot = rotX*rotY*rotZ
        val matrixVector = MatrixDouble(3,1)
        matrixVector.data = arrayOf(arrayOf(this.x), arrayOf(this.y), arrayOf(this.z))
        val outMatrix = rot*matrixVector
        val out = Vector3(outMatrix.get(0,0),outMatrix.get(1,0),outMatrix.get(2,0))
        return out
    }

    override fun toString(): String {
        return "Vector3(x=$x, y=$y, z=$z)"
    }
    val length: Double
    get() {
        return Math.sqrt((x*x)+(y*y)+(z*z))
    }
}