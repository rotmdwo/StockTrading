package Line_2020_FALL

fun main() {
    print(solution5(intArrayOf(3, 3, 3, 3, 3, 3, 3, 3, 3, 3)))
}

fun solution5(cards: IntArray): Int {
    var answer: Int = 0
    var index = 0

    while (true) {
        val result = playBlackJack(cards, index)

        if (result[0] == 1) {
            index = result[1]
            answer += result[2]
        } else break
    }

    return answer
}

fun playBlackJack(cards: IntArray, index: Int): IntArray {
    var cardsLeft = cards.size - index
    var i = index
    var player = 0
    var dealer = 0
    var numOfAOfPlayer = 0
    var numOfAOfDealer = 0

    if (canPlayGame(cardsLeft)) {
        cardsLeft--
        val card = cards[i]
        i++

        if (card == 1) numOfAOfPlayer++
        else if (card > 10) player += 10
        else player += card
    } else return intArrayOf(0)

    if (canPlayGame(cardsLeft)) {
        cardsLeft--
        val card = cards[i]
        i++

        if (card == 1) numOfAOfDealer++
        else if (card > 10) dealer += 10
        else dealer += card
    } else return intArrayOf(0)

    if (canPlayGame(cardsLeft)) {
        cardsLeft--
        val card = cards[i]
        i++

        if (card == 1) numOfAOfPlayer++
        else if (card > 10) player += 10
        else player += card
    } else return intArrayOf(0)

    var shownCard = 0

    if (canPlayGame(cardsLeft)) {
        cardsLeft--
        val card = cards[i]
        shownCard = card
        i++

        if (card == 1) numOfAOfDealer++
        else if (card > 10) dealer += 10
        else dealer += card


    } else return intArrayOf(0)

    if (numOfAOfPlayer == 1 && player == 10) { // jackpot
        if (numOfAOfDealer == 1 && dealer == 10) return intArrayOf(1, i, 0)
        else return intArrayOf(1, i, 3)
    } else if (shownCard == 1 || shownCard >= 7) {
        while (getScore(player, numOfAOfPlayer) < 17) {
            if (canPlayGame(cardsLeft)) {
                cardsLeft--
                val card = cards[i]
                i++

                if (card == 1) numOfAOfPlayer++
                else if (card > 10) player += 10
                else player += card

                if (getScore(player, numOfAOfPlayer) > 21) return intArrayOf(1, i, -2)
            } else return intArrayOf(0)
        }

    } else if (shownCard == 2 || shownCard == 3) {
        while (getScore(player, numOfAOfPlayer) < 12) {
            if (canPlayGame(cardsLeft)) {
                cardsLeft--
                val card = cards[i]
                i++

                if (card == 1) numOfAOfPlayer++
                else if (card > 10) player += 10
                else player += card

                if (getScore(player, numOfAOfPlayer) > 21) return intArrayOf(1, i, -2)
            } else return intArrayOf(0)
        }
    }

    while (getScore(dealer, numOfAOfDealer) < 17) {
        if (canPlayGame(cardsLeft)) {
            cardsLeft--
            val card = cards[i]
            i++

            if (card == 1) numOfAOfDealer++
            else if (card > 10) dealer += 10
            else dealer += card

            if (getScore(dealer, numOfAOfDealer) > 21) return intArrayOf(1, i, 2)
        } else return intArrayOf(0)
    }

    val totalPlayer = getScore(player, numOfAOfPlayer)
    val totalDealer = getScore(dealer, numOfAOfDealer)

    if (totalPlayer > totalDealer) return intArrayOf(1, i, 2)
    else if (totalPlayer < totalDealer) return intArrayOf(1, i, -2)
    else return intArrayOf(1, i, 0)
}

fun canPlayGame(cardsLeft: Int): Boolean {
    if (cardsLeft > 0) return true
    return false
}

fun getScore(score: Int, numOfA: Int): Int {
    if (numOfA == 0) return score

    var max = 0
    for (i in 1..numOfA) {
        val temp = score + i * 11 + (numOfA - i) * 1
        if (temp <= 21 && max < temp) max = temp
    }

    return max
}