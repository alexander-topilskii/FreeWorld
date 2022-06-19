package com.talex.game.freeworld

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.talex.game.testgame.EmptyScreen
import com.talex.game.testgame.Maps
import java.util.*

class FreeWorldScreen : EmptyScreen {
    private lateinit var map: TiledMap
    private lateinit var camera: OrthographicCamera
    private lateinit var renderer: OrthogonalTiledMapRenderer
    private lateinit var player: Entity
    var entities = mutableListOf<Entity>()

    override fun show() {
        map = Maps.level1
        camera = OrthographicCamera()
        renderer = OrthogonalTiledMapRenderer(map, Maps.level1UnitScale)

        val tiles = Texture("tiles.png")
        val grid = TextureRegion.split(tiles, 16, 16)

        player = Entities.createPlayer("grass", grid[6][0], grid[6][1], grid[6][2], grid[6][3]).apply {
            isMe = true
            health = 10
            stamina = 5f
            damage = 4
        }

        entities.add(player)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        renderer.setView(camera)
        renderer.render()

        renderer.asBatch {
            entities
                .filter { entity ->
                    entity.drawTime != 0f || entity.health != 0
                }.forEach { entity ->
                    entity.updateDrawTime(delta)
                    player.move(delta)

                    draw(entity.image, entity.x, entity.y, entity.width, entity.height)
                }
        }

        camera.position.x = player.x
        camera.position.y = player.y
    }

    fun Entity.move(delta: Float): Boolean {
        val e = this
        val maxVelocity = 5f
        if (e.isMe) {
            val downTouched = Gdx.input.isTouched && Gdx.input.y > Gdx.graphics.height * 2 / 3
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || downTouched) {
                e.yVelocity = -1 * maxVelocity
            }
            val upTouched = Gdx.input.isTouched && Gdx.input.y < Gdx.graphics.height / 3
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
                e.yVelocity = maxVelocity
            }
            val leftTouched = Gdx.input.isTouched && Gdx.input.x < Gdx.graphics.width / 3
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftTouched) {
                e.xVelocity = -1 * maxVelocity
            }
            val rightTouched = Gdx.input.isTouched && Gdx.input.x > Gdx.graphics.width * 2 / 3
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightTouched) {
                e.xVelocity = maxVelocity
            }
        }

        e.xChange = e.xVelocity * delta
        e.yChange = e.yVelocity * delta
        e.x = e.x + e.xChange
        e.y = e.y + e.yChange
        e.xVelocity = Utils.decelerate(e.xVelocity)
        e.yVelocity = Utils.decelerate(e.yVelocity)
        return e.xChange != 0f || e.yChange != 0f
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, Utils.cameraHeight * width / height, Utils.cameraHeight)
    }
}


fun OrthogonalTiledMapRenderer.asBatch(action: SpriteBatch.() -> Unit) {
    val batch = this.batch as SpriteBatch
    batch.begin()
    action.invoke(batch)
    batch.end()
}
