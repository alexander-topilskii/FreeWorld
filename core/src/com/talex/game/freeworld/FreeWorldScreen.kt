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
import java.util.*

class FreeWorldScreen : Screen {
    var map: TiledMap? = null
    var stage: Stage? = null
    var renderer: OrthogonalTiledMapRenderer? = null
    var camera: OrthographicCamera? = null
    var entities: ArrayList<Entity?>? = null
    var player: Entity? = null
    var swipe: Entity? = null
    var hit: Entity? = null
    var time = 0f
    var hearts: ArrayList<Image?>? = null
    var bolts: ArrayList<Image?>? = null

    override fun show() {
        time = 0f
        map = TmxMapLoader().load("level1.tmx")
        renderer = OrthogonalTiledMapRenderer(map, 1 / Utils.pixelsPerTile)
        camera = OrthographicCamera()
        Utils.start = Gdx.audio.newSound(Gdx.files.internal("test.wav"))
        Utils.playerHurt = Gdx.audio.newSound(Gdx.files.internal("playerhurt.wav"))
        Utils.monsterHurt = Gdx.audio.newSound(Gdx.files.internal("monsterhurt.wav"))
        Utils.death = Gdx.audio.newSound(Gdx.files.internal("death.wav"))
        val tiles = Texture("tiles.png")
        val grid = TextureRegion.split(tiles, 16, 16)
        val treeTexture = TextureRegion(tiles, 0, 8, 16, 16)
        val cactusTexture = TextureRegion(tiles, 16, 8, 16, 16)
        val attackDownTexture = TextureRegion(tiles, 48, 0, 16, 8)
        val attackRightTexture = TextureRegion(tiles, 32, 8, 8, 16)
        val hitTexture = TextureRegion(tiles, 40, 8, 16, 16)
        val heartOffTexture = TextureRegion(tiles, 64, 0, 8, 8)
        val boltOffTexture = TextureRegion(tiles, 72, 0, 8, 8)
        val heartOnTexture = TextureRegion(tiles, 80, 0, 8, 8)
        val boltOnTexture = TextureRegion(tiles, 88, 0, 8, 8)
        val blackTexture = TextureRegion(tiles, 0, 18, 1, 1)
        player = Entities.createPlayer("grass", grid[6][0], grid[6][1], grid[6][2], grid[6][3])
        player!!.isMe = true
        player!!.health = 10
        player!!.stamina = 5f
        player!!.damage = 4
        entities = ArrayList()
        entities!!.add(player)
        swipe = Entities.createPlayer("grass",
            attackDownTexture,
            Utils.flipY(attackDownTexture),
            attackRightTexture,
            attackRightTexture)
        swipe!!.drawTime = 0f
        entities!!.add(swipe)
        hit = Entities.createPlayer("grass", hitTexture)
        hit!!.drawTime = 0f
        entities!!.add(hit)
        var zombieCount = 5
        while (zombieCount > 0) {
            val zombie = Entities.createPlayer("grass", grid[6][4], grid[6][5], grid[6][6], grid[6][7])
            zombie.isNpc = true
            zombie.health = 10
            zombie.damage = 3
            entities!!.add(zombie)
            zombieCount -= 1
        }
        var slimeCount = 5
        while (slimeCount > 0) {
            val slime = Entities.createPlayer("grass", grid[7][4], grid[7][5])
            slime.isNpc = true
            slime.health = 10
            slime.damage = 2
            entities!!.add(slime)
            slimeCount -= 1
        }
        var treeCount = 100
        while (treeCount > 0) {
            val tree = Entities.createPlayer("grass", treeTexture)
            tree.health = 12
            entities!!.add(tree)
            treeCount = treeCount - 1
        }
        var cactusCount = 10
        while (cactusCount > 0) {
            val cactus = Entities.createPlayer("desert", cactusTexture)
            cactus.damage = 3
            cactus.health = 12
            entities!!.add(cactus)
            cactusCount = cactusCount - 1
        }
        for (e in entities!!) {
            val options = Utils.getLocationOptions(e!!.width, e.height, Utils.mapWidth, Utils.mapHeight)
            Utils.shuffle(options)
            for (option in options) {
                if (Utils.isOnlyOnLayer(option, map, e.startLayer) && !Utils.isNearEntity(option,
                        entities,
                        e.minDistance)
                ) {
                    e.x = option.x
                    e.y = option.y
                    break
                }
            }
        }
        Gdx.input.inputProcessor = object : InputAdapter() {
            override fun keyDown(keycode: Int): Boolean {
                if (keycode == Input.Keys.SPACE) {
                    Entities.attack(player, entities, swipe, hit, map)
                    return true
                }
                return false
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val width = Gdx.graphics.width
                val height = Gdx.graphics.height
                val minX = width / 3
                val maxX = width * 2 / 3
                val minY = height / 3
                val maxY = height * 2 / 3
                if (screenX > minX && screenX < maxX && screenY > minY && screenY < maxY) {
                    Entities.attack(player, entities, swipe, hit, map)
                    return true
                }
                return false
            }
        }
        stage = Stage()
        val bar = Utils.createImage(stage!!, blackTexture, 0f, 0f)
        bar.setSize(Utils.stageWidth, Utils.pixelsPerTile * 2)
        hearts = ArrayList()
        bolts = ArrayList()
        var count = 0
        while (count < player!!.health) {
            val x = count * Utils.pixelsPerTile
            Utils.createImage(stage!!, heartOffTexture, x, Utils.pixelsPerTile)
            hearts!!.add(Utils.createImage(stage!!, heartOnTexture, x, Utils.pixelsPerTile))
            Utils.createImage(stage!!, boltOffTexture, x, 0f)
            bolts!!.add(Utils.createImage(stage!!, boltOnTexture, x, 0f))
            count++
        }
        Utils.start!!.play()
    }

    override fun render(delta: Float) {
        time += delta
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera!!.position.x = player!!.x
        camera!!.position.y = player!!.y
        camera!!.update()
        renderer!!.setView(camera)
        renderer!!.render()
        Collections.sort(entities)
        Entities.animateSwipe(swipe)
        Entities.animateHit(hit)
        Utils.updateImages(hearts, player!!.health.toFloat() / 10)
        Utils.updateImages(bolts, (player!!.stamina - player!!.attackTime) / player!!.stamina)
        val batch = renderer!!.batch as SpriteBatch
        batch.begin()
        for (e in entities!!) {
            if (e!!.drawTime > 0) {
                e.drawTime = Math.max(0f, e.drawTime - delta)
            } else if (e.drawTime == 0f || e.health == 0) {
                continue
            }
            if (Entities.move(e, player, delta)) {
                Entities.animate(e, time, map)
                Entities.preventMove(e, entities)
            }
            if (e.isNpc && Utils.canAttack(e, player)) {
                Entities.attack(e, entities, swipe, hit, map)
            }
            batch.draw(e.image, e.x, e.y, e.width, e.height)
        }
        batch.end()

        //stage.draw();
    }

    override fun dispose() {}
    override fun hide() {}
    override fun pause() {}
    override fun resize(width: Int, height: Int) {
        camera!!.setToOrtho(false, Utils.cameraHeight * width / height, Utils.cameraHeight)
        //stage.setViewport(Utils.stageWidth, Utils.stageWidth * height / width);
    }

    override fun resume() {}
}