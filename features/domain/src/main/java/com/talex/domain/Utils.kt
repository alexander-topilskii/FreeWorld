package main.java.com.talex.domain

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import main.java.com.talex.domain.entity.Entity
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

object Utils {
    var cameraHeight = 60f

    fun decelerate(velocity: Float): Float {
        var velocity = velocity
        val deceleration = 0.9f
        velocity *= deceleration
        if (abs(velocity) < 0.5f) {
            velocity = 0f
        }
        return velocity
    }

    fun flipX(t: TextureRegion?): TextureRegion {
        val t2 = TextureRegion(t)
        t2.flip(true, false)
        return t2
    }

}