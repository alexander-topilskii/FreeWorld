package com.talex.game.testgame

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.talex.game.freeworld.Utils

class TestScreen : EmptyScreen {
    private lateinit var map: TiledMap
    private lateinit var camera: OrthographicCamera
    private lateinit var renderer: OrthogonalTiledMapRenderer

    override fun show() {
        map = Maps.level1
        camera = OrthographicCamera()
        renderer = OrthogonalTiledMapRenderer(map, Maps.level1UnitScale)
    }

    override fun render(delta: Float) {
        camera.update()
        renderer.setView(camera)
        renderer.render()
    }

    override fun resize(width: Int, height: Int) {
        camera.setToOrtho(false, Utils.cameraHeight * width / height, Utils.cameraHeight)
    }
}
