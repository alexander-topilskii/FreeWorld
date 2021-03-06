package main.java.com.talex.domain.entity

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.max

class Entity : Comparable<Entity?> {
    var x = 0f
    var y = 0f
    var xChange = 0f
    var yChange = 0f
    var width = 0f
    var height = 0f
    var xVelocity = 0f
    var yVelocity = 0f
    var image: TextureRegion? = null
    var down: Animation<TextureRegion?>? = null
    var up: Animation<TextureRegion?>? = null
    var left: Animation<TextureRegion?>? = null
    var right: Animation<TextureRegion?>? = null
    var minDistance = 2
    var drawTime = -1f
    var stamina = 1f
    var health = 0
    var damage = 0

    override fun compareTo(other: Entity?): Int {
        other ?: return 1
        return if (y > other.y) -1 else 1
    }

    fun updateDrawTime(delta: Float) {
        if (drawTime >= 0) {
            drawTime = max(0f, drawTime - delta)
        }
    }

    fun draw(batch: Batch, delta: Float) {
        updateDrawTime(delta)
        batch.draw(image, x, y, width, height)
    }

    enum class Direction {
        DOWN, UP, LEFT, RIGHT
    }

    var lastDirection = Direction.DOWN
}