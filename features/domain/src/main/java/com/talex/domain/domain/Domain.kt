package main.java.com.talex.domain.domain

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.TextureRegion
import main.java.com.talex.domain.Entities
import main.java.com.talex.domain.Utils
import main.java.com.talex.domain.entity.Entity

class Domain() {
    fun onShow(grid: Array<Array<TextureRegion>>) {

        val player = Entities.createPlayer(
            _down = grid[6][0],
            _up = grid[6][1],
            standRight = grid[6][2],
            walkRight = grid[6][3]).apply {
            health = 10
            stamina = 5f
            damage = 4
        }

        entities.add(0, player)
    }

    fun forAllReadyToDrawEntities(delta: Float, drawAction: (Float, Entity) -> Unit) {
        entities
            .filter { entity -> entity.drawTime != 0f || entity.health != 0 }
            .forEach {
                beforeDraw(delta, it)
                drawAction(delta, it)
            }
    }

    private fun beforeDraw(delta: Float, entity: Entity) {
        val maxVelocity = 5f
        val downTouched = Gdx.input.isTouched && Gdx.input.y > Gdx.graphics.height * 2 / 3
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || downTouched) {
            entity.yVelocity = -1 * maxVelocity
        }
        val upTouched = Gdx.input.isTouched && Gdx.input.y < Gdx.graphics.height / 3
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
            entity.yVelocity = maxVelocity
        }
        val leftTouched = Gdx.input.isTouched && Gdx.input.x < Gdx.graphics.width / 3
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftTouched) {
            entity.xVelocity = -1 * maxVelocity
        }
        val rightTouched = Gdx.input.isTouched && Gdx.input.x > Gdx.graphics.width * 2 / 3
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightTouched) {
            entity.xVelocity = maxVelocity
        }

        entity.xChange = entity.xVelocity * delta
        entity.yChange = entity.yVelocity * delta
        entity.x = entity.x + entity.xChange
        entity.y = entity.y + entity.yChange
        entity.xVelocity = Utils.decelerate(entity.xVelocity)
        entity.yVelocity = Utils.decelerate(entity.yVelocity)
    }

    var entities = mutableListOf<Entity>()
    val player: Entity get() = entities.first()


}