package org.gali.cricket.ui.console

import org.gali.cricket.domain.BatsmanScore
import org.gali.cricket.domain.BowlerScore
import org.gali.cricket.domain.ScoreCardSummary
import org.gali.cricket.domain.TeamScore

fun ScoreCardSummary.display() {
    val rowWidth = teamScore.displayString().length + 20
    println(startLine(rowWidth))
    println(tableRow(teamScore.displayString(), rowWidth))
    println(tableRow(strikerScore.displayString(), rowWidth))
    println(tableRow(nonStrikerScore.displayString(), rowWidth))
    println(tableRow(bowlerScore.displayString(), rowWidth))
}

fun tableRow(tableData: String, rowWidth: Int = tableData.length + 20): String {
    val line = startLine(rowWidth)
    return tableData.rightAlign(rowWidth) + "\n" + line
}

private fun startLine(rowWidth: Int) = "_".repeat(rowWidth)

fun String.rightAlign(containerWidth: Int): String {
    return " ".repeat(containerWidth - length) + this
}

fun TeamScore.displayString(): String {
    return "${run}-${wickets} (${overNumber}.${ballNumber})"
}

fun BatsmanScore.displayString(): String {
    return "$id $runs $balls"
}

fun BowlerScore.displayString(): String {
    return "$id $runs ${legalBalls / 6}.${legalBalls % 6}"
}