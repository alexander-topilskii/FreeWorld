package main.java.com.talex.domain

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import main.java.com.talex.domain.domain.Domain

class FreeWorldScreen : EmptyScreen {
    val domain = Domain()
    private lateinit var map: TiledMap
    private lateinit var camera: OrthographicCamera
    private lateinit var renderer: OrthogonalTiledMapRenderer

    override fun show() {
        map = Maps.level1
        camera = OrthographicCamera()
        renderer = OrthogonalTiledMapRenderer(map, Maps.level1UnitScale)

        val tiles = Texture("tiles.png")
        val grid = TextureRegion.split(tiles, 16, 16)
        domain.onShow(grid)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        renderer.setView(camera)
        renderer.render()

        renderer.asBatch { batch ->
            domain.forAllReadyToDrawEntities(delta) { delta, entity ->
                entity.draw(batch, delta)
            }
        }

        camera.position.x = domain.player.x
        camera.position.y = domain.player.y
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, Utils.cameraHeight * width / height, Utils.cameraHeight)
    }
}


fun OrthogonalTiledMapRenderer.asBatch(action: (Batch) -> Unit) {
    val batch = this.batch as SpriteBatch
    batch.begin()
    action.invoke(batch)
    batch.end()
}
