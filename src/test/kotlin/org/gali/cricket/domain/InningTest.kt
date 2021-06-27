package org.gali.cricket.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InningTest {

    @Test
    fun `should return register first ball over`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val ball = NoWicketBall(2)

        val actualOver = inning.registerBall(ball)

        val expectedOver = Over(number = 0, balls = listOf(ball))

        assertEquals(actualOver, expectedOver)
    }

    @Test
    fun `should return register second ball of over`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val ball1 = NoWicketBall(2)
        val ball2 = WideBall(1)

        inning.registerBall(ball1)
        val actualOver = inning.registerBall(ball2)

        val expectedOver = Over(number = 0, balls = listOf(ball1, ball2))

        assertEquals(actualOver, expectedOver)
    }

    @Test
    fun `create a new over when current over is completed`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val noWicketBall = NoWicketBall(1)

        repeat(6) {
            inning.registerBall(noWicketBall)
        }

        val over = inning.registerBall(noWicketBall)

        assertEquals(over, Over(1, listOf(noWicketBall)))
    }

    @Test
    fun `a inning is completed when last over is completed`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val noWicketBall = NoWicketBall(1)

        repeat(12) {
            inning.registerBall(noWicketBall)
        }

        assertTrue { inning.isCompleted() }
    }

    @Test
    fun `gives score for zero balls`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val actualScoreCard = inning.scoreCard()

        val expectedTeamScore = TeamScore(
            run = 0,
            wickets = 0,
            overNumber = 0,
            ballNumber = 0,
        )

        assertEquals(expectedTeamScore, actualScoreCard.teamScore)
    }

    @Test
    fun `gives score for 1 ball`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val ball = NoWicketBall(4)
        inning.registerBall(ball)
        val actualScoreCard = inning.scoreCard()
        val expectedTeamScore = TeamScore(
            run = 4,
            wickets = 0,
            overNumber = 0,
            ballNumber = 1,
        )

        assertEquals(expectedTeamScore, actualScoreCard.teamScore)
    }

    @Test
    fun `gives score for 1 over and 1 ball`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val ball = NoWicketBall(4)
        val wideBall = WideBall()

        repeat(7) {
            inning.registerBall(ball)
            inning.registerBall(wideBall)
        }
        val actualScoreCard = inning.scoreCard()
        val expectedTeamScore = TeamScore(
            run = 35,
            wickets = 0,
            overNumber = 1,
            ballNumber = 1,
        )

        assertEquals(expectedTeamScore, actualScoreCard.teamScore)
    }

    @Test
    fun `updates score when wicket is fallen`() {
        val inning = Inning(2, listOf(0, 1), listOf(12))
        val ball = WicketBall(wicket = Bowled(playerId = 0, byPlayerId = 1), run = 0)
        inning.registerBall(ball)

        val actualScoreCard = inning.scoreCard()
        val expectedScoreCard = TeamScore(
            run = 0,
            wickets = 1,
            overNumber = 0,
            ballNumber = 1,
        )

        assertEquals(expectedScoreCard, actualScoreCard.teamScore)
    }

    @Test
    fun `updates batsman score card`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12))
        val ball = NoWicketBall(4)
        inning.registerBall(ball)

        val actualScoreCard = inning.scoreCard()
        val expectedStrikerScore = BatsmanScore(0, 4)
        val expectedNonStrikerScore = BatsmanScore(1, 0)

        assertEquals(expectedStrikerScore, actualScoreCard.strikerScore)
        assertEquals(expectedNonStrikerScore, actualScoreCard.nonStrikerScore)
    }

    @Test
    fun `updates non strikers score card`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12))
        inning.registerBall(NoWicketBall(2))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(NoWicketBall(2))

        val actualScoreCard = inning.scoreCard()
        val expectedStrikerScore = BatsmanScore(0, 7)
        val expectedNonStrikerScore = BatsmanScore(1, 3)

        assertEquals(expectedNonStrikerScore, actualScoreCard.nonStrikerScore)
        assertEquals(expectedStrikerScore, actualScoreCard.strikerScore)
    }

    @Test
    fun `updates bowler score card`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12))
        inning.registerBall(NoWicketBall(2))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(NoWicketBall(3))
        inning.registerBall(WideBall(0))
        inning.registerBall(NoWicketBall(2))
        inning.registerBall(WicketBall(0, Bowled(0, 0)))

        val actualScoreCard = inning.scoreCard()
        val expectedBowlerScoreCard = BowlerScore(12, 11, 5, 1)

        assertEquals(expectedBowlerScoreCard, actualScoreCard.bowlerScore)
    }

    @Test
    fun `updates bowler on over completion`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12, 13))

        repeat(7) {
            inning.registerBall(NoWicketBall(1))
        }

        val actualScoreCard = inning.scoreCard()
        val expectedBowlerScoreCard = BowlerScore(13, 1, 1, 0)

        assertEquals(expectedBowlerScoreCard, actualScoreCard.bowlerScore)
    }

    @Test
    fun `updates bowler when all bowler are done with bowling`() {
        val inning = Inning(2, listOf(0, 1, 2), listOf(12, 13))

        repeat(12) {
            inning.registerBall(NoWicketBall(1))
        }

        val actualScoreCard = inning.scoreCard()
        val expectedBowlerScoreCard = BowlerScore(12, 6, 6, 0)

        assertEquals(expectedBowlerScoreCard, actualScoreCard.bowlerScore)
    }
}