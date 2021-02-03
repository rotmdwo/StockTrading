package BFS

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ex1525 {
    fun main() {
        // BFS, 메모리 관리 문제
        // BFS는 배열에 배치 되어 있는 숫자들의 형태 즉 위상을 기준으로 탐색
        // visited는 Hash를 사용하여 메모리를 줄이며, 정의역을 배열의 위상으로 함
        // 배열의 위상을 효율적으로 표현하기 위해 mat[2][1] -> 1의 자리, mat[0][0] -> 10000000의 자리
        // 로 하며, mat[2][2]는 앞의 수에 따라 자동으로 결정되기 때문에 포함시키지 않음
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))

        val visited = HashMap<Int, Boolean>(10000000)
        var answer = 0
        var foundAnswer = false
        var mat = Array(3, {IntArray(3)})
        // IntArray[0]: 3x3 배열의 숫자형, IntArray[1]: level, IntArray[2]: 0의 위치 X, IntArray[3]: 0의 위치 Y
        val queue = ArrayList<IntArray>(1000000)

        // 0 위치 찾기
        var x = 2
        var y = 2

        for (i in 0..2) {
            var st = StringTokenizer(br.readLine())

            for (j in 0..2) {
                mat[i][j] = st.nextToken().toInt()
                if (mat[i][j] == 0) {
                    x = i
                    y = j
                }
            }
        }

        queue.add(intArrayOf(matToDecimal(mat), answer, x, y))
        visited[queue[0][0]] = true

        // BFS
        while (queue.isNotEmpty()) {
            val itemFromQueue = queue.removeAt(0)
            mat = decimalToMat(itemFromQueue[0])
            val level = itemFromQueue[1]
            x = itemFromQueue[2]
            y = itemFromQueue[3]

            // 답 찾음
            if (itemFromQueue[0] == 12345678) {
                answer = level
                foundAnswer = true
                break
            }

            if (x > 0) { // 위쪽 탐색
                search4Directions(mat, queue, visited, level, x, y, -1, 0)
            }

            if (y < 2) { // 오른쪽 탐색
                search4Directions(mat, queue, visited, level, x, y, 0, 1)
            }

            if (x < 2) { // 아래 탐색
                search4Directions(mat, queue, visited, level, x, y, 1, 0)
            }

            if (y > 0) { // 왼쪽 탐색
                search4Directions(mat, queue, visited, level, x, y, 0, -1)
            }
        }

        if (foundAnswer) bw.write("${answer}")
        else bw.write("-1")

        bw.flush()
    }

    fun toDecimal(a: Int, b: Int, c:Int, d: Int, e: Int, f: Int, g: Int, h: Int): Int {
        return h + g * 10 + f * 100 + e * 1000 + d * 10000 + c * 100000 + b * 1000000 + a * 10000000
    }

    fun matToDecimal(mat: Array<IntArray>): Int {
        return toDecimal(mat[0][0], mat[0][1], mat[0][2], mat[1][0], mat[1][1], mat[1][2], mat[2][0], mat[2][1])
    }

    fun decimalToMat(decimal: Int): Array<IntArray> {
        val arrayList = ArrayList<Int>()
        for (i in 0..8) {
            arrayList.add(i)
        }

        if (decimal >= 10000000) {
            for (i in 0..7) {
                arrayList.remove((decimal / Math.pow(10.0, i.toDouble())).toInt() % 10)
            }

            return arrayOf(intArrayOf(decimal / 10000000, (decimal / 1000000) % 10, (decimal / 100000) % 10),
                    intArrayOf((decimal / 10000) % 10, (decimal / 1000) % 10, (decimal / 100) % 10),
                    intArrayOf((decimal / 10) % 10, decimal % 10, arrayList.get(0)))
        } else {
            for (i in 0..6) {
                arrayList.remove((decimal / Math.pow(10.0, i.toDouble())).toInt() % 10)
            }
            arrayList.remove(0)

            return arrayOf(intArrayOf(0, (decimal / 1000000) % 10, (decimal / 100000) % 10),
                    intArrayOf((decimal / 10000) % 10, (decimal / 1000) % 10, (decimal / 100) % 10),
                    intArrayOf((decimal / 10) % 10, decimal % 10, arrayList.get(0)))
        }
    }

    fun search4Directions(mat: Array<IntArray>, queue: ArrayList<IntArray>, visited: HashMap<Int, Boolean>,
                          level: Int, x: Int, y:Int, dx: Int, dy: Int) {
        val newMat = Array(3, { IntArray(3) })
        for (i in 0..2) {
            System.arraycopy(mat[i], 0, newMat[i], 0, 3)
        }
        val tempNum = mat[x + dx][y + dy]
        newMat[x + dx][y + dy] = mat[x][y]
        newMat[x][y] = tempNum

        val decimal = matToDecimal(newMat)

        if(visited[decimal] == null) {
            visited[decimal] = true
            queue.add(intArrayOf(decimal, level + 1, x + dx, y + dy))
        }
    }
}