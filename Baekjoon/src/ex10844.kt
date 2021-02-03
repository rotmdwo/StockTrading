import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

fun main() {
    // DP 문제
    // N 자리 수 중 인접한 자릿수가 1차이 나는 수의 개수 찾기
    // 마지막 자리의 수가 다음 차례의 수의 개수에 영향을 미치므로
    // 마지막 자리의 수에 따라 dp 분류하기
    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.out))
    val N = StringTokenizer(br.readLine()).nextToken().toInt()

    val dp = Array(N + 1, { LongArray(5, { 0 }) })

    val BILLION = 1000000000

    dp[1][0] = 1 // 0, 9로 끝남
    dp[1][1] = 2 // 1, 8로 끝남
    dp[1][2] = 2 // 2, 7로 끝남
    dp[1][3] = 2 // 3, 6로 끝남
    dp[1][4] = 2 // 4, 5로 끝남

    for (i in 2 .. N) {
        dp[i][0] = dp[i - 1][1]
        dp[i][1] = dp[i - 1][0] + dp[i - 1][2]
        dp[i][2] = dp[i - 1][1] + dp[i - 1][3]
        dp[i][3] = dp[i - 1][2] + dp[i - 1][4]
        dp[i][4] = dp[i - 1][3] + dp[i - 1][4]

        val mappedList = dp[i].map { num ->
            if (num < BILLION) num
            else num % BILLION
        }

        dp[i] = mappedList.toLongArray()
    }

    bw.write("${dp[N].reduce { acc, i -> acc + i } % BILLION }")
    bw.flush()
}