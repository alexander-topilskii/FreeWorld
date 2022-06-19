package com.talex.game.freeworld

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Animation
import java.util.ArrayList
import kotlin.math.max

object Entities {

    fun createPlayer(
        _startLayer: String?,
        _down: TextureRegion?,
        _up: TextureRegion?,
        standRight: TextureRegion?,
        walkRight: TextureRegion?,
        duration: Float = 0.2f,
    ): Entity {
        return Entity().apply {
            width = 2f
            height = 2f
            xVelocity = 0f
            yVelocity = 0f
            minDistance = 5
            startLayer = _startLayer

            down = Animation<TextureRegion?>(duration, _down, Utils.flipX(_down))
            up = Animation<TextureRegion?>(duration, _up, Utils.flipX(_up))
            right = Animation<TextureRegion?>(duration, standRight, walkRight)
            left = Animation<TextureRegion?>(duration, Utils.flipX(standRight), Utils.flipX(walkRight))
            image = _down
        }
    }

    fun createPlayer(
        _startLayer: String?,
        _down: TextureRegion?,
        _up: TextureRegion?,
        duration: Float = 0.2f,
        anim: Animation<TextureRegion?> = Animation<TextureRegion?>(duration, _down, _up),
    ): Entity {
        return Entity().apply {
            width = 2f
            height = 2f
            xVelocity = 0f
            yVelocity = 0f
            minDistance = 5
            startLayer = _startLayer

            down = anim
            up = anim
            right = anim
            left = anim
            image = _down
        }
    }

    fun createPlayer(_startLayer: String?, img: TextureRegion?): Entity {
        return Entity().apply {
            width = 2f
            height = 2f
            xVelocity = 0f
            yVelocity = 0f
            startLayer = _startLayer
            image = img
        }
    }

    fun move(e: Entity?, player: Entity?, delta: Float): Boolean {
        val maxVelocity = 5f
        val maxNpcVelocity = 3f
        if (e!!.isMe) {
            e.attackTime = Math.max(0f, e.attackTime - delta)
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
        } else if (e.isNpc) {
            if (e.attackTime == 0f) {
                e.attackTime = 1f
            }
            e.attackTime = Math.max(0f, e.attackTime - delta)
            if (Utils.isNearEntity(e, player, 6)) {
                val xDiff = e.x - player!!.x
                val yDiff = e.y - player.y
                val closeEnough = 1.5f
                if (xDiff > closeEnough) {
                    e.xVelocity = -1 * maxNpcVelocity
                } else if (xDiff < -1 * closeEnough) {
                    e.xVelocity = maxNpcVelocity
                }
                if (yDiff > closeEnough) {
                    e.yVelocity = -1 * maxNpcVelocity
                } else if (yDiff < -1 * closeEnough) {
                    e.yVelocity = maxNpcVelocity
                }
            } else if (e.attackTime == 0f) {
                e.xVelocity = maxNpcVelocity * Utils.random()
                e.yVelocity = maxNpcVelocity * Utils.random()
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

    fun animate(e: Entity?, time: Float, map: TiledMap?) {
        var anim: Animation<*>? = null
        if (e!!.yVelocity != 0f) {
            if (e.yVelocity > 0) {
                anim = e.up
                e.lastDirection = Entity.Direction.UP
            } else {
                anim = e.down
                e.lastDirection = Entity.Direction.DOWN
            }
        } else if (e.xVelocity != 0f) {
            if (e.xVelocity > 0) {
                anim = e.right
                e.lastDirection = Entity.Direction.RIGHT
            } else {
                anim = e.left
                e.lastDirection = Entity.Direction.LEFT
            }
        }
        if (anim != null) {
            e.image = anim.getKeyFrame(time, true) as TextureRegion
        }
        if (Utils.isOnLayer(e, map, "water")) {
            e.image = TextureRegion(e.image)
            e.image!!.regionHeight = Utils.pixelsPerTile.toInt()
        }
        e.width = e.image!!.regionWidth / Utils.pixelsPerTile
        e.height = e.image!!.regionHeight / Utils.pixelsPerTile
    }

    fun preventMove(e: Entity?, entities: List<Entity?>?) {
        if (Utils.isNearEntity(e, entities, 1) || !Utils.isOnMap(e)) {
            e!!.x = e.x - e.xChange
            e.y = e.y - e.yChange
            e.xVelocity = 0f
            e.yVelocity = 0f
        }
    }

    fun attack(e: Entity?, entities: List<Entity?>?, swipe: Entity?, hit: Entity?, map: TiledMap?) {
        if (Utils.isOnLayer(e, map, "water")) {
            return
        }
        var victim: Entity? = null
        for (e2 in entities!!) {
            if (Utils.canAttack(e, e2)) {
                var xIsGood = false
                var yIsGood = false
                when (e!!.lastDirection) {
                    Entity.Direction.DOWN -> {
                        xIsGood = true
                        yIsGood = e.y - e2!!.y > 0
                    }
                    Entity.Direction.UP -> {
                        xIsGood = true
                        yIsGood = e.y - e2!!.y < 0
                    }
                    Entity.Direction.RIGHT -> {
                        xIsGood = e.x - e2!!.x < 0
                        yIsGood = true
                    }
                    Entity.Direction.LEFT -> {
                        xIsGood = e.x - e2!!.x > 0
                        yIsGood = true
                    }
                }
                if (xIsGood && yIsGood) {
                    victim = e2
                    break
                }
            }
        }
        if (e!!.isMe && e.health > 0) {
            swipe!!.related = e
            swipe.drawTime = 0.2f
        }
        hit!!.related = victim
        if (victim != null) {
            hit.drawTime = 0.2f
            victim.health = Math.max(0, victim.health - e.damage)
            e.attackTime = Math.min(e.stamina, e.attackTime + 1)
            if (victim.isMe) {
                Utils.playerHurt!!.play()
                if (victim.health == 0) {
                    Utils.death!!.play()
                }
            } else {
                Utils.monsterHurt!!.play()
            }
        }
    }

    fun animateSwipe(swipe: Entity?) {
        if (swipe!!.related == null) {
            return
        }
        when (swipe.related!!.lastDirection) {
            Entity.Direction.DOWN -> {
                swipe.x = swipe.related!!.x
                swipe.y = swipe.related!!.y - 1
                swipe.image = swipe.down!!.getKeyFrame(0f, true) as TextureRegion
            }
            Entity.Direction.UP -> {
                swipe.x = swipe.related!!.x
                swipe.y = swipe.related!!.y + 1
                swipe.image = swipe.up!!.getKeyFrame(0f, true) as TextureRegion
            }
            Entity.Direction.RIGHT -> {
                swipe.x = swipe.related!!.x + 1
                swipe.y = swipe.related!!.y
                swipe.image = swipe.right!!.getKeyFrame(0f, true) as TextureRegion
            }
            Entity.Direction.LEFT -> {
                swipe.x = swipe.related!!.x - 1
                swipe.y = swipe.related!!.y
                swipe.image = swipe.left!!.getKeyFrame(0f, true)
            }
        }
    }

    fun animateHit(hit: Entity?) {
        if (hit!!.related == null) {
            return
        }
        hit.x = hit.related!!.x
        hit.y = hit.related!!.y - 0.1f
    }
}