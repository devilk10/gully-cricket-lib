import org.gali.cricket.domain.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MatchTest {

    @Test
    fun `should update scorecard of the team`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val match = Match(Pair(Team("", listOf(player, player1)), Team("", listOf(player, player1))), 6)
        match.registerBall(NoWicketBall(4))
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
            match.registerBall(NoWicketBall(4))
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

}