import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Integer.min
import java.util.*
import kotlin.math.max

private var maxEatenFish = 0

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val originalMat = Array(4, {Array(4, { IntArray(2) })}) // 상어 20
    val location = Array(17, {IntArray(2)}) // 잡아먹힌 물고기 -1, -1 / 상어 idx 0


    for (i in 0..3) {
        val st = StringTokenizer(br.readLine())
        for (j in 0..3) {
            originalMat[i][j][0] = st.nextToken().toInt()
            originalMat[i][j][1] = st.nextToken().toInt()
            location[originalMat[i][j][0]] = intArrayOf(i, j)
        }
    }

    location[0] = intArrayOf(-1, -1)

    simulate(originalMat, location, 0)
    bw.write("${maxEatenFish}")
    bw.flush()
}

fun simulate(mat: Array<Array<IntArray>>, location: Array<IntArray>, eatenFish: Int) {
    var sharkX = location[0][0]
    var sharkY = location[0][1]

    if (location[0][0] == -1) {
        print("HI\n")
        maxEatenFish = mat[0][0][0]
        location[maxEatenFish] = intArrayOf(-1, -1)
        location[0] = intArrayOf(0, 0)
        mat[0][0][0] = 20

        sharkX = 0
        sharkY = 0

    }

    val sharkDir = mat[sharkX][sharkY][1]

    fishMove(mat, location)

    for (i in 0..3) {
        for (j in 0..3) {
            print("${mat[i][j][0]} ${mat[i][i][1]} ")
        }
        print("\n")
    }


    print("shark dir: ${sharkDir}\n")

    if (sharkDir == 1) {
        var didEatFish = false
        for (i in 1..sharkX) {
            if (mat[sharkX - i][sharkY][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX - i, sharkY) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    } else if (sharkDir == 2) {
        var didEatFish = false
        for (i in 1..min(sharkX, sharkY)) {
            if (mat[sharkX - i][sharkY - i][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX - i, sharkY - i) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    } else if (sharkDir == 3) {
        var didEatFish = false
        for (i in 1..sharkY) {
            if (mat[sharkX][sharkY - i][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX, sharkY - i) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    } else if (sharkDir == 4) {
        var didEatFish = false
        for (i in 1..min(3 - sharkX, sharkY)) {
            if (mat[sharkX + i][sharkY - i][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX + i, sharkY - i) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    } else if (sharkDir == 5) {
        var didEatFish = false
        for (i in 1..(3 - sharkX)) {
            if (mat[sharkX + i][sharkY][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX + i, sharkY) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    } else if (sharkDir == 6) {
        var didEatFish = false
        for (i in 1..min(3 - sharkX, 3 - sharkY)) {
            if (mat[sharkX + i][sharkY + i][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                print("shark will move to: ${sharkX + i} ${sharkY + i}\n")
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX + i, sharkY + i) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    } else if (sharkDir == 7) {
        var didEatFish = false
        for (i in 1..(3 - sharkY)) {
            if (mat[sharkX][sharkY + i][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX, sharkY + i) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    } else if (sharkDir == 8) {
        var didEatFish = false
        for (i in 1..min(sharkX, 3 - sharkY)) {
            if (mat[sharkX - i][sharkY + i][0] != -1) {
                didEatFish = true
                val newMat = mat.copyOf()
                val newLocation = location.copyOf()
                val newEatenFish = sharkMove(newMat, newLocation, sharkX, sharkY, sharkX - i, sharkY + i) + eatenFish
                if (newEatenFish > maxEatenFish) maxEatenFish = newEatenFish
                simulate(newMat, newLocation, newEatenFish)
            }
        }
        if (!didEatFish) return

    }
}

fun sharkMove(mat: Array<Array<IntArray>>, location: Array<IntArray>, sharkX: Int, sharkY: Int,
    goalX: Int, goalY: Int): Int {
    val score = mat[goalX][goalY][0]
    location[score] = intArrayOf(-1, -1)

    mat[goalX][goalY][0] = 20
    mat[sharkX][sharkY] = intArrayOf(-1, -1)
    location[0] = intArrayOf(goalX, goalY)
    print("shark eat: ${goalX} ${goalY}\n")
    return score
}

fun fishMove(mat: Array<Array<IntArray>>, location: Array<IntArray>) {
    for (fish in 1..16) {
        val row = location[fish][0]
        val col = location[fish][1]

        if (row == -1) continue

        var dir = mat[row][col][1]

        var triedCount = 0
        while (triedCount < 7) {
            triedCount++

            if (dir == 1) {
                if (row == 0 || mat[row - 1][col][0] == 20) {
                    dir++
                    mat[row][col][1] = dir
                } else {
                    if (mat[row - 1][col][0] != -1) {
                        print("Swap: ${row} ${col}, ${row - 1} ${col}\n")
                        swapLocation(mat, location, row, col, row - 1, col)
                        print("Swapped: ${mat[row]}\n")
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }

            } else if (dir == 2) {
                if (row == 0 || col == 0 || mat[row - 1][col - 1][0] == 20) {
                    dir++
                    mat[row][col][1] = dir
                } else {
                    if (mat[row - 1][col - 1][0] != -1) {
                        print("Swap: ${row} ${col}, ${row - 1} ${col - 1}\n")
                        swapLocation(mat, location, row, col, row - 1, col - 1)
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }

            } else if (dir == 3) {
                if (col == 0 || mat[row][col - 1][0] == 20) {
                    dir++
                    mat[row][col][1] = dir
                } else {
                    if (mat[row][col - 1][0] != -1) {
                        print("Swap: ${row} ${col}, ${row} ${col - 1}\n")
                        swapLocation(mat, location, row, col, row, col - 1)
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }

            } else if (dir == 4) {
                if (row == 3 || col == 0 || mat[row + 1][col - 1][0] == 20) {
                    dir++
                    mat[row][col][1] = dir
                } else {
                    if (mat[row + 1][col - 1][0] != -1) {
                        print("Swap: ${row} ${col}, ${row + 1} ${col - 1}\n")
                        swapLocation(mat, location, row, col, row + 1, col - 1)
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }

            } else if (dir == 5) {
                if (row == 3 || mat[row + 1][col][0] == 20) {
                    dir++
                    mat[row][col][1] = dir
                } else {
                    if (mat[row + 1][col][0] != -1) {
                        print("Swap: ${row} ${col}, ${row + 1} ${col}\n")
                        swapLocation(mat, location, row, col, row + 1, col)
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }

            } else if (dir == 6) {
                if (row == 3 || col == 3 || mat[row + 1][col + 1][0] == 20) {
                    dir++
                    mat[row][col][1] = dir
                } else {
                    if (mat[row + 1][col + 1][0] != -1) {
                        print("Swap: ${row} ${col}, ${row + 1} ${col + 1}\n")
                        swapLocation(mat, location, row, col, row + 1, col + 1)
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }

            } else if (dir == 7) {
                if (col == 3 || mat[row][col + 1][0] == 20) {
                    dir++
                    mat[row][col][1] = dir
                } else {
                    if (mat[row][col + 1][0] != -1) {
                        print("Swap: ${row} ${col}, ${row} ${col + 1}\n")
                        swapLocation(mat, location, row, col, row, col + 1)
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }

            } else if (dir == 8) {
                if (row == 0 || col == 3 || mat[row - 1][col + 1][0] == 20) {
                    dir = 0
                    mat[row][col][1] = dir
                } else {
                    if (mat[row - 1][col + 1][0] != -1) {
                        print("Swap: ${row} ${col}, ${row - 1} ${col + 1}\n")
                        swapLocation(mat, location, row, col, row - 1, col + 1)
                    } else {
                        moveFish(mat, location, row, col, dir)
                    }
                    break
                }
            }
        }
    }
}

fun swapLocation(mat: Array<Array<IntArray>>, location: Array<IntArray>, x1: Int, y1: Int,
                 x2: Int, y2: Int) {
    val num1 = mat[x1][y1][0]
    val num2 = mat[x2][y2][0]
    val dir1 = mat[x1][y1][1]
    val dir2 = mat[x2][y2][1]

    location[num1] = intArrayOf(x2, y2)
    location[num2] = intArrayOf(x1, y1)

    mat[x1][y1] = intArrayOf(num2, dir2)
    mat[x2][y2] = intArrayOf(num1, dir1)
}

fun moveFish(mat: Array<Array<IntArray>>, location: Array<IntArray>, x: Int, y: Int, dir: Int) {
    if (dir == 1) {
        location[mat[x][y][0]] = intArrayOf(x - 1, y)
        mat[x - 1][y] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    } else if (dir == 2) {
        location[mat[x][y][0]] = intArrayOf(x - 1, y - 1)
        mat[x - 1][y - 1] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    } else if (dir == 3) {
        print("fish move: ${x} ${y}\n")
        location[mat[x][y][0]] = intArrayOf(x, y - 1)
        mat[x][y - 1] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    } else if (dir == 4) {
        location[mat[x][y][0]] = intArrayOf(x + 1, y - 1)
        mat[x + 1][y - 1] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    } else if (dir == 5) {
        location[mat[x][y][0]] = intArrayOf(x + 1, y)
        mat[x + 1][y] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    } else if (dir == 6) {
        location[mat[x][y][0]] = intArrayOf(x + 1, y + 1)
        mat[x + 1][y + 1] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    } else if (dir == 7) {
        location[mat[x][y][0]] = intArrayOf(x, y + 1)
        mat[x][y + 1] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    } else if (dir == 8) {
        location[mat[x][y][0]] = intArrayOf(x - 1, y + 1)
        mat[x - 1][y + 1] = mat[x][y]
        mat[x][y] = intArrayOf(-1, -1)
    }
}