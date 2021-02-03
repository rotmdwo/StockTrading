package Kakao_2020_Blind

fun main() {
    val board = arrayOf(intArrayOf(1, 0, 2, 3), intArrayOf(5, 0, 4, 6),
            intArrayOf(5,1,0,6), intArrayOf(2,4,0,3))
    val r = 2
    val c = 3
    print("${solution6(board, r, c)}")
}

fun solution6(board: Array<IntArray>, r: Int, c: Int): Int {
    var answer: Int = 0
    var cardsLeft = 0
    var x = r
    var y = c

    for (i in 0..3) {
        for (j in 0..3) {
            if (board[i][j] != 0) answer += 1
        }
    }
    cardsLeft = answer

    while (cardsLeft > 0) {
        if (board[x][y] != 0) {
            val parallelLocation = findParallelLocation(board, x, y, true)
            if (parallelLocation[0] != -1) {
                board[x][y] = 0
                x = parallelLocation[0]
                y = parallelLocation[1]
                board[x][y] = 0
                answer++
            } else {
                val farLocation = findFarLocation(board, x, y, true)
                board[x][y] = 0
                x = farLocation[0]
                y = farLocation[1]
                board[x][y] = 0
                answer += 2
            }
        } else {
            val parallelLocation = findParallelLocation(board, x, y, false)

            if (parallelLocation[0] != -1) {
                val parallelLocation2 = findParallelLocation(board, parallelLocation[0],
                        parallelLocation[1], true)

                if (parallelLocation2[0] != -1) {
                    board[parallelLocation[0]][parallelLocation[1]] = 0
                    x = parallelLocation2[0]
                    y = parallelLocation2[1]
                    board[x][y] = 0
                    answer += 2
                } else {
                    val farLocation = findFarLocation(board, parallelLocation[0],
                            parallelLocation[1], true)

                    board[parallelLocation[0]][parallelLocation[1]] = 0
                    x = farLocation[0]
                    y = farLocation[1]
                    board[x][y] = 0
                    answer += 3
                }
            } else {
                val farLocation = findFarLocation(board, x, y, false)
                val parallelLocation2 = findParallelLocation(board, farLocation[0],
                        farLocation[1], true)

                if (parallelLocation2[0] != -1) {
                    board[farLocation[0]][farLocation[1]] = 0
                    x = parallelLocation2[0]
                    y = parallelLocation2[1]
                    board[x][y] = 0
                    answer += 3
                } else {
                    val farLocation2 = findFarLocation(board, farLocation[0],
                            farLocation[1], true)

                    board[farLocation[0]][farLocation[1]] = 0
                    x = farLocation2[0]
                    y = farLocation2[1]
                    board[x][y] = 0
                    answer += 4
                }
            }

        }
        cardsLeft -= 2
    }

    return answer
}

fun findParallelLocation(board: Array<IntArray>, x: Int, y: Int, hasCard: Boolean): IntArray {
    val location = intArrayOf(-1, -1)

    if (hasCard) {
        val card = board[x][y]

        for (i in 0..3) {
            if (y != i && board[x][i] == card) {
                location[0] = x
                location[1] = i
            }
        }

        for (i in 0..3) {
            if (x != i && board[i][y] == card) {
                location[0] = i
                location[1] = y
            }
        }
    } else {
        for (i in 0..3) {
            if (board[x][i] != 0) {
                location[0] = x
                location[1] = i
            }
        }

        for (i in 0..3) {
            if (board[i][y] != 0) {
                location[0] = i
                location[1] = y
            }
        }
    }

    return location
}

fun findFarLocation(board: Array<IntArray>, x: Int, y: Int, hasCard: Boolean): IntArray {
    val location = intArrayOf(-1, -1)

    if (hasCard) {
        val card = board[x][y]

        for (i in 0..3) {
            for (j in 0..3) {
                if (x != i && y != j && board[i][j] == card) {
                    location[0] = i
                    location[1] = j
                }
            }
        }
    } else {
        for (i in 0..3) {
            for (j in 0..3) {
                if (board[i][j] != 0) {
                    location[0] = i
                    location[1] = j
                }
            }
        }
    }
    return location
}