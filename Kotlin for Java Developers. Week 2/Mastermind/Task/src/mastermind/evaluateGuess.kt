package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var correct = 0
    var misplaced = 0
    var secretTmp = secret
    var guessTmp = guess
    for ((idx, ch) in guess.withIndex()) {
        if (ch == secret[idx]) {
            correct++
            secretTmp = secretTmp.substring(0 until idx) + "_" + secretTmp.substring(idx + 1 until secretTmp.length)
            guessTmp = guessTmp.substring(0 until idx) + "-" + guessTmp.substring(idx + 1 until guessTmp.length)
        }
    }
    for (ch in guessTmp) {
        if (ch in secretTmp) {
            misplaced++
            secretTmp = secretTmp.replaceFirst(ch, '_', true)
        }
    }

    return Evaluation(correct, misplaced);
}
