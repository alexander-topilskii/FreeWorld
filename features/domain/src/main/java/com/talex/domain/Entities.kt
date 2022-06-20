package main.java.com.talex.domain

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import main.java.com.talex.domain.entity.Entity

object Entities {

    fun createPlayer(
        _down: TextureRegion?,
        _up: TextureRegion?,
        standRight: TextureRegion?,
        walkRight: TextureRegion?,
        duration: Float = 0.2f,
    ): Entity {
        return Entity().apply {
            width = 2f
            height = 2f
            xVelocity = 0f
            yVelocity = 0f
            minDistance = 5

            down = Animation<TextureRegion?>(duration, _down, Utils.flipX(_down))
            up = Animation<TextureRegion?>(duration, _up, Utils.flipX(_up))
            right = Animation<TextureRegion?>(duration, standRight, walkRight)
            left = Animation<TextureRegion?>(duration, Utils.flipX(standRight), Utils.flipX(walkRight))
            image = _down
        }
    }
}