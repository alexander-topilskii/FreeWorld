package com.talex.utils

import org.lwjgl.system.MemoryUtil
import java.nio.FloatBuffer
import java.nio.IntBuffer

fun withVertexBuffer(vertices: FloatArray, action: (FloatBuffer) -> Unit) {
    var verticesBuffer: FloatBuffer? = null
    try {
        verticesBuffer = MemoryUtil.memAllocFloat(vertices.size)
        verticesBuffer.put(vertices).flip()

        action.invoke(verticesBuffer)
    } finally {
        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer)
        }
    }
}

fun withVertexBuffer(vertices: IntArray, action: (IntBuffer) -> Unit) {
    var verticesBuffer: IntBuffer? = null
    try {
        verticesBuffer = MemoryUtil.memAllocInt(vertices.size)
        verticesBuffer.put(vertices).flip()

        action.invoke(verticesBuffer)
    } finally {
        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer)
        }
    }
}
