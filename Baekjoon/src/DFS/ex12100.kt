package DFS

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class ex12000 {
    fun main() {
        // dfs 문제
        // 이동 방향에 따른 delta값을 담는 dir 2차원 배열을 선언하면 상하좌우 방향탐색을 일괄적으로 할 수 있음
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        val n = br.readLine().toInt()
        val mat = Array(n, {Array(n, {IntArray(2)})})
        val dir = arrayOf(intArrayOf(0,0), intArrayOf(-1,0),intArrayOf(0,1), intArrayOf(1,0), intArrayOf(0,-1))

        for (i in 0 until n) {
            val st = StringTokenizer(br.readLine())
            for (j in 0 until n) {
                mat[i][j][0] = st.nextToken().toInt()
            }
        }

        bw.write("${simulate2048(mat, dir, 0, 0)}")
        bw.flush()
    }

    fun simulate2048(mat: Array<Array<IntArray>>, dir: Array<IntArray>, dirNum: Int, stage: Int): Int {
        val deltaX = dir[dirNum][0]
        val deltaY = dir[dirNum][1]

        for (i in 1 until mat.size) {
            if (dirNum == 1) {
                for (x in 1 until mat.size) {
                    for (y in 0 until mat.size) {
                        moveBlock(mat, x, y, deltaX, deltaY)
                    }
                }
            } else if (dirNum == 2) {
                for (y in mat.size - 2 downTo 0) {
                    for (x in 0 until mat.size) {
                        moveBlock(mat, x, y, deltaX, deltaY)
                    }
                }
            } else if (dirNum == 3) {
                for (x in mat.size - 2 downTo 0) {
                    for (y in 0 until mat.size) {
                        moveBlock(mat, x, y, deltaX, deltaY)
                    }
                }
            } else if (dirNum == 4) {
                for (y in 1 until mat.size) {
                    for (x in 0 until mat.size) {
                        moveBlock(mat, x, y, deltaX, deltaY)
                    }
                }
            }
        }

        if (stage < 5) {
            var max = 0
            for (i in 1..4) {
                val newMat = Array(mat.size, {Array(mat.size, {IntArray(2)})})
                for (j in 0 until mat.size) {
                    for (k in 0 until mat.size) {
                        newMat[j][k][0] = mat[j][k][0]
                        newMat[j][k][1] = 0
                    }
                }
                val result = simulate2048(newMat, dir, i, stage + 1)
                if (max < result) max = result
            }
            return max
        } else {
            var max = 0
            for (i in 0 until mat.size) {
                for (j in 0 until mat.size) {
                    if (max < mat[i][j][0]) max = mat[i][j][0]
                }
            }
            return max
        }
    }

    fun moveBlock(mat: Array<Array<IntArray>>, x: Int, y: Int, deltaX: Int, deltaY: Int) {
        if (x+deltaX < 0 || x+deltaX >= mat.size || y+deltaY < 0 || y+deltaY >= mat.size) return
        else if (mat[x+deltaX][y+deltaY][0] == 0 && mat[x][y][0] != 0) {
            mat[x+deltaX][y+deltaY] = intArrayOf(mat[x][y][0], mat[x][y][1])
            mat[x][y] = intArrayOf(0, 0)
        } else if (mat[x+deltaX][y+deltaY][1] == 0 && mat[x][y][1] == 0 && mat[x][y][0] == mat[x+deltaX][y+deltaY][0] && mat[x][y][0] != 0) {
            mat[x+deltaX][y+deltaY][0] *= 2
            mat[x+deltaX][y+deltaY][1] = 1
            mat[x][y] = intArrayOf(0, 0)
        }
    }
}