package main.java.com.talex.domain.domain

import com.badlogic.gdx.graphics.g2d.TextureRegion
import main.java.com.talex.domain.Entities
import main.java.com.talex.domain.entity.Entity

class Domain(
    private val playerMovementScenario: PlayerMovementScenario = PlayerMovementScenario(),
) {
    var entities = mutableListOf<Entity>()
    val player: Entity get() = entities.first()

    fun onShow(grid: Array<Array<TextureRegion>>) {
        val player = Entities.createPlayer(
            _down = grid[6][0],
            _up = grid[6][1],
            standRight = grid[6][2],
            walkRight = grid[6][3]
        ).apply {
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
        playerMovementScenario.movePlayer(delta, entity)
    }
}

