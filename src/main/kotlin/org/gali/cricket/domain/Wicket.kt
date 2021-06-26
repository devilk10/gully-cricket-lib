package org.gali.cricket.domain

sealed class Wicket

data class RunOut(val playerId: Int, val byPlayerId: Int) : Wicket()
data class Bowled(val playerId: Int, val byPlayerId: Int) : Wicket()