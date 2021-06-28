package org.gali.cricket.domain

sealed class Wicket {
    abstract val playerId: Int
}

data class RunOut(override val playerId: Int, val byPlayerId: Int) : Wicket()
data class Bowled(override val playerId: Int) : Wicket()