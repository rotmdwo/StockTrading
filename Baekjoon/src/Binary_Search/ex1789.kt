package Binary_Search

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class ex1789 {
    fun main() {
        // 이분탐색 문제
        // 서로 다른 자연수의 합 N이 주어졌을 때, 서로 다른 자연수 개수의 최댓값 찾기가 목표
        // 시작을 left = 1, right = N으로 자연수 mid개로 N을 만들 수 있는지 이분탐색
        // mid개가 가능한 지 확인하는 방법은 1..mid까지 더했을 때 N보다 작거나 같으면 되는 거임
        val br = BufferedReader(InputStreamReader(System.`in`))
        val bw = BufferedWriter(OutputStreamWriter(System.out))
        val N = StringTokenizer(br.readLine()).nextToken().toLong()
        bw.write("${binarySearch(N)}")
        bw.flush()
    }

    // Long을 사용하는 바이너리 서치. O(logN)
    fun binarySearch(N: Long): Long {
        var left = 1L
        var right = N
        var mid = (left + right) / 2

        while (left <= right) {
            if (isXPossible(N, mid)) left = mid + 1
            else right = mid - 1
            mid = (left + right) / 2
        }

        return mid
    }

    fun isXPossible(N: Long, X: Long): Boolean {
        if (sigmaFrom1ToN(X) > N) return false
        return true
    }

    // 1부터 N까지 더하는 메서드. O(1)
    fun sigmaFrom1ToN(N: Long): Long {
        return N * (1 + N) / 2
    }
}
