package org.gali.cricket

import org.gali.cricket.domain.*
import org.gali.cricket.ui.console.display

object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        val ketan = Player(0, "ketan")
        val lalit = Player(1, "Lalit")
        val joy = Player(2, "Joy")
        val gaurav = Player(3, "Gaurav")
        val match = Match(Pair(Team("Team One", listOf(joy, gaurav)), Team("Team Two", listOf(ketan, lalit))), 1)

        match.registerBall(NoWicketBall(NormalRuns(3)))
        match.scoreCard().display()
        match.registerBall(WicketBall(wicket = Bowled(3)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(6)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(6)))
        match.scoreCard().display()

        match.startSecondInning()

        match.registerBall(NoWicketBall(NormalRuns(3)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(2)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(1)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(6)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(4)))
        match.scoreCard().display()
        match.registerBall(NoWicketBall(NormalRuns(6)))
        match.scoreCard().display()
    }
}