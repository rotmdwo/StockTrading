package Binary_Search

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*
class ex8301 {
    fun main() {
        // 이분탐색 문제
        // N개의 집에 C개의 라우터를 설치하여 라우터 간 가장 좁은 간격을 최대로 하는 것이 목표
        // 시작 left: 1, right: 가장 오른쪽 집 위치 - 가장 왼쪽 집 위치
        // mid값의 간격으로 라우터 설치되는 지 확인
        // 되면 left = mid + 1
        // 안 되면 right = mid - 1
        // left > right 일 때까지 반복
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))

        var st = StringTokenizer(br.readLine())

        val N = st.nextToken().toInt()
        val C = st.nextToken().toInt()

        val houseLocation = IntArray(N)

        for (i in 0 until N) {
            houseLocation[i] = StringTokenizer(br.readLine()).nextToken().toInt()
        }

        houseLocation.sort()

        var left = 1
        var right = houseLocation[houseLocation.size - 1] - houseLocation[0]
        var mid = (right + left) / 2

        while (left <= right) {
            if (isXPossible(houseLocation, C, mid)) left = mid + 1
            else right = mid - 1
            mid = (right + left) / 2
        }

        bw.write("${mid}")
        bw.flush()
    }

    fun isXPossible(houseLocation: IntArray, C: Int, X: Int): Boolean {
        var stackedNum = 0
        var RoutersLeft = C - 1

        for (i in 1 until houseLocation.size) {
            stackedNum += houseLocation[i] - houseLocation[i - 1]

            if (stackedNum >= X) {
                stackedNum = 0
                RoutersLeft--
            }

            if (RoutersLeft <= 0) return true
        }

        return false
    }
}
