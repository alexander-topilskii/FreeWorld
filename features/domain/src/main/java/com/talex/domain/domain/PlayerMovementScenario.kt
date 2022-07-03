package main.java.com.talex.domain.domain

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import main.java.com.talex.domain.Utils
import main.java.com.talex.domain.entity.Entity

class PlayerMovementScenario() {

    fun movePlayer(delta: Float, player: Entity) {
        val maxVelocity = 5f
        val downTouched = Gdx.input.isTouched && Gdx.input.y > Gdx.graphics.height * 2 / 3
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || downTouched) {
            player.yVelocity = -1 * maxVelocity
        }
        val upTouched = Gdx.input.isTouched && Gdx.input.y < Gdx.graphics.height / 3
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || upTouched) {
            player.yVelocity = maxVelocity
        }
        val leftTouched = Gdx.input.isTouched && Gdx.input.x < Gdx.graphics.width / 3
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || leftTouched) {
            player.xVelocity = -1 * maxVelocity
        }
        val rightTouched = Gdx.input.isTouched && Gdx.input.x > Gdx.graphics.width * 2 / 3
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || rightTouched) {
            player.xVelocity = maxVelocity
        }

        player.xChange = player.xVelocity * delta
        player.yChange = player.yVelocity * delta
        player.x = player.x + player.xChange
        player.y = player.y + player.yChange
        player.xVelocity = Utils.decelerate(player.xVelocity)
        player.yVelocity = Utils.decelerate(player.yVelocity)
    }
}