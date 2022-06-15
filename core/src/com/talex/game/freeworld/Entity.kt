package com.talex.game.freeworld

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Game
import com.talex.game.freeworld.FreeWorldScreen
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Entity : Comparable<Any?> {
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
    var isMe = false
    var isNpc = false
    var minDistance = 2
    var startLayer: String? = null
    var drawTime = -1f
    var attackTime = 0f
    var stamina = 1f
    var related: Entity? = null
    var health = 0
    var damage = 0
    override fun compareTo(o: Any?): Int {
        val e = o as Entity?
        return if (y > e!!.y) {
            -1
        } else 1
    }

    enum class Direction {
        DOWN, UP, LEFT, RIGHT
    }

    var lastDirection = Direction.DOWN
}