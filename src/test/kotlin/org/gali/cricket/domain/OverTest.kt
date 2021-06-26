package org.gali.cricket.domain

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class OverTest {

    @Test
    fun `an over less than 6 legal deliveries is not completed`() {
        val noWicketBall = NoWicketBall(2)
        val wicketBall = WicketBall(wicket = Bowled(0, 0))
        val wideBall = WideBall(2)

        assertFalse(Over(0, listOf(noWicketBall)).isCompleted())

        assertFalse(Over(0, listOf(
            noWicketBall,
            wicketBall,
            noWicketBall,
            wideBall,
            noWicketBall,
            noWicketBall
        )).isCompleted())
    }

    @Test
    fun `an over with 6 legal deliveries is completed`() {
        val noWicketBall = NoWicketBall(2)
        val wicketBall = WicketBall(wicket = Bowled(0, 0))
        val wideBall = WideBall(2)

        assertTrue(Over(0, listOf(
            noWicketBall,
            wicketBall,
            noWicketBall,
            wideBall,
            noWicketBall,
            noWicketBall,
            noWicketBall
        )).isCompleted())

        assertTrue(Over(0, listOf(
            noWicketBall,
            wicketBall,
            noWicketBall,
            noWicketBall,
            noWicketBall,
            noWicketBall
        )).isCompleted())
    }

    @Test
    fun `gives total wickets in the over`() {
        val wicketBall = WicketBall(wicket = Bowled(0, 0))

        assertEquals(
            Over(0, listOf()).totalWickets(),
            0
        )
        assertEquals(
            Over(0, listOf(wicketBall)).totalWickets(),
            1
        )
        assertEquals(
            Over(0, listOf(wicketBall, NoWicketBall(1))).totalWickets(),
            1
        )

    }

    @Test
    fun `gives total number of runs`() {
        val noWicketBall = NoWicketBall(2)
        assertEquals(Over(0, listOf()).totalRuns(), 0)
        assertEquals(Over(0, listOf(noWicketBall, noWicketBall)).totalRuns(), 4)
        assertEquals(Over(0, listOf(noWicketBall, WideBall())).totalRuns(), 3)
        assertEquals(Over(0, listOf(noWicketBall, WideBall(), WicketBall(runs = 3, wicket =Bowled(
            playerId = 0,
            byPlayerId = 0
        )))).totalRuns(), 6)
    }

    @Test
    fun `gives number of legal balls`() {
        val noWicketBall = NoWicketBall(2)
        val wicketBall = WicketBall(wicket = Bowled(0, 0))
        val wideBall = WideBall(2)

        assertEquals(Over(0, listOf()).numberOfLegalBalls(), 0)
        assertEquals(Over(0, listOf(noWicketBall)).numberOfLegalBalls(), 1)
        assertEquals(Over(0, listOf(
            noWicketBall,
            wicketBall,
            noWicketBall,
            wideBall,
            noWicketBall,
            noWicketBall
        )).numberOfLegalBalls(), 5)
    }
}