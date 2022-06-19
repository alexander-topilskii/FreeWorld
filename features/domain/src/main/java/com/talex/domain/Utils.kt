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
    var pixelsPerTile = 8f
    var mapWidth = 50
    var mapHeight = 50
    var cameraHeight = 60f
    var stageWidth = 150f
    var start: Sound? = null
    var playerHurt: Sound? = null
    var monsterHurt: Sound? = null
    var death: Sound? = null

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

    fun flipY(t: TextureRegion?): TextureRegion {
        val t2 = TextureRegion(t)
        t2.flip(false, true)
        return t2
    }

    fun isOnLayer(e: Entity?, map: TiledMap?, layerName: String?): Boolean {
        val layer = map!!.layers[layerName] as TiledMapTileLayer
        val endX = e!!.x + e.width
        val endY = e.y + e.height
        var x = e.x.toInt()
        while (x < endX) {
            var y = e.y.toInt()
            while (y < endY) {
                if (layer.getCell(x, y) == null) {
                    return false
                }
                y += 1
            }
            x += 1
        }
        return true
    }

    fun isOnlyOnLayer(e: Entity?, map: TiledMap?, layerName: String?): Boolean {
        var count = map!!.layers.count
        while (count > 0) {
            count -= 1
            val name = map.layers[count].name
            if (name == "grass") {
                continue
            }
            val isRequired = layerName == name
            if (isOnLayer(e, map, name) != isRequired) {
                return false
            }
        }
        return true
    }

    fun isNearEntity(e: Entity?, e2: Entity?, minDistance: Int): Boolean {
        if (e === e2 || e2!!.drawTime != -1f || e!!.health == 0 || e2.health == 0) {
            return false
        }
        val xDiff = abs(e.x - e2.x)
        val yDiff = abs(e.y - e2.y)
        return xDiff < minDistance && yDiff < minDistance
    }

    fun isNearEntity(e: Entity?, entities: List<Entity?>?, minDistance: Int): Boolean {
        for (e2 in entities!!) {
            if (isNearEntity(e, e2, minDistance)) {
                return true
            }
        }
        return false
    }

    fun getLocationOptions(width: Float, height: Float, mapWidth: Int, mapHeight: Int): MutableList<Entity> {
        val options = mutableListOf<Entity>()
        val endX = mapWidth - width.toInt()
        val endY = mapHeight - height.toInt()
        var x = 0
        while (x < endX) {
            var y = 0
            while (y < endY) {
                val option = Entity()
                option.x = x.toFloat()
                option.y = y.toFloat()
                option.width = width
                option.height = height
                options.add(option)
                y += 1
            }
            x += 1
        }
        return options
    }

    fun isOnMap(e: Entity?): Boolean {
        return e!!.x >= 0 && e.x < mapWidth - 1 && e.y >= 0 && e.y < mapHeight - 1
    }

    fun random(): Int {
        return (Math.random() * 2 - 1).roundToInt()
    }

    fun createImage(s: Stage, t: TextureRegion?, x: Float, y: Float): Image {
        val image = Image(t)
        image.setPosition(x, y)
        s.addActor(image)
        return image
    }

    fun canAttack(e: Entity?, e2: Entity?): Boolean {
        return if (e!!.isNpc && !e2!!.isMe) {
            false
        } else e.stamina - e.attackTime >= 1 && isNearEntity(e, e2, 2)
    }

    fun updateImages(images: ArrayList<Image?>?, percent: Float) {
        var num = images!!.size * percent
        for (i in images) {
            i!!.isVisible = num >= 1
            num--
        }
    }

    private fun swap(list: MutableList<Entity>?, idx1: Int, idx2: Int) {
        val o1 = list!![idx1]
        list[idx1] = list[idx2]
        list[idx2] = o1
    }

    fun shuffle(objects: MutableList<Entity>?) {
        for (i in objects!!.size downTo 2) {
            swap(objects, i - 1, Random().nextInt(i))
        }
    }
}