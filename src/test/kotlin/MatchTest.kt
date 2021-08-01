import org.gali.cricket.domain.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MatchTest {

    @Test
    fun `should update scorecard of the team`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val match = Match(Pair(Team("", listOf(player, player1)), Team("", listOf(player, player1))), 6)
        match.registerBall(NoWicketBall(NormalRuns(4)))
        val expectedTeamScore = TeamScore(
            run = 4,
            wickets = 0,
            overNumber = 0,
            ballNumber = 1,
            inningState = InningState.IN_PROGRESS,
        )
        assertEquals(match.scoreCard().teamScore, expectedTeamScore)
    }

    @Test
    fun `should change innings when overs are bowled for one inning`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val match = Match(Pair(Team("", listOf(player, player1)), Team("", listOf(player, player1))), 1)
        repeat(6) {
            match.registerBall(NoWicketBall(NormalRuns(4)))
        }
        match.startSecondInning()
        val expectedTeamScore = TeamScore(
            run = 0,
            wickets = 0,
            overNumber = 0,
            ballNumber = 0,
            inningState = InningState.IN_PROGRESS,
        )
        assertEquals(match.scoreCard().teamScore, expectedTeamScore)
    }

    @Test
    fun `should have Ashish as a bowler when new over is started`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val match = Match(Pair(Team("", listOf(player, player1)), Team("", listOf(player, player1))), 2)
        repeat(6) {
            match.registerBall(NoWicketBall(NormalRuns(4)))
        }
        match.startNewOver(player1)
        assertEquals(match.scoreCard().bowlerScore, BowlerScore(1, 0, 0, 0))
    }

    @Test
    fun `should have Foo as new batsman when wicket falls`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val player2 = Player(2, "Foo")
        val match = Match(Pair(Team("", listOf(player, player1, player2)), Team("", listOf(player, player1))), 2)
        match.registerBall(WicketBall(wicket = Bowled(0)))
        match.setNewBatsman(player2)

        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                2,
                runs = 0,
                balls = 0,
                battingState = BattingState.BATTING,
                noOfFours = 0,
                noOfSixes = 0,
            )
        )
    }
}