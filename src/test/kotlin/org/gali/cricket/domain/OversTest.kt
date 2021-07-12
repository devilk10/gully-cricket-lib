package org.gali.cricket.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OversTest {
    @Test
    fun `should register first ball over`() {
        val overs = Overs(2)
        val ball = NoWicketBall(2)
        val actualOver = overs.addBall(ball)

        val expectedOver = Over(number = 0, balls = listOf(ball))

        assertEquals(actualOver, expectedOver)
    }

    @Test
    fun `should register second ball of over`() {
        val overs = Overs(2)
        val ball1 = NoWicketBall(2)
        val ball2 = WideBall(1)

        overs.addBall(ball1)
        val actualOver = overs.addBall(ball2)

        val expectedOver = Over(number = 0, balls = listOf(ball1, ball2))

        assertEquals(actualOver, expectedOver)
    }

    @Test
    fun `should create a new over when current over is completed`() {
        val overs = Overs(2)
        val noWicketBall = NoWicketBall(1)

        repeat(6) {
            overs.addBall(noWicketBall)
        }

        val over = overs.addBall(noWicketBall)

        assertEquals(over, Over(1, listOf(noWicketBall)))
    }

    @Test
    fun `should give total runs made so far`() {
        val overs = Overs(2)
        overs.addBall(NoWicketBall(2))
        overs.addBall(NoWicketBall(3))
        overs.addBall(NoWicketBall(4))
        overs.addBall(NoWicketBall(0))
        overs.addBall(WideBall(1))
        overs.addBall(WicketBall(0, Bowled(playerId = 0)))
        overs.addBall(NoWicketBall(1))
        overs.addBall(NoWicketBall(2))
        overs.addBall(NoWicketBall(3))

        assertEquals(17, overs.totalRuns())
    }

    @Test
    fun `should give total wickets down so far`() {
        val overs = Overs(2)
        overs.addBall(NoWicketBall(2))
        overs.addBall(WideBall(1))
        overs.addBall(WicketBall(0, Bowled(playerId = 0)))
        overs.addBall(NoWicketBall(1))

        assertEquals(1, overs.totalWicket())
    }

    @Test
    fun `should mark overs completed when all overs are completed`() {
        val overs = Overs(2)

        repeat(12) {
            overs.addBall(NoWicketBall(0))
        }

        assertTrue { overs.isCompleted() }
    }

    @Test
    fun `should not mark overs completed when all over are not completed`() {
        val overs = Overs(2)

        repeat(11) {
            overs.addBall(NoWicketBall(0))
        }

        assertFalse { overs.isCompleted() }
    }

    @Test
    fun `should give current over number`() {
        val overs = Overs(2)

        repeat(10) {
            overs.addBall(NoWicketBall(0))
        }

        assertEquals(1, overs.currentOverNumber())
    }

    @Test
    fun `should give current over balls`() {
        val overs = Overs(2)

        repeat(10) {
            overs.addBall(NoWicketBall(0))
        }

        assertEquals(4, overs.currentOverBalls())
    }
}