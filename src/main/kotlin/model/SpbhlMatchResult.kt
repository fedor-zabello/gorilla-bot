package model

data class SpbhlMatchResult(
    val teams: Pair<String, String>,
    val score: Pair<Int, Int>,
){
    fun isDraw(): Boolean {
        return score.first == score.second
    }

    fun gorillaWon(): Boolean {
        return (score.first > score.second && teams.first.contains("Горилла")
                || score.second > score.first && teams.second.contains("Горилла"))
    }
}
