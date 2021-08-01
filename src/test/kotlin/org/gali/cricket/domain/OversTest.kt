package org.gali.cricket.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OversTest {
    @Test
    fun `should have 1 ball in the over when first ball is bowled`() {
        val overs = Overs(2)
        val ball = NoWicketBall(NormalRuns(2))
        overs.addBall(ball)

        assertEquals(overs.currentOverBalls(), 1)
    }

    @Test
    fun `should register second ball of over`() {
        val overs = Overs(2)
        val ball1 = NoWicketBall(NormalRuns(2))
        val ball2 = NoWicketBall(NormalRuns(1))

        overs.addBall(ball1)
        overs.addBall(ball2)

        assertEquals(overs.currentOverBalls(), 2)
    }

    @Test
    fun `should create a new over when current over is completed`() {
        val overs = Overs(2)
        val noWicketBall = NoWicketBall(NormalRuns(1))

        repeat(6) {
            overs.addBall(noWicketBall)
        }

        overs.addBall(noWicketBall)

        assertEquals(overs.currentOverNumber(), 1)
    }

    @Test
    fun `should give total runs made so far`() {
        val overs = Overs(2)
        overs.addBall(NoWicketBall(NormalRuns(2)))
        overs.addBall(NoWicketBall(NormalRuns(3)))
        overs.addBall(NoWicketBall(NormalRuns(4)))
        overs.addBall(NoWicketBall(NormalRuns(0)))
        overs.addBall(WideBall(Byes(1)))
        overs.addBall(WicketBall(wicket = Bowled(playerId = 0)))
        overs.addBall(NoWicketBall(NormalRuns(1)))
        overs.addBall(NoWicketBall(NormalRuns(2)))
        overs.addBall(NoWicketBall(NormalRuns(3)))

        assertEquals(17, overs.totalRuns())
    }

    @Test
    fun `should give total wickets down so far`() {
        val overs = Overs(2)
        overs.addBall(NoWicketBall(NormalRuns(2)))
        overs.addBall(WideBall(Byes(1)))
        overs.addBall(WicketBall(NormalRuns(1), Bowled(playerId = 0)))
        overs.addBall(NoWicketBall(NormalRuns(1)))

        assertEquals(1, overs.totalWicket())
    }

    @Test
    fun `should mark overs completed when all overs are completed`() {
        val overs = Overs(2)

        repeat(12) {
            overs.addBall(NoWicketBall(NormalRuns(0)))
        }

        assertTrue { overs.isCompleted() }
    }

    @Test
    fun `should not mark overs completed when all over are not completed`() {
        val overs = Overs(2)

        repeat(11) {
            overs.addBall(NoWicketBall(NormalRuns(0)))
        }

        assertFalse { overs.isCompleted() }
    }

    @Test
    fun `should give current over number`() {
        val overs = Overs(2)

        repeat(10) {
            overs.addBall(NoWicketBall(NormalRuns(0)))
        }

        assertEquals(1, overs.currentOverNumber())
    }

    @Test
    fun `should give current over balls`() {
        val overs = Overs(2)

        repeat(10) {
            overs.addBall(NoWicketBall(NormalRuns(0)))
        }

        assertEquals(4, overs.currentOverBalls())
    }
}