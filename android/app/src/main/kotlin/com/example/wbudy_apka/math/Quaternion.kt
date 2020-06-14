package com.example.wbudy_apka.math

import java.lang.Math.asin
import java.lang.Math.atan2

class Quaternion(var q0: Double, var q1: Double, var q2: Double, var q3: Double) {
    fun toEulerAngle(): Vector3 {
        val q0q1 = q0*q1
        val q2q3 = q2*q3
        val q1q1 = q1*q1
        val q2q2 = q2*q2
        val x = atan2(2*(q0q1+q2q3),1-2*(q1q1+q2q2))
        val q0q2 = q0*q2
        val q3q1 = q3*q1
        val y = asin(2*(q0q2-q3q1))
        val q0q3 = q0*q3
        val q1q2 = q1*q2
        val q3q3 = q3*q3
        val z = atan2(2*(q0q3+q1q2),1-2*(q2q2+q3q3))
        return Vector3(x,y,z)
    }
}