package com.talex.utils

import org.joml.Vector3f

class Movement {
    val cameraInc: Vector3f = Vector3f()

    private val cameraStep: Float get() = 1f

    val xMovement: Float
        get() = cameraInc.x * cameraStep

    val yMovement: Float
        get() = cameraInc.y * cameraStep

    val zMovement: Float
        get() = cameraInc.z * cameraStep

    fun reset() {
        cameraInc.set(0f, 0f, 0f)
    }
}


fun forXZ(fromX: Int, toX: Int, fromZ: Int, toZ: Int, action: (Int, Int) -> Unit) {
    for (x in fromX..toX) {
        for (z in fromZ..toZ) {
            action.invoke(x, z)
        }
    }
}