package main.java.com.talex.domain.domain

import com.badlogic.gdx.graphics.g2d.TextureRegion
import main.java.com.talex.domain.Entities
import main.java.com.talex.domain.entity.Entity

class Domain() {
    fun onShow(grid: Array<Array<TextureRegion>>) {

        player = Entities.createPlayer(
            _down = grid[6][0],
            _up = grid[6][1],
            standRight = grid[6][2],
            walkRight = grid[6][3]).apply {
            health = 10
            stamina = 5f
            damage = 4
        }

        entities.add(player)
    }

    var entities = mutableListOf<Entity>()
    private lateinit var player: Entity


}