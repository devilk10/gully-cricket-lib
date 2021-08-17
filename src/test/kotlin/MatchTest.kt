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

    @Test
    fun `second team should win the match when target is chased`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val player2 = Player(2, "Foo")
        val player3 = Player(3, "Fooo")
        val player4 = Player(4, "Foooo")
        val player5 = Player(5, "Fooooo")
        val match = Match(
            Pair(
                Team("One", listOf(player, player1, player2, player3, player4, player5)),
                Team("Two", listOf(player, player1, player2, player3, player4, player5))
            ), 4
        )
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = Bowled(1)))
        match.setNewBatsman(player2)
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(4)))

        match.startNewOver(player5)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = Bowled(2)))
        match.setNewBatsman(player3)
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(0)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 22, wickets = 2, overNumber = 1, ballNumber = 6, inningState = InningState.IN_PROGRESS
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 0,
                runs = 6,
                balls = 4,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                3,
                runs = 4,
                balls = 2,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 5, runs = 7, legalBalls = 6, wickets = 1
            )
        )

        // after 2 overs

        match.startNewOver(player4)

        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = RunOut(0, 2)))
        match.setNewBatsman(player4)
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))

        match.startNewOver(player5)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(6)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 45, wickets = 3, overNumber = 3, ballNumber = 6, inningState = InningState.Completed
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 3,
                runs = 8,
                balls = 6,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                4,
                runs = 14,
                balls = 6,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 1,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 5, runs = 21, legalBalls = 12, wickets = 1
            )
        )


//         Second inning

        match.startSecondInning()

        match.startNewOver(player2)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(6)))

        match.startNewOver(player3)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(6)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(6)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 33, wickets = 0, overNumber = 1, ballNumber = 6, inningState = InningState.IN_PROGRESS
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 1,
                runs = 8,
                balls = 4,
                battingState = BattingState.BATTING,
                noOfFours = 0,
                noOfSixes = 1,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                0,
                runs = 25,
                balls = 8,
                battingState = BattingState.BATTING,
                noOfFours = 2,
                noOfSixes = 2,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 3, runs = 19, legalBalls = 6, wickets = 0
            )
        )


        match.startNewOver(player)
        match.registerBall(WideBall(Byes(0)))
        match.registerBall(WideBall(Byes(2)))
        match.registerBall(NoWicketBall(NormalRuns(6)))
        match.registerBall(NoWicketBall(NormalRuns(3)))
        match.registerBall(WicketBall(NormalRuns(2), RunOut(1, 5)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 48, wickets = 1, overNumber = 2, ballNumber = 3, inningState = InningState.Completed
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 0,
                runs = 27,
                balls = 9,
                battingState = BattingState.BATTING,
                noOfFours = 2,
                noOfSixes = 2,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                1,
                runs = 17,
                balls = 8,
                battingState = BattingState.OUT,
                noOfFours = 0,
                noOfSixes = 2,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 0, runs = 15, legalBalls = 3, wickets = 1
            )
        )
    }

    @Test
    fun `first team should win the match when second team could not chase the target`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val player2 = Player(2, "Foo")
        val player3 = Player(3, "Fooo")
        val player4 = Player(4, "Foooo")
        val player5 = Player(5, "Fooooo")
        val match = Match(
            Pair(
                Team("One", listOf(player, player1, player2, player3, player4, player5)),
                Team("Two", listOf(player, player1, player2, player3, player4, player5))
            ), 4
        )
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = Bowled(1)))
        match.setNewBatsman(player2)
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(4)))

        match.startNewOver(player5)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = Bowled(2)))
        match.setNewBatsman(player3)
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(0)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 22, wickets = 2, overNumber = 1, ballNumber = 6, inningState = InningState.IN_PROGRESS
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 0,
                runs = 6,
                balls = 4,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                3,
                runs = 4,
                balls = 2,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 5, runs = 7, legalBalls = 6, wickets = 1
            )
        )

        // after 2 overs

        match.startNewOver(player4)

        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = RunOut(0, 2)))
        match.setNewBatsman(player4)
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))

        match.startNewOver(player5)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(6)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 45, wickets = 3, overNumber = 3, ballNumber = 6, inningState = InningState.Completed
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 3,
                runs = 8,
                balls = 6,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                4,
                runs = 14,
                balls = 6,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 1,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 5, runs = 21, legalBalls = 12, wickets = 1
            )
        )


//         Second inning

        match.startSecondInning()

        match.startNewOver(player2)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(6)))

        match.startNewOver(player3)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(6)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(6)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 33, wickets = 0, overNumber = 1, ballNumber = 6, inningState = InningState.IN_PROGRESS
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 1,
                runs = 8,
                balls = 4,
                battingState = BattingState.BATTING,
                noOfFours = 0,
                noOfSixes = 1,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                0,
                runs = 25,
                balls = 8,
                battingState = BattingState.BATTING,
                noOfFours = 2,
                noOfSixes = 2,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 3, runs = 19, legalBalls = 6, wickets = 0
            )
        )


        match.startNewOver(player)
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(WicketBall(NormalRuns(2), RunOut(1, 5)))
        match.setNewBatsman(player3)


        match.startNewOver(player2)
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(0)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 35, wickets = 1, overNumber = 3, ballNumber = 6, inningState = InningState.Completed
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 3,
                runs = 0,
                balls = 0,
                battingState = BattingState.BATTING,
                noOfFours = 0,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                0,
                runs = 25,
                balls = 14,
                battingState = BattingState.BATTING,
                noOfFours = 2,
                noOfSixes = 2,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 2, runs = 14, legalBalls = 12, wickets = 0
            )
        )
    }

    @Test
    fun `first team should win the match when second team gets out`() {
        val player = Player(0, "ketan")
        val player1 = Player(1, "Ashish")
        val player2 = Player(2, "Foo")
        val player3 = Player(3, "Fooo")
        val player4 = Player(4, "Foooo")
        val player5 = Player(5, "Fooooo")
        val match = Match(
            Pair(
                Team("One", listOf(player, player1, player2, player3, player4, player5)),
                Team("Two", listOf(player, player1, player2, player3, player4, player5))
            ), 4
        )
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = Bowled(1)))
        match.setNewBatsman(player2)
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(4)))

        match.startNewOver(player5)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = Bowled(2)))
        match.setNewBatsman(player3)
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(0)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 22, wickets = 2, overNumber = 1, ballNumber = 6, inningState = InningState.IN_PROGRESS
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 0,
                runs = 6,
                balls = 4,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                3,
                runs = 4,
                balls = 2,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 5, runs = 7, legalBalls = 6, wickets = 1
            )
        )

        // after 2 overs

        match.startNewOver(player4)

        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(WicketBall(wicket = RunOut(0, 2)))
        match.setNewBatsman(player4)
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))

        match.startNewOver(player5)

        match.registerBall(NoWicketBall(NormalRuns(0)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.registerBall(NoWicketBall(NormalRuns(6)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 45, wickets = 3, overNumber = 3, ballNumber = 6, inningState = InningState.Completed
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 3,
                runs = 8,
                balls = 6,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                4,
                runs = 14,
                balls = 6,
                battingState = BattingState.BATTING,
                noOfFours = 1,
                noOfSixes = 1,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 5, runs = 21, legalBalls = 12, wickets = 1
            )
        )


//         Second inning

        match.startSecondInning()

        match.startNewOver(player2)

        match.registerBall(WicketBall(NormalRuns(0), Bowled(0)))
        match.setNewBatsman(player2)
        match.registerBall(WicketBall(NormalRuns(0), Bowled(2)))
        match.setNewBatsman(player3)
        match.registerBall(WicketBall(NormalRuns(0), Bowled(3)))
        match.setNewBatsman(player4)
        match.registerBall(WicketBall(NormalRuns(0), Bowled(4)))
        match.setNewBatsman(player5)
        match.registerBall(WicketBall(NormalRuns(0), Bowled(5)))

        assertEquals(
            match.scoreCard().teamScore, TeamScore(
                run = 0, wickets = 5, overNumber = 0, ballNumber = 5, inningState = InningState.Completed
            )
        )
        assertEquals(
            match.scoreCard().strikerScore, BatsmanScore(
                id = 5,
                runs = 0,
                balls = 1,
                battingState = BattingState.OUT,
                noOfFours = 0,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().nonStrikerScore, BatsmanScore(
                1,
                runs = 0,
                balls = 0,
                battingState = BattingState.BATTING,
                noOfFours = 0,
                noOfSixes = 0,
            )
        )
        assertEquals(
            match.scoreCard().bowlerScore, BowlerScore(
                id = 2, runs = 0, legalBalls = 5, wickets = 5
            )
        )
    }

}