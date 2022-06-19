package main.java.com.talex.domain

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

object Maps {
    // level 1
    private const val level1Path = "level1.tmx"
    val level1: TiledMap by lazy { TmxMapLoader().load(level1Path) }
    private const val level1PixelsPerTile = 8f
    const val level1UnitScale = 1 / level1PixelsPerTile
}